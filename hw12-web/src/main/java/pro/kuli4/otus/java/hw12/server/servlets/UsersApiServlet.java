package pro.kuli4.otus.java.hw12.server.servlets;

import com.google.gson.Gson;
import pro.kuli4.otus.java.hw12.dao.UserDao;
import pro.kuli4.otus.java.hw12.entities.PhoneDataSet;
import pro.kuli4.otus.java.hw12.entities.User;
import pro.kuli4.otus.java.hw12.service.PassEncoder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class UsersApiServlet extends HttpServlet {

    private final UserDao userDao;
    private final Gson gson;
    private final PassEncoder passEncoder;


    public UsersApiServlet(UserDao userDao, Gson gson, PassEncoder passEncoder) {
        this.userDao = userDao;
        this.gson = gson;
        this.passEncoder = passEncoder;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getOutputStream().print(gson.toJson(userDao.getAllUsers()));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            User user = gson.fromJson(request.getReader(), User.class);
            for (PhoneDataSet phoneDataSet : user.getPhones()) {
                phoneDataSet.setUser(user);
            }
            user.setPassword(passEncoder.encode(user.getPassword()));
            try {
                userDao.insertUser(user);
            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
