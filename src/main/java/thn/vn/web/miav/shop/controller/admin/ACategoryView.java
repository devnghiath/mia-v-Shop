package thn.vn.web.miav.shop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import thn.vn.web.miav.shop.common.AdminControllerBase;
import thn.vn.web.miav.shop.models.entity.Category;
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
        List<Category> list = dataBaseService.getAll(Category.class);
        model.addAttribute("list", list);
        return contentPage("list/category", model);
    }

    @RequestMapping(value = {"/detail/{id}"}, method = RequestMethod.GET)
    public String ajaxDetail(Model model, @PathVariable int id) {
        Category category = dataBaseService.find(Category.class, "id=?", new ParameterSql[]{new ParameterSql(Integer.class, id)});
        model.addAttribute("category", category);
        return "admin/fragments/ajax/modal/category_update";
    }


    @RequestMapping(value = {"/popup"}, method = RequestMethod.GET)
    public String ajaxList(Model model) {
        List<Category> list = dataBaseService.getAll(Category.class);
        model.addAttribute("list", list);
        return "admin/fragments/ajax/modal/popupCategory";
    }
    @RequestMapping(value = "/{action}", method = RequestMethod.GET)
    public String settingCategory(Model model, @PathVariable String action) {

        if (action.equalsIgnoreCase("new")) {
            return contentPage("forms/category", model);
        } else if (action.equalsIgnoreCase("list")) {
            List<Category> list = dataBaseService.getAll(Category.class);
            model.addAttribute("list", list);
            return contentPage("list/category", model);
        } else {
            return contentPage("list/category", model);
        }
    }
    @RequestMapping(value = {"/del/{id}"}, method = RequestMethod.GET)
    public String ajaxDelete(Model model, @PathVariable int id) {
        dataBaseService.delete(Category.class, "id=?", new ParameterSql[]{new ParameterSql(Integer.class, id)});
        List<Category> list = dataBaseService.getAll(Category.class);
        model.addAttribute("list", list);
        return "admin/fragments/list/category";
    }
    @RequestMapping(value = "/{action}/{id}", method = RequestMethod.GET)
    public String viewUpdate(Model model, @PathVariable String action,@PathVariable int id) {

        if (action.equalsIgnoreCase("update")) {
            Category category = dataBaseService.find(Category.class,"id=?",new ParameterSql[]{new ParameterSql(Integer.class,id)});
            model.addAttribute("category",category);
            return contentPage("forms/category", model);
        } else {
            List<Category> list = dataBaseService.getAll(Category.class);
            model.addAttribute("list", list);
            return contentPage("list/category", model);
        }
    }
    @RequestMapping(value = "/{action}", method = RequestMethod.POST)
    public String updateCategory(@ModelAttribute(value = "formData") Category categoryForm, Model model, @PathVariable String action) {
        categoryForm.setDateUpdate(Utils.DateNow(Utils.DATE_FILE));
        dataBaseService.save(categoryForm);
        return "redirect:/admin/category";
    }
}
