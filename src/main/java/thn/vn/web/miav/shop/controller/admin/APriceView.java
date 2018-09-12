package thn.vn.web.miav.shop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import thn.vn.web.miav.shop.common.AdminControllerBase;
import thn.vn.web.miav.shop.models.entity.Category;
import thn.vn.web.miav.shop.models.entity.Price;
import thn.vn.web.miav.shop.models.entity.Product;
import thn.vn.web.miav.shop.utils.ParameterSql;
import thn.vn.web.miav.shop.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/admin/price")
public class APriceView extends AdminControllerBase{
    @RequestMapping(value = "/{action}", method = RequestMethod.GET)
    public String getAction(Model model,@PathVariable String action){
        switch (action){
            case "new":
                importCssHref("/webjars/dropzone/4.2.0/min/dropzone.min.css");
                importJavaScriptSrc("/webjars/dropzone/4.2.0/min/dropzone.min.js");
//                jsCustom("/mia-v/js/custom.js");
                return contentPage("forms/price", model);
                default:
                    return contentPage("list/price", model);
        }
    }
    @RequestMapping(value = "/{action}", method = RequestMethod.POST)
    public String postPrice(HttpServletRequest request, Model model, @PathVariable String action) {
        Map<String,String[]> map = request.getParameterMap();
        for (String itemId:map.keySet()){
            Price price = new Price();
            price.setProductId(Integer.parseInt(itemId));
            price.setPriceSell(Integer.parseInt(map.get(itemId)[0]));
            Product product = dataBaseService.find(Product.class ,"id=?",new ParameterSql[]{new ParameterSql(Integer.class,Integer.parseInt(itemId))});
            price.setProductName(product.getName());
            price.setDateUpdate(Utils.DateNow(Utils.DATE_FILE));
            price.setProductNameSecond(product.getNameSecond());
            dataBaseService.save(price);
        }
        return "redirect:/admin/price";
    }
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String categoryMain(Model model) {
        List<Price> list = dataBaseService.getAll(Price.class);
        model.addAttribute("list", list);
        return contentPage("list/price", model);
    }
    @RequestMapping(value = {"/del/{id}"}, method = RequestMethod.GET)
    public String ajaxDelete(Model model, @PathVariable int id) {
        dataBaseService.delete(Price.class, "productId=?", new ParameterSql[]{new ParameterSql(Integer.class, id)});
        List<Price> list = dataBaseService.getAll(Price.class);
        model.addAttribute("list", list);
        return "admin/fragments/list/price";
    }
}
