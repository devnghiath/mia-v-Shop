package thn.vn.web.miav.shop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import thn.vn.web.miav.shop.common.AdminControllerBase;
import thn.vn.web.miav.shop.dao.ShopDBBuilder;
import thn.vn.web.miav.shop.models.entity.Attributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping(value = "/admin/attributes")
public class AAttributesView extends AdminControllerBase {
    @ModelAttribute(value = "formData")
    public Attributes newEntity() {
        return new Attributes();
    }
    @RequestMapping(value = {"","/brand/"},method = RequestMethod.GET)
    public String list(Model model){
        List<Attributes> list = ShopDBBuilder.newInstance(shopDBService,Attributes.class).getList();
        model.addAttribute("list",list);
        return contentPage("list/attributes",model);
    }
    @RequestMapping(value = "/{action}", method = RequestMethod.GET)
    public String getAction(Model model, @PathVariable String action) {
        return contentPage("forms/attributes",model);
    }
    @RequestMapping(value = "/{action}", method = RequestMethod.POST)
    public String postAction(HttpServletRequest request, @ModelAttribute(value = "formData") Attributes formData, Model model, @PathVariable String action) {
        shopDBService.save(formData);
        return "redirect:/admin/attributes";
    }
}
