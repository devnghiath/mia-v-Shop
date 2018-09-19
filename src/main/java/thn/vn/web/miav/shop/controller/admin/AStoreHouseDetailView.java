package thn.vn.web.miav.shop.controller.admin;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
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
import thn.vn.web.miav.shop.models.entity.StoreHouseDetail;
import thn.vn.web.miav.shop.utils.ParameterSql;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/admin/storehouse_detail")
public class AStoreHouseDetailView extends AdminControllerBase {
    @ModelAttribute(value = "formData")
    public StoreHouseDetail newEntity() {
        return new StoreHouseDetail();
    }
    @RequestMapping(value = "/{action}", method = RequestMethod.GET)
    public String getAction(Model model, @PathVariable String action){
        if (action.equalsIgnoreCase("new")){

            return contentPage("forms/storehouse_detail", model);
        } else {
            return contentPage("", model);
        }
    }
    @RequestMapping(value = {"/tree/{rootId}"}, method = RequestMethod.GET)
    public String ajaxTreeList(Model model, @PathVariable int rootId) {
        ShopDBBuilder shopDBBuilder = new ShopDBBuilder();
        shopDBBuilder.select(StoreHouseDetail.class);
        shopDBBuilder.setClause("rootId=?");
        shopDBBuilder.setParameterSql(new ParameterSql[]{new ParameterSql(Integer.class,rootId)});
        List<StoreHouseDetail> list = shopDBBuilder.getList(shopDBService);
        model.addAttribute("list", list);
        return "admin/fragments/ajax/modal/popupStorehouseTree";
    }
    @RequestMapping(value = {"/ajax/item/{id}"}, method = RequestMethod.GET)
    public ResponseEntity<?> ajaxDetailItem(@PathVariable int id){
        StoreHouseDetail storeHouseDetail =(StoreHouseDetail)ShopDBBuilder.newInstance().select(StoreHouseDetail.class).setClause("id=?").setParameterSql(new ParameterSql[]{new ParameterSql(Integer.class, id)}).getEntity(shopDBService);
        return new ResponseEntity<>(storeHouseDetail, HttpStatus.OK);
    }
    @RequestMapping(value = "/{action}", method = RequestMethod.POST)
    public String postAction(Model model, @PathVariable String action, HttpServletRequest request, @ModelAttribute(value = "formData") StoreHouseDetail formData){
        formData.setDateUpdate(getDataUpdate());
        List<Integer> path = new ArrayList<>();
        List<String> pathName = new ArrayList<>();
        if (formData.getPath()==null){
            StoreHouseDetail storeHouseDetail = (StoreHouseDetail)ShopDBBuilder.newInstance().select(StoreHouseDetail.class).setClause("storeHouseId=?").setParameterSql(new ParameterSql[]{new ParameterSql(Integer.class, formData.getRootId())}).getEntity(shopDBService);
            storeHouseDetail.setIsParent(1);
            shopDBService.save(storeHouseDetail);
            path.add(storeHouseDetail.getId());
            formData.setPath(new Gson().toJson(path));
            pathName.add(storeHouseDetail.getName());
            pathName.add(formData.getName());
            formData.setPathName(new Gson().toJson(pathName));
        } else {
            StoreHouseDetail storeHouseDetail = (StoreHouseDetail)ShopDBBuilder.newInstance().select(StoreHouseDetail.class).setClause("id=?").setParameterSql(new ParameterSql[]{new ParameterSql(Integer.class, Integer.parseInt(formData.getPath()))}).getEntity(shopDBService);
            ArrayList<Integer> listPath= (ArrayList<Integer>) new Gson(). fromJson(storeHouseDetail.getPath(),
                    new TypeToken<ArrayList<Integer>>() {
                    }.getType());
            ArrayList<String> listPathName= (ArrayList<String>) new Gson(). fromJson(storeHouseDetail.getPathName(),
                    new TypeToken<ArrayList<String>>() {
                    }.getType());
            path.addAll(listPath);

            path.add(storeHouseDetail.getId());
            pathName.addAll(listPathName);
            pathName.add(formData.getName());
            formData.setPath(new Gson().toJson(path));
            formData.setPathName(new Gson().toJson(pathName));
            storeHouseDetail.setIsParent(1);
            formData.setRootId(storeHouseDetail.getId());
            shopDBService.save(storeHouseDetail);
        }
        shopDBService.save(formData);
//        formData.setPath("");
        return "redirect:/admin/storehouse";
    }
}
