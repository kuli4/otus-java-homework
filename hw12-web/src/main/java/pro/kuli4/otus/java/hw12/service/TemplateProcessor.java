package pro.kuli4.otus.java.hw12.service;

import java.io.IOException;
import java.util.Map;

public interface TemplateProcessor {
    String getPage(String filename, Map<String, Object> data) throws IOException;
}
