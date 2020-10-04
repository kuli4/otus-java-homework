package pro.kuli4.otus.java.hw12.server.servlets;

import pro.kuli4.otus.java.hw12.service.TemplateProcessor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IndexServlet extends HttpServlet {

    private final static String INDEX_TEMPLATE = "index.ftl";

    private final TemplateProcessor templateProcessor;

    public IndexServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            Map<String, Object> paramsMap = new HashMap<>();
            String event = request.getParameter("event");
            if (event != null) {
                switch (event) {
                    case "invalidCredentials" -> paramsMap.put("message", "You have passed invalid credentials. Please try again.");
                    case "authRequired" -> paramsMap.put("message", "Please enter login and password for a restricted area access");
                }
            }
            paramsMap.put("hasMessage", paramsMap.containsKey("message"));

            response.setContentType("text/html");
            response.getWriter().println(templateProcessor.getPage(INDEX_TEMPLATE, paramsMap));
        } else {
            response.sendRedirect("/private/");
        }
    }
}
