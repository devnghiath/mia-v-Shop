package thn.vn.web.miav.shop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import thn.vn.web.miav.shop.common.AdminControllerBase;
import thn.vn.web.miav.shop.dao.ShopDBBuilder;
import thn.vn.web.miav.shop.models.entity.Category;
import thn.vn.web.miav.shop.services.ShopDBService;
import thn.vn.web.miav.shop.utils.ParameterSql;
import thn.vn.web.miav.shop.utils.Utils;

import java.util.List;

@Controller
@RequestMapping(value = "/admin/category")
public class ACategoryView extends AdminControllerBase {
    @ModelAttribute(value = "formData")
    public Category newEntity() {
        return new Category();
    }

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String categoryMain(Model model) {
        List<Category> list = ShopDBBuilder.newInstance(Category.class).getList(shopDBService);
        model.addAttribute("list", list);
        return contentPage("list/category", model);
    }

    @RequestMapping(value = {"/detail/{id}"}, method = RequestMethod.GET)
    public String ajaxDetail(Model model, @PathVariable int id) {
        Category category =(Category) ShopDBBuilder.newInstance(Category.class,"id=?",new ParameterSql[]{new ParameterSql(Integer.class, id)}).getEntity(shopDBService);
        model.addAttribute("category", category);
        return "admin/fragments/ajax/modal/category_update";
    }


    @RequestMapping(value = {"/popup"}, method = RequestMethod.GET)
    public String ajaxList(Model model) {
        List<Category> list = ShopDBBuilder.newInstance(Category.class).getList(shopDBService);
        model.addAttribute("list", list);
        return "admin/fragments/ajax/modal/popupCategory";
    }
    @RequestMapping(value = "/{action}", method = RequestMethod.GET)
    public String settingCategory(Model model, @PathVariable String action) {

        if (action.equalsIgnoreCase("new")) {
            return contentPage("forms/category", model);
        } else if (action.equalsIgnoreCase("list")) {
            List<Category> list = ShopDBBuilder.newInstance(Category.class).getList(shopDBService);
            model.addAttribute("list", list);
            return contentPage("list/category", model);
        } else {
            return contentPage("list/category", model);
        }
    }
    @RequestMapping(value = {"/del/{id}"}, method = RequestMethod.GET)
    public String ajaxDelete(Model model, @PathVariable int id) {
        ShopDBBuilder.newInstance(Category.class,"id=?",new ParameterSql[]{new ParameterSql(Integer.class, id)}).delete(shopDBService);
        List<Category> list =  ShopDBBuilder.newInstance(Category.class).getList(shopDBService);
        model.addAttribute("list", list);
        return "admin/fragments/list/category";
    }
    @RequestMapping(value = "/{action}/{id}", method = RequestMethod.GET)
    public String viewUpdate(Model model, @PathVariable String action,@PathVariable int id) {

        if (action.equalsIgnoreCase("update")) {
            Category category =(Category) ShopDBBuilder.newInstance(Category.class,"id=?",new ParameterSql[]{new ParameterSql(Integer.class, id)}).getEntity(shopDBService);
            model.addAttribute("category",category);
            return contentPage("forms/category", model);
        } else {
            List<Category> list =  ShopDBBuilder.newInstance(Category.class).getList(shopDBService);
            model.addAttribute("list", list);
            return contentPage("list/category", model);
        }
    }
    @RequestMapping(value = "/{action}", method = RequestMethod.POST)
    public String updateCategory(@ModelAttribute(value = "formData") Category categoryForm, Model model, @PathVariable String action) {
        categoryForm.setDateUpdate(getDataUpdate());
        shopDBService.save(categoryForm);
        return "redirect:/admin/category";
    }
}
