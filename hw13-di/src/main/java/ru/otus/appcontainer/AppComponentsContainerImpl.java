package ru.otus.appcontainer;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.*;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;


import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
        log.debug("appComponents: {}", appComponents);
        log.debug("appComponentsByName: {}", appComponentsByName);
    }

    public AppComponentsContainerImpl(Class<?>... configClasses) {
        processConfigArray(Arrays.stream(configClasses)
                .filter(clazz -> clazz.isAnnotationPresent(AppComponentsContainerConfig.class)).toArray()
        );
    }

    public AppComponentsContainerImpl(String packageName) {
        Reflections reflections = new Reflections(packageName, new TypeAnnotationsScanner());
        processConfigArray(reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class, true).toArray());

    }

    private void processConfigArray(Object[] classes) {
        Arrays.stream(classes)
                .map(clazz -> (Class<?>) clazz)
                .sorted(Comparator.comparingInt(clazz -> clazz.getAnnotation(AppComponentsContainerConfig.class).order()))
                .forEachOrdered(this::processConfig);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        try {
            Object configObj = configClass.getDeclaredConstructor().newInstance();
            Arrays.stream(configClass.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(AppComponent.class))
                    .sorted(Comparator.comparingInt(method -> method.getAnnotation(AppComponent.class).order()))
                    .forEachOrdered(method -> this.processComponent(configObj, method));
        } catch (Exception e) {
            throw new AppComponentsContainerException(e);
        }
    }

    private void processComponent(Object obj, Method method) {
        Object[] args = Arrays.stream(method.getParameterTypes()).map(this::getAppComponent).toArray();
        log.debug("args for {} method: {}", method.getName(), Arrays.asList(args));
        try {
            Object newComponent = method.invoke(obj, args);
            appComponents.add(newComponent);
            appComponentsByName.put(method.getAnnotation(AppComponent.class).name(), newComponent);
        } catch (Exception e) {
            throw new AppComponentsContainerException(e);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponents.stream()
                .filter(component -> componentClass.isAssignableFrom(component.getClass()))
                .findFirst()
                .orElseThrow(() -> new AppComponentsContainerException(String.format("There isn't %s component in context!", componentClass.getName())));
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
