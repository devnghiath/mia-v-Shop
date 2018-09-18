package thn.vn.web.miav.shop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import thn.vn.web.miav.shop.common.AdminControllerBase;
import thn.vn.web.miav.shop.dao.ShopDBBuilder;
import thn.vn.web.miav.shop.models.entity.BarcodeLabel;
import thn.vn.web.miav.shop.models.entity.Product;
import thn.vn.web.miav.shop.utils.ParameterSql;

import java.util.ArrayList;
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
        List<Product> list = new ArrayList<>();
        Product product = (Product)ShopDBBuilder.newInstance().select(Product.class).setClause("id=?").setParameterSql(new ParameterSql[]{new ParameterSql(Integer.class,2)}).getEntity(shopDBService);
        for (int i = 0; i <44;i++ ){
            list.add(product);
        }
        model.addAttribute("list", list);
        return "admin/fragments/list/printLabel";
    }
}
