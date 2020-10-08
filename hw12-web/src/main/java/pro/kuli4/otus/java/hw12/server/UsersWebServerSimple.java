package pro.kuli4.otus.java.hw12.server;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import pro.kuli4.otus.java.hw12.dao.UserDao;
import pro.kuli4.otus.java.hw12.helpers.FileSystemHelper;
import pro.kuli4.otus.java.hw12.server.servlets.AuthorizationFilter;
import pro.kuli4.otus.java.hw12.server.servlets.IndexServlet;
import pro.kuli4.otus.java.hw12.server.servlets.LoginServlet;
import pro.kuli4.otus.java.hw12.server.servlets.UsersApiServlet;
import pro.kuli4.otus.java.hw12.service.PassEncoder;
import pro.kuli4.otus.java.hw12.service.TemplateProcessor;
import pro.kuli4.otus.java.hw12.service.UserAuthService;

import java.util.Arrays;

@Slf4j
public class UsersWebServerSimple implements UsersWebServer {
    private static final String RESOURCES_DIR = "static";
    private static final String LOGIN_URL = "/login/";

    private final UserDao userDao;
    private final UserAuthService authService;
    private final TemplateProcessor templateProcessor;
    private final Gson gson;
    private final Server server;
    private final PassEncoder passEncoder;
    private final String[] restrictAreaPaths = new String[]{"/api/*", "/private/*"};

    public UsersWebServerSimple(int port,
                                UserAuthService authService,
                                UserDao userDao,
                                TemplateProcessor templateProcessor,
                                Gson gson,
                                PassEncoder passEncoder) {
        this.userDao = userDao;
        this.authService = authService;
        this.templateProcessor = templateProcessor;
        this.gson = gson;
        this.passEncoder = passEncoder;
        server = new Server(port);
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private void initContext() {
        HandlerList handlers = new HandlerList();
        ServletContextHandler servletContextHandler = createServletContextHandler();
        handlers.addHandler(applySecurity(servletContextHandler, restrictAreaPaths));

        server.setHandler(handlers);
    }

    private Handler applySecurity(ServletContextHandler servletContextHandler, String[] paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(authService)), LOGIN_URL);
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths).forEachOrdered(path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(RESOURCES_DIR));
        servletContextHandler.setContextPath("/");

        ServletHolder resourceServletHolder = new ServletHolder(new DefaultServlet());
        resourceServletHolder.setInitParameter("dirAllowed", "false");
        resourceServletHolder.setInitParameter("welcomeServlets", "true");
        servletContextHandler.addServlet(resourceServletHolder, "/");

        servletContextHandler.addServlet(new ServletHolder(new IndexServlet(templateProcessor)), "/index.html");
        servletContextHandler.addServlet(new ServletHolder(new UsersApiServlet(userDao, gson, passEncoder)), "/api/user/");
        return servletContextHandler;
    }
}
