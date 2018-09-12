package thn.vn.web.miav.shop.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import thn.vn.web.miav.shop.common.ClientControllerBase;
import thn.vn.web.miav.shop.models.entity.UserApp;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexView extends ClientControllerBase {
    @RequestMapping(value = {""})
    public String index(Model model){
        return viewName("index");
    }
    @RequestMapping(value = {"/html"})
    public String html(Model model){
        return viewName("html");
    }
    @RequestMapping(value = {"/403"})
    public String accessDenied(Model model){
        return "403";
    }

}
