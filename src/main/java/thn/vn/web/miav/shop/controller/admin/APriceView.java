package thn.vn.web.miav.shop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import thn.vn.web.miav.shop.common.AdminControllerBase;
import thn.vn.web.miav.shop.dao.ShopDBBuilder;

import thn.vn.web.miav.shop.models.entity.Product;
import thn.vn.web.miav.shop.utils.ParameterSql;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
        Map<Integer,Integer> priceRoot = new HashMap<>();
        Map<Integer,Integer> priceSell = new HashMap<>();
        Map<Integer,Integer> ListId = new HashMap<>();
        for (String itemId:map.keySet()){
            if (itemId.startsWith("priceRoot_")){
                String[]array_str = itemId.split("_");
                priceRoot.put(Integer.parseInt(array_str[1]),Integer.parseInt(map.get(itemId)[0]));
                ListId.put(Integer.parseInt(array_str[1]),Integer.parseInt(array_str[1]));
            }
            if (itemId.startsWith("priceSell_")){
                String[]array_str = itemId.split("_");
                priceSell.put(Integer.parseInt(array_str[1]),Integer.parseInt(map.get(itemId)[0]));
                ListId.put(Integer.parseInt(array_str[1]),Integer.parseInt(array_str[1]));
            }

        }
        for (int itemId:ListId.keySet()){
            Product product = (Product)ShopDBBuilder.newInstance(shopDBService,Product.class,"id=?",new ParameterSql[]{new ParameterSql(Integer.class,itemId)}).getEntity();
            product.setPriceRoot(priceRoot.get(itemId));
            product.setPriceSell(priceSell.get(itemId));
            product.setDateUpdate(getDataUpdate());
            shopDBService.save(product);
        }
        return "redirect:/admin/price";
    }
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String categoryMain(Model model) {
        List<Product> list = ShopDBBuilder.newInstance(shopDBService,Product.class).getList();
        model.addAttribute("list", list);
        return contentPage("list/price", model);
    }

}
