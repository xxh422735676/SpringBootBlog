package com.xxh.blog.web.admin;

import com.xxh.blog.dbU.User;
import com.xxh.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String logingPage(){
        return "admin/blog-login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes, Model model){
        User user=userService.checkUser(username, password);
        if(user!=null){//通过
            user.setPassword(null);
            session.setAttribute("user",user);
            return "admin/blog-index";
        }else{//未通过
            attributes.addFlashAttribute("message","用户名或密码错误");
//            model.addAllAttributes("message","错误"); 重定向后拿不到
            return "redirect:/admin";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}
