package thn.vn.web.miav.shop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import thn.vn.web.miav.shop.common.AdminControllerBase;
import thn.vn.web.miav.shop.models.entity.Brand;
import thn.vn.web.miav.shop.models.entity.Category;
import thn.vn.web.miav.shop.utils.ParameterSql;
import thn.vn.web.miav.shop.utils.Utils;

import java.util.List;

@Controller
@RequestMapping(value = "/admin/brand")
public class ABrandsView extends AdminControllerBase {
    @ModelAttribute(value = "formData")
    public Brand newEntity() {
        return new Brand();
    }
    @RequestMapping(value = {"","/brand/"},method = RequestMethod.GET)
    public String list(Model model){
        List<Brand> list = dataBaseService.getAll(Brand.class);
        model.addAttribute("list",list);
        return contentPage("list/brand",model);
    }
    @RequestMapping(value = "/{action}",method = RequestMethod.GET)
    public String settingCategory(Model model,@PathVariable String action){
        if (action.equalsIgnoreCase("new")){
            return contentPage("forms/brand",model);
        } else if (action.equalsIgnoreCase("list")){
            List<Brand> list = dataBaseService.getAll(Brand.class);
            model.addAttribute("list",list);
            return contentPage("brand",model);
        } else {
            return contentPage("brand",model);
        }

    }
    @RequestMapping(value = "/{action}/{id}", method = RequestMethod.GET)
    public String viewUpdate(Model model, @PathVariable String action,@PathVariable int id) {

        if (action.equalsIgnoreCase("update")) {
            Brand brand = dataBaseService.find(Brand.class,"id=?",new ParameterSql[]{new ParameterSql(Integer.class,id)});
            model.addAttribute("brand",brand);
            return contentPage("forms/brand", model);
        } else {
            List<Brand> list = dataBaseService.getAll(Brand.class);
            model.addAttribute("list", list);
            return contentPage("list/brand", model);
        }
    }
    @RequestMapping(value = {"/del/{id}"}, method = RequestMethod.GET)
    public String ajaxDelete(Model model, @PathVariable int id) {
        dataBaseService.delete(Brand.class, "id=?", new ParameterSql[]{new ParameterSql(Integer.class, id)});
        List<Brand> list = dataBaseService.getAll(Brand.class);
        model.addAttribute("list", list);
        return "admin/fragments/list/brand";
    }
    @RequestMapping(value = "/{action}",method = RequestMethod.POST)
    public String updateCategory(@ModelAttribute(value = "formData") Brand brandForm, Model model, @PathVariable String action){
        brandForm.setDateUpdate(Utils.DateNow(Utils.DATE_FILE));
        dataBaseService.save(brandForm);
        return "redirect:/admin/brand";
    }
    @RequestMapping(value = {"/popup"}, method = RequestMethod.GET)
    public String ajaxList(Model model) {
        List<Brand> list = dataBaseService.getAll(Brand.class);
        model.addAttribute("list", list);
        return "admin/fragments/ajax/modal/popupBrand";
    }

}