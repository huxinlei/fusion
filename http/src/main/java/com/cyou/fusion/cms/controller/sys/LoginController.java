package com.cyou.fusion.cms.controller.sys;

import com.cyou.fusion.cms.domain.sys.Account;
import com.cyou.fusion.cms.service.sys.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * LoginController
 * <p>
 * Created by zhanglei_js on 2017/5/17.
 */
@Controller
public class LoginController {

    private final static String ATTRIBUTE_USER = "attribute_user";

    @Autowired
    private AccountService accountService;

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String forwardLogin(HttpServletRequest request) {
        if (request.getSession(true).getAttribute(ATTRIBUTE_USER) != null) {
            return "redirect:main";
        }
        return "sys/login";
    }

    @RequestMapping(value = "doLogin", method = RequestMethod.POST)
    public String login(HttpServletRequest request, @RequestParam String email, @RequestParam String password, RedirectAttributes redirect) {
        Account account = accountService.login(email, password);
        if (account == null) {

            redirect.addFlashAttribute("error", "Incorrect username or password.");
            return "redirect:login";
        } else {
            request.getSession(true).setAttribute(ATTRIBUTE_USER, account);
            return "redirect:main";
        }
    }
}
