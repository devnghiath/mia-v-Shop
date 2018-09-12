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
import thn.vn.web.miav.shop.models.entity.Manufacturer;
import thn.vn.web.miav.shop.models.entity.Product;
import thn.vn.web.miav.shop.utils.ParameterSql;
import thn.vn.web.miav.shop.utils.Utils;

import java.util.List;

@Controller
@RequestMapping(value = "/admin/manufacturer")
public class AManufacturerView extends AdminControllerBase {
    @ModelAttribute(value = "formData")
    public Manufacturer newEntity() {
        return new Manufacturer();
    }

    @RequestMapping(value = {"", "/manufacturer/"}, method = RequestMethod.GET)
    public String list(Model model) {
        List<Manufacturer> list = dataBaseService.getAll(Manufacturer.class);
        model.addAttribute("list", list);
        return contentPage("list/manufacturer", model);
    }

    @RequestMapping(value = "/{action}", method = RequestMethod.GET)
    public String settingManufacturer(Model model, @PathVariable String action) {

        if (action.equalsIgnoreCase("new")) {
            return contentPage("forms/manufacturer", model);
        } else if (action.equalsIgnoreCase("list")) {
            List<Brand> list = dataBaseService.getAll(Brand.class);
            model.addAttribute("list", list);
            return contentPage("list/manufacturer", model);
        } else {
            return contentPage("list/manufacturer", model);
        }

    }

    @RequestMapping(value = "/{action}", method = RequestMethod.POST)
    public String updateManufacturer(@ModelAttribute(value = "formData") Manufacturer formData, Model model, @PathVariable String action) {
        formData.setDateUpdate(Utils.DateNow(Utils.DATE_FILE));
        dataBaseService.save(formData);
        return "redirect:/admin/manufacturer";
    }
    @RequestMapping(value = {"/del/{id}"}, method = RequestMethod.GET)
    public String ajaxDelete(Model model, @PathVariable int id) {
        dataBaseService.delete(Manufacturer.class, "id=?", new ParameterSql[]{new ParameterSql(Integer.class, id)});
        List<Manufacturer> list = dataBaseService.getAll(Manufacturer.class);
        model.addAttribute("list", list);
        return "admin/fragments/list/manufacturer";
    }
    @RequestMapping(value = "/{action}/{id}", method = RequestMethod.GET)
    public String viewUpdate(Model model, @PathVariable String action,@PathVariable int id) {

        if (action.equalsIgnoreCase("update")) {
            Manufacturer manufacturer = dataBaseService.find(Manufacturer.class,"id=?",new ParameterSql[]{new ParameterSql(Integer.class,id)});
            model.addAttribute("manufacturer",manufacturer);
            return contentPage("forms/manufacturer", model);
        } else {
            List<Category> list = dataBaseService.getAll(Category.class);
            model.addAttribute("list", list);
            return contentPage("list/manufacturer", model);
        }
    }
    @RequestMapping(value = {"/popup"}, method = RequestMethod.GET)
    public String ajaxList(Model model) {
        List<Manufacturer> list = dataBaseService.getAll(Manufacturer.class);
        model.addAttribute("list", list);
        return "admin/fragments/ajax/modal/popupManufacturer";
    }

}
