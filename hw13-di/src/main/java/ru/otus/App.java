package ru.otus;

import ru.otus.appcontainer.AppComponentsContainerImpl;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.config.AppConfig;
import ru.otus.configs.AppConfigAgain;
import ru.otus.configs.AppConfigToo;
import ru.otus.services.GameProcessor;
import ru.otus.services.GameProcessorImpl;




/*
*
* For start:
* ./gradlew hw13-di:clean hw13-di:build
* java -jar hw13-di/build/libs/hw13-di-0.0.1.jar
*

В классе AppComponentsContainerImpl реализовать обработку, полученной в конструкторе конфигурации,
основываясь на разметке аннотациями из пакета appcontainer. Так же необходимо реализовать методы getAppComponent.
В итоге должно получиться работающее приложение. Менять можно только класс AppComponentsContainerImpl.

PS Приложение представляет из себя тренажер таблицы умножения)
*/

public class App {

    public static void main(String[] args) throws Exception {
        // optional - implemented
        //AppComponentsContainer container = new AppComponentsContainerImpl(AppConfigAgain.class, AppConfigToo.class);

        // optional - implemented
        AppComponentsContainer container = new AppComponentsContainerImpl("ru.otus.configs");

        // Mandatory - implemented
        //AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig.class);

        // Приложение должно работать в каждом из указанных ниже вариантов
        //GameProcessor gameProcessor = container.getAppComponent(GameProcessor.class);
        GameProcessor gameProcessor = container.getAppComponent(GameProcessorImpl.class);
        //GameProcessor gameProcessor = container.getAppComponent("gameProcessor");

        gameProcessor.startGame();
    }
}
