package thn.vn.web.miav.shop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import thn.vn.web.miav.shop.common.AdminControllerBase;
import thn.vn.web.miav.shop.models.entity.*;
import thn.vn.web.miav.shop.utils.ParameterSql;
import thn.vn.web.miav.shop.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/admin/import")
public class AImportView extends AdminControllerBase{
    @RequestMapping(value = "/{action}", method = RequestMethod.GET)
    public String getAction(Model model,@PathVariable String action){
        switch (action){
            case "new":
                importCssHref("/webjars/dropzone/4.2.0/min/dropzone.min.css");
                importJavaScriptSrc("/webjars/dropzone/4.2.0/min/dropzone.min.js");
//                jsCustom("/mia-v/js/custom.js");
                return contentPage("forms/import", model);
                default:
                    return contentPage("list/import", model);
        }
    }
    @RequestMapping(value = "/{action}", method = RequestMethod.POST)
    public String postImport(HttpServletRequest request, Model model, @PathVariable String action) {
        StringBuilder prefix = new StringBuilder();
        UserApp userApp = getUserApp(request);
        prefix.append(userApp.getId());
        prefix.append(Utils.DateNow(Utils.DATE_FILE));
        prefix.append(0);
        String id = generateId(3,prefix.toString());
        ImportHeader importHeader = new ImportHeader();
        importHeader.setId(id);
        importHeader.setDateImport(Utils.DateNow(Utils.DATE_YYYY_MM_DD_JP));
        importHeader.setDateUpdate(Utils.DateNow(Utils.DATE_FILE));
        importHeader.setUserId(userApp.getId());
        Map<String,Integer> amountIndex = new HashMap<>();
        Map<String,String[]> map = request.getParameterMap();
        String[] listId = request.getParameterValues("productId");
        Map<Integer,List<Import>> listItem = new HashMap<>();
        int index = 0;
        for (String productId:listId){
            Import anImport = new Import();
            anImport.setProductId(Integer.parseInt(productId));
            Product product = dataBaseService.find(Product.class ,"id=?",new ParameterSql[]{new ParameterSql(Integer.class,Integer.parseInt(productId))});
            anImport.setProductName(product.getName());
            anImport.setDateUpdate(Utils.DateNow(Utils.DATE_FILE));
            anImport.setDateImport(Utils.DateNow(Utils.DATE_YYYY_MM_DD_JP));
            anImport.setProductNameSecond(product.getNameSecond());
            if (amountIndex.containsKey(productId)){
                int i = amountIndex.get(productId)+1;
                anImport.setAmount(Integer.parseInt(map.get("amount_"+productId)[i]));
                anImport.setPrice(Integer.parseInt(map.get("money_"+productId)[i]));
                amountIndex.put(productId,i);
            } else {
                amountIndex.put(productId,0);
                anImport.setAmount(Integer.parseInt(map.get("amount_"+productId)[0]));
                anImport.setPrice(Integer.parseInt(map.get("money_"+productId)[0]));
            }
            anImport.setTotalMoney(anImport.getPrice()*anImport.getAmount());
            List<Import> list = new ArrayList<>();
//            list.add(anImport);

            if (listItem.containsKey(anImport.getProductId())){
                boolean isHave = false;
                list.addAll(listItem.get(anImport.getProductId()));
                for (Import obj:list){
                    if (obj.getPrice() == anImport.getPrice()){
                        obj.setAmount(anImport.getAmount()+obj.getAmount());
                        obj.setTotalMoney(obj.getPrice()*obj.getAmount());
                        isHave = true;
                        break;
                    }

                }
                if (!isHave) {
                    list.add(anImport);
                }
            } else {
                list.add(anImport);
            }
            listItem.put(anImport.getProductId(),list);
            index++;
        }
        int total = 0;
        int no = 1;
        for (int productId:listItem.keySet()){

            int totalImport = 0;
            for (Import obj:listItem.get(productId)){
                obj.setId(importHeader.getId());
                obj.setNo(no);
                total+= obj.getTotalMoney();
                no++;
                totalImport+=obj.getAmount();
                dataBaseService.save(obj);
                Inventory inventory = dataBaseService.find(Inventory.class,"productId=?",new ParameterSql[]{new ParameterSql(Integer.class,productId)});
                if (inventory!=null){
                    inventory.setAmountImport(inventory.getAmountImport()+totalImport);
                } else {
                    inventory = new Inventory();
                    inventory.setProductId(productId);
                    inventory.setAmountImport(totalImport);
                }
                Product product = dataBaseService.find(Product.class ,"id=?",new ParameterSql[]{new ParameterSql(Integer.class,productId)});
                inventory.setProductName(product.getName());
                inventory.setProductNameSecond(product.getNameSecond());
                inventory.setAmountInventory(inventory.getAmountImport()+inventory.getAmountFirst()-inventory.getAmountExport());
                dataBaseService.save(inventory);
            }
            }

        importHeader.setTotal(total);
        dataBaseService.save(importHeader);

        return "redirect:/admin/import";
    }
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String importMain(Model model) {
        List<ImportHeader> list = dataBaseService.getAll(ImportHeader.class);
        model.addAttribute("list", list);
        return contentPage("list/import", model);
    }
    @RequestMapping(value = {"/del/{id}"}, method = RequestMethod.GET)
    public String ajaxDelete(Model model, @PathVariable int id) {
        dataBaseService.delete(Import.class, "productId=?", new ParameterSql[]{new ParameterSql(Integer.class, id)});
        List<Import> list = dataBaseService.getAll(Import.class);
        model.addAttribute("list", list);
        return "admin/fragments/list/import";
    }
    @RequestMapping(value = {"/detail/{id}"}, method = RequestMethod.GET)
    public String detail(Model model,@PathVariable String id) {
        List<Import> list = dataBaseService.getAll(Import.class,"id=?",new ParameterSql[]{new ParameterSql(String.class,id)});
        model.addAttribute("list", list);
        ImportHeader importHeader = dataBaseService.find(ImportHeader.class,"id=?",new ParameterSql[]{new ParameterSql(String.class,id)});
        model.addAttribute("total", importHeader.getTotal());
        return "admin/fragments/ajax/modal/popupImportDetail";
    }
}
