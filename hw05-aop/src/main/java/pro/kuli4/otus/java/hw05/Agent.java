package pro.kuli4.otus.java.hw05;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.InstructionAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

public class Agent {
    private static final String CLASS_INTERNAL_NAME = "pro/kuli4/otus/java/hw05/TestLogger";
    private static final String ANNOTATION_DESCRIPTOR = "Lpro/kuli4/otus/java/hw05/Log;";
    private static final List<MethodPointer> logMethods = new ArrayList<>();

    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) {
                if (className.equals(CLASS_INTERNAL_NAME)) {
                    return addProxyMethods(classfileBuffer);
                }
                return classfileBuffer;
            }
        });
    }

    public static byte[] addProxyMethods(byte[] originalClass) {
        ClassReader cr = new ClassReader(originalClass);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM8) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                return new CustomMethodVisitor(Opcodes.ASM8, mv, name, descriptor) {
                    @Override
                    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
                        if (descriptor.equals(ANNOTATION_DESCRIPTOR)) {
                            Agent.logMethods.add(this.mp);
                        }
                        return null;
                    }
                };
            }
        };
        cr.accept(cv, Opcodes.ASM8);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        TraceClassVisitor tcv = new TraceClassVisitor(cw, null); // new PrintWriter(System.out)
        cv = new ClassVisitor(Opcodes.ASM8, tcv) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                if (Agent.logMethods.contains(new MethodPointer(name, descriptor))) {
                    return super.visitMethod(access, name + "Proxied", descriptor, signature, exceptions);
                }
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            }
        };
        cr.accept(cv, Opcodes.ASM8);
        cv = new ClassVisitor(Opcodes.ASM8, tcv) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                return new InstructionAdapter(super.visitMethod(access, name, descriptor, signature, exceptions));
            }
        };
        for (MethodPointer mp : logMethods) {
            InstructionAdapter mv = (InstructionAdapter) cv.visitMethod(Opcodes.ACC_PUBLIC, mp.getMethodName(), mp.getMethodDescriptor(), null, null);

            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            String ls = System.lineSeparator();
            StringBuilder log = new StringBuilder(ls + "Executed method: " + mp.getMethodName() + mp.getMethodDescriptor() + ls);
            Type[] argumentsTypes = Type.getArgumentTypes(mp.getMethodDescriptor());
            if (argumentsTypes.length == 0) {
                log.append("\tNone args");
                mv.aconst(log.toString());
            } else {
                log.append("With args:").append(ls);
                StringBuilder concatDescriptor = new StringBuilder("(");
                int i = 1;
                for (Type type : argumentsTypes) {
                    log.append("\t").append(type.getClassName()).append("Param = \u0001").append(ls);
                    mv.visitVarInsn(type.getOpcode(Opcodes.ILOAD), i);
                    i += type.getSize();
                    concatDescriptor.append(type.getDescriptor());
                }
                concatDescriptor.append(")Ljava/lang/String;");
                mv.visitInvokeDynamicInsn("makeConcatWithConstants",
                        concatDescriptor.toString(),
                        new Handle(Opcodes.H_INVOKESTATIC,
                                "java/lang/invoke/StringConcatFactory",
                                "makeConcatWithConstants",
                                Type.getMethodDescriptor(Type.getType(CallSite.class),
                                        Type.getType(MethodHandles.Lookup.class),
                                        Type.getType(String.class),
                                        Type.getType(MethodType.class),
                                        Type.getType(String.class),
                                        Type.getType(Object[].class)),
                                false),
                        log.toString());
            }

            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            if (argumentsTypes.length > 0) {
                int i = 1;
                for (Type type : argumentsTypes) {
                    mv.visitVarInsn(type.getOpcode(Opcodes.ILOAD), i);
                    i += type.getSize();
                }
            }

            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, CLASS_INTERNAL_NAME, mp.getMethodName() + "Proxied", mp.getMethodDescriptor(), false);

            mv.visitInsn(Type.getReturnType(mp.getMethodDescriptor()).getOpcode(Opcodes.IRETURN));

            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }

        byte[] finalClass = cw.toByteArray();

        try (OutputStream fos = new FileOutputStream("proxyASM.class")) {
            fos.write(finalClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalClass;
    }

    private static class CustomMethodVisitor extends MethodVisitor {
        final MethodPointer mp;

        CustomMethodVisitor(int api, MethodVisitor mv, String methodName, String methodDescriptor) {
            super(api, mv);
            this.mp = new MethodPointer(methodName, methodDescriptor);
        }
    }


    private static class MethodPointer {
        private final String methodName;
        private final String methodDescriptor;

        MethodPointer(String methodName, String methodDescriptor) {
            this.methodName = methodName;
            this.methodDescriptor = methodDescriptor;
        }

        public String getMethodName() {
            return methodName;
        }

        public String getMethodDescriptor() {
            return methodDescriptor;
        }


        @Override
        public boolean equals(Object o) {
            if (o == null) return false;
            if (!(o instanceof MethodPointer)) return false;
            MethodPointer mp = (MethodPointer) o;
            return mp.getMethodName().equals(this.getMethodName()) && mp.getMethodDescriptor().equals(this.getMethodDescriptor());
        }

        @Override
        public String toString() {
            return System.lineSeparator() + this.methodName + " ||| " + this.methodDescriptor;
        }
    }
}
