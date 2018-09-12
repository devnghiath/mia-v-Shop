package thn.vn.web.miav.shop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import thn.vn.web.miav.shop.common.AdminControllerBase;
@Controller
public class AdminView extends AdminControllerBase {
    @RequestMapping(value = {"/admin","/admin/"},method = RequestMethod.GET)
    public String index(Model model){
        return contentPage("home",model);
    }
}
