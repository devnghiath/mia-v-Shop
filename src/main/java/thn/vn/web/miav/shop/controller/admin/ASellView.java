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
@RequestMapping(value = "/admin/sell")
public class ASellView extends AdminControllerBase {
    @RequestMapping(value = "/{action}", method = RequestMethod.GET)
    public String getNew(Model model,@PathVariable String action){
        if (action.equalsIgnoreCase("new")){
            return contentPage("forms/sell", model);
        } else {
            return contentPage("list/sell", model);
        }
    }
    @RequestMapping(value = "/{action}", method = RequestMethod.POST)
    public String postSell(HttpServletRequest request, Model model, @PathVariable String action) {
        StringBuilder prefix = new StringBuilder();
        UserApp userApp = getUserApp(request);
        prefix.append(userApp.getId());
        prefix.append(Utils.DateNow(Utils.DATE_FILE));
        prefix.append(0);
        String id = generateId(3,prefix.toString());
        ExportHeader exportHeader = new ExportHeader();
        exportHeader.setId(id);
        exportHeader.setDateExport(Utils.DateNow(Utils.DATE_YYYY_MM_DD_JP));
        exportHeader.setDateUpdate(Utils.DateNow(Utils.DATE_FILE));
        exportHeader.setUserId(userApp.getId());
        // detail
        Map<String,String[]> map = request.getParameterMap();
        String[] listId = request.getParameterValues("productId");
        Map<Integer,List<Export>> listItem = new HashMap<>();
        Map<String,Integer> amountIndex = new HashMap<>();

        int index = 0;
        for (String productId:listId){
            Export export = new Export();
            export.setProductId(Integer.parseInt(productId));
            Product product = dataBaseService.find(Product.class ,"id=?",new ParameterSql[]{new ParameterSql(Integer.class,Integer.parseInt(productId))});
            export.setProductName(product.getName());
            export.setDateUpdate(exportHeader.getDateUpdate());
            export.setDateExport(exportHeader.getDateExport());
            export.setProductNameSecond(product.getNameSecond());
            if (amountIndex.containsKey(productId)){
                int i = amountIndex.get(productId)+1;
                export.setAmount(Integer.parseInt(map.get("amount_"+productId)[i]));
                export.setPrice(Integer.parseInt(map.get("money_"+productId)[i]));
                amountIndex.put(productId,i);
            } else {
                amountIndex.put(productId,0);
                export.setAmount(Integer.parseInt(map.get("amount_"+productId)[0]));
                export.setPrice(Integer.parseInt(map.get("money_"+productId)[0]));
            }

            export.setTotalMoney(export.getPrice()*export.getAmount());
            List<Export> list = new ArrayList<>();
            if (listItem.containsKey(export.getProductId())){
                boolean isHave = false;
                list.addAll(listItem.get(export.getProductId()));
                for (Export obj:list){
                    if (obj.getPrice() == export.getPrice()){
                        obj.setAmount(export.getAmount()+obj.getAmount());
                        obj.setTotalMoney(obj.getPrice()*obj.getAmount());
                        isHave = true;
                        break;
                    }

                }
                if (!isHave) {
                    list.add(export);
                }
            } else {
                list.add(export);
            }
            listItem.put(export.getProductId(),list);
            index++;
        }
        int total = 0;
        int no = 1;
        for (int productId:listItem.keySet()){

            int totalExport = 0;
            for (Export obj:listItem.get(productId)){
                obj.setId(exportHeader.getId());
                obj.setNo(no);
                total+= obj.getTotalMoney();
                totalExport+=obj.getAmount();
                dataBaseService.save(obj);
                no++;
                Inventory inventory = dataBaseService.find(Inventory.class,"productId=?",new ParameterSql[]{new ParameterSql(Integer.class,obj.getProductId())});
                if (inventory!=null){
                    inventory.setAmountExport(inventory.getAmountExport()+totalExport);
                } else {
                    inventory = new Inventory();
                    inventory.setProductId(productId);
                    inventory.setAmountExport(totalExport);
                }
                Product product = dataBaseService.find(Product.class ,"id=?",new ParameterSql[]{new ParameterSql(Integer.class,productId)});
                inventory.setProductName(product.getName());
                inventory.setProductNameSecond(product.getNameSecond());
                inventory.setAmountInventory(inventory.getAmountImport()+inventory.getAmountFirst()-inventory.getAmountExport());
                dataBaseService.save(inventory);
            }

        }
        //
        exportHeader.setTotal(total);
        dataBaseService.save(exportHeader);
        return "redirect:/admin/sell";
    }
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String home(Model model) {
        List<ExportHeader> list = dataBaseService.getAll(ExportHeader.class);
        model.addAttribute("list", list);
        return contentPage("list/sell", model);
    }
    @RequestMapping(value = {"/detail/{id}"}, method = RequestMethod.GET)
    public String detail(Model model,@PathVariable String id) {
        List<Export> list = dataBaseService.getAll(Export.class,"id=?",new ParameterSql[]{new ParameterSql(String.class,id)});
        model.addAttribute("list", list);
        ExportHeader exportHeader = dataBaseService.find(ExportHeader.class,"id=?",new ParameterSql[]{new ParameterSql(String.class,id)});
        model.addAttribute("total", exportHeader.getTotal());
        return "admin/fragments/ajax/modal/popupSellDetail";
    }
//    @RequestMapping(value = {"/popup"}, method = RequestMethod.GET)
//    public String ajaxDetail(Model model) {
//        List<Export> list = dataBaseService.getAll(Export.class);
//        model.addAttribute("list", list);
//        return "admin/fragments/ajax/modal/popupSellDetail";
//    }
}
