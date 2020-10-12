package pro.kuli4.otus.java.hw14.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;


@Controller
public class UserController {

    @GetMapping({"/"})
    public String userListView(Model model) {
        model.addAttribute("hasMessage", true);
        model.addAttribute("message","There isn't authenticate system. Please click \"Sing in\" for enter!");
        return "index.html";
    }

    @PostMapping("/login/")
    public RedirectView userSave() {
        return new RedirectView("/private/users.html", true);
    }

}
