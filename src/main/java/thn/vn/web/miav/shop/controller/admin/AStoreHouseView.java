package thn.vn.web.miav.shop.controller.admin;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import thn.vn.web.miav.shop.common.AdminControllerBase;
import thn.vn.web.miav.shop.dao.ShopDBBuilder;
import thn.vn.web.miav.shop.models.entity.StoreHouse;
import thn.vn.web.miav.shop.models.entity.StoreHouseDetail;
import thn.vn.web.miav.shop.utils.ParameterSql;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/admin/storehouse")
public class AStoreHouseView extends AdminControllerBase {
    @ModelAttribute(value = "formData")
    public StoreHouse newEntity() {
        return new StoreHouse();
    }
    @RequestMapping(value = "/{action}", method = RequestMethod.GET)
    public String getAction(Model model, @PathVariable String action){
        if (action.equalsIgnoreCase("new")){

            return contentPage("forms/storehouse", model);
        } else {
            return contentPage("", model);
        }
    }
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("list", ShopDBBuilder.newInstance(shopDBService,StoreHouse.class).getList());
        return contentPage("list/storehouse",model);
    }
    @RequestMapping(value = "/{action}", method = RequestMethod.POST)
    public String postAction(Model model, @PathVariable String action,HttpServletRequest request, @ModelAttribute(value = "formData") StoreHouse formData){
        formData.setDateUpdate(getDataUpdate());
        shopDBService.save(formData);
        List<Integer> path = new ArrayList<>();
        StoreHouseDetail storeHouseDetail = new StoreHouseDetail();
        storeHouseDetail.setPath(new Gson().toJson(path));
        storeHouseDetail.setRootId(0);
        storeHouseDetail.setStoreHouseId(formData.getId());
        storeHouseDetail.setName(formData.getName());
        storeHouseDetail.setDateUpdate(formData.getDateUpdate());
        shopDBService.save(storeHouseDetail);
        return "redirect:/admin/storehouse";
//        return contentPage("list/storehouse",model);
    }
    @RequestMapping(value = {"/popup"}, method = RequestMethod.GET)
    public String ajaxList(Model model) {
        List<StoreHouse> list = ShopDBBuilder.newInstance(shopDBService).select(StoreHouse.class).getList();
        model.addAttribute("list", list);
        return "admin/fragments/ajax/modal/popupStorehouse";
    }

    @RequestMapping(value = {"/ajax/item/{id}"}, method = RequestMethod.GET)
    public ResponseEntity<?> ajaxDetailItem(@PathVariable int id){
        ShopDBBuilder shopDBBuilder = new ShopDBBuilder(shopDBService);
        shopDBBuilder.select(StoreHouse.class);
        shopDBBuilder.setClause("id=?");
        shopDBBuilder.setParameterSql(new ParameterSql[]{new ParameterSql(Integer.class, id)});
        StoreHouse storeHouse = (StoreHouse)shopDBBuilder.getEntity();
        StoreHouseDetail storeHouseDetail = (StoreHouseDetail)ShopDBBuilder.newInstance(shopDBService).select(StoreHouseDetail.class).setClause("storeHouseId=?").setParameterSql(new ParameterSql[]{new ParameterSql(Integer.class, id)}).getEntity();
        HashMap<String,Object> data = new HashMap<>();
        data.put("storehouse",storeHouse);
        data.put("storeHouseDetail",storeHouseDetail);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
