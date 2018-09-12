package thn.vn.web.miav.shop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import thn.vn.web.miav.shop.common.AdminControllerBase;
import thn.vn.web.miav.shop.models.entity.BarcodeLabel;

import java.util.List;

@Controller
@RequestMapping(value = "/admin/label")
public class ALabelView extends AdminControllerBase {
    @RequestMapping(value = "/{action}")
    String action(Model model, @PathVariable(name = "action")String action){
        return contentPage("forms/label",model);
    }
    @RequestMapping(value = {"","/"}, method = RequestMethod.GET)
    public String categoryMain(Model model) {
        List<BarcodeLabel> list = dataBaseService.getAll(BarcodeLabel.class);
        model.addAttribute("list", list);
        return contentPage("list/label", model);
    }
}
