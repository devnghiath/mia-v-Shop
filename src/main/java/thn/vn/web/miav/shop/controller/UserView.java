package thn.vn.web.miav.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import thn.vn.web.miav.shop.common.ControllerBase;
import thn.vn.web.miav.shop.component.JwtTokenProvider;
import thn.vn.web.miav.shop.models.entity.UserApp;
import thn.vn.web.miav.shop.services.DataBaseService;
import thn.vn.web.miav.shop.utils.ParameterSql;
import thn.vn.web.miav.shop.utils.Utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


@Controller
public class UserView extends ControllerBase {
    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider tokenProvider;
    @Autowired
    private DataBaseService dataBaseService;
    @ModelAttribute(value = "formData")
    public UserApp newEntity() {
        return new UserApp();
    }
    @Override
    protected List<String> initCssCommon() {
        List<String> cssList = new ArrayList<>();
        cssList.add("/webjars/bootstrap/3.3.7/css/bootstrap.min.css");
        cssList.add("/webjars/font-awesome/4.7.0/css/font-awesome.min.css");
        cssList.add("/webjars/animate.css/3.2.0/animate.min.css");
        cssList.add("/mia-v/css/mia-v.min.css");
        return cssList;
    }

    @Override
    protected List<String> initJavaScriptSrcCommon() {
        return null;
    }

    @Override
    protected String pathLayout() {
        return "";
    }
    @RequestMapping(value = "/sign", method = RequestMethod.GET)
    public String login(Model model, String error, String logout,@RequestParam(value = "redirect",required = false)String url,HttpServletRequest request,HttpServletResponse response) {
        if (error != null)
            model.addAttribute("errorMsg", "Your username and password are invalid.");

        if (logout != null)
            model.addAttribute("msg", "You have been logged out successfully.");
        model.addAttribute("cssList",initCssCommon());
        Cookie authenticationCookie = new Cookie("token", "");
        authenticationCookie.setPath("/");
        authenticationCookie.setHttpOnly(true);
        authenticationCookie.setSecure(request.isSecure());
        authenticationCookie.setMaxAge(0);
        response.addCookie(authenticationCookie);
        return "login";
    }
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout(Model model,HttpServletRequest request) {
        request.getSession(false).removeAttribute("Authorization");
        return "redirect:sign";
    }
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String getSignUp(Model model){
        model.addAttribute("cssList",initCssCommon());
        return "signup";
    }
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String postSignUp(@ModelAttribute(value = "formData") UserApp userApp,Model model){
        if(dataBaseService.checked(UserApp.class,"userName=? or email=?",new ParameterSql[]{new ParameterSql(String.class,userApp.getUserName()),new ParameterSql(String.class,userApp.getEmail())})){
            model.addAttribute("exist",true);
            return "signup";
        } else {

        }
        String pass = passwordEncoder.encode(userApp.getPassWord());
        userApp.setPassWord(pass);
        userApp.setPasswordConfirm(pass);
        userApp.setDateUpdate(Utils.DateNow(Utils.DATE_FILE));
        dataBaseService.save(userApp);
        return "redirect:sign";
    }

}
