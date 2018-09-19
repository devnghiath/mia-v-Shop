package thn.vn.web.miav.shop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import thn.vn.web.miav.shop.common.AdminControllerBase;
import thn.vn.web.miav.shop.dao.ShopDBBuilder;
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
        List<Manufacturer> list = ShopDBBuilder.newInstance(Manufacturer.class).getList(shopDBService);
        model.addAttribute("list", list);
        return contentPage("list/manufacturer", model);
    }

    @RequestMapping(value = "/{action}", method = RequestMethod.GET)
    public String settingManufacturer(Model model, @PathVariable String action) {

        if (action.equalsIgnoreCase("new")) {
            return contentPage("forms/manufacturer", model);
        } else if (action.equalsIgnoreCase("list")) {
            List<Brand> list = ShopDBBuilder.newInstance(Manufacturer.class).getList(shopDBService);
            model.addAttribute("list", list);
            return contentPage("list/manufacturer", model);
        } else {
            return contentPage("list/manufacturer", model);
        }

    }

    @RequestMapping(value = "/{action}", method = RequestMethod.POST)
    public String updateManufacturer(@ModelAttribute(value = "formData") Manufacturer formData, Model model, @PathVariable String action) {
        formData.setDateUpdate(getDataUpdate());
        shopDBService.save(formData);
        return "redirect:/admin/manufacturer";
    }
    @RequestMapping(value = {"/del/{id}"}, method = RequestMethod.GET)
    public String ajaxDelete(Model model, @PathVariable int id) {
        ShopDBBuilder.newInstance(Manufacturer.class,"id=?",new ParameterSql[]{new ParameterSql(Integer.class, id)}).delete(shopDBService);
        List<Manufacturer> list = ShopDBBuilder.newInstance(Manufacturer.class).getList(shopDBService);
        model.addAttribute("list", list);
        return "admin/fragments/list/manufacturer";
    }
    @RequestMapping(value = "/{action}/{id}", method = RequestMethod.GET)
    public String viewUpdate(Model model, @PathVariable String action,@PathVariable int id) {

        if (action.equalsIgnoreCase("update")) {
            Manufacturer manufacturer = (Manufacturer)ShopDBBuilder.newInstance(Manufacturer.class,"id=?",new ParameterSql[]{new ParameterSql(Integer.class,id)}).getEntity(shopDBService);
            model.addAttribute("manufacturer",manufacturer);
            return contentPage("forms/manufacturer", model);
        } else {
            List<Category> list = ShopDBBuilder.newInstance(Category.class).getList(shopDBService);
            model.addAttribute("list", list);
            return contentPage("list/manufacturer", model);
        }
    }
    @RequestMapping(value = {"/popup"}, method = RequestMethod.GET)
    public String ajaxList(Model model) {
        List<Manufacturer> list = ShopDBBuilder.newInstance(Manufacturer.class).getList(shopDBService);
        model.addAttribute("list", list);
        return "admin/fragments/ajax/modal/popupManufacturer";
    }

}
