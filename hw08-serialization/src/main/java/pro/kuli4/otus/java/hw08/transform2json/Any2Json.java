package pro.kuli4.otus.java.hw08.transform2json;

import javax.json.JsonStructure;

abstract class Any2Json {
    private Any2Json wrapper;
    protected String name;

    public Any2Json getWrapper() {
        return wrapper;
    }

    public void setWrapper(Any2Json any2Json) {
        this.wrapper = any2Json;
    }

    abstract void addChild(String name, JsonStructure js);

    public void setName(String name) {
        this.name = name;
    }
}
