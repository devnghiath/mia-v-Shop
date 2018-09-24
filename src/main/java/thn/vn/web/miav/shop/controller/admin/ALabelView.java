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
import java.util.Random;

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

        list = ShopDBBuilder.newInstance(shopDBService,Product.class).getList();
        int size = list.size();
        for (int i = 0; i <40;i++ ){
            if (list.size()==40){
                break;
            }
            Random random = new Random();

            Product product = list.get(random.nextInt(size));
            list.add(product);

        }
        model.addAttribute("list", list);
        return "admin/fragments/list/printLabel";
    }
}
