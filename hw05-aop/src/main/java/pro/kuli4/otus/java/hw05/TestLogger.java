package pro.kuli4.otus.java.hw05;

public class TestLogger {
    private final int i;

    public TestLogger(int i) {
        this.i = i;
    }

    @Log
    public int print() {
        System.out.println(i);
        return 11;
    }

    @Log
    public void printString(String str) {
        System.out.println(str);
    }

    public void print(int x) {

    }

    @Log
    public void print(long x, short s) {
        System.out.println("hi");
    }
}
