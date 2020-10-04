package pro.kuli4.otus.java.hw12.server.servlets;

import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;
import pro.kuli4.otus.java.hw12.dao.UserDao;
import pro.kuli4.otus.java.hw12.dto.UserJsonDto;
import pro.kuli4.otus.java.hw12.entities.PhoneDataSet;
import pro.kuli4.otus.java.hw12.entities.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class UsersApiServlet extends HttpServlet {

    private static final int ID_PATH_PARAM_POSITION = 1;

    private final UserJsonDto userJsonDto;
    private final UserDao userDao;
    private final Gson gson;


    public UsersApiServlet(UserJsonDto userJsonDto, UserDao userDao, Gson gson) {
        this.userJsonDto = userJsonDto;
        this.userDao = userDao;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getOutputStream().print(userJsonDto.getAllUsers());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            User user = gson.fromJson(request.getReader(), User.class);
            for (PhoneDataSet phoneDataSet : user.getPhones()) {
                phoneDataSet.setUser(user);
            }
            user.setPassword(DigestUtils.md5Hex(user.getPassword()));
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
