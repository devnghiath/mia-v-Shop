package thn.vn.web.miav.shop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import thn.vn.web.miav.shop.common.AdminControllerBase;
import thn.vn.web.miav.shop.dao.ShopDBBuilder;
import thn.vn.web.miav.shop.models.entity.*;
import thn.vn.web.miav.shop.utils.ParameterSql;
import thn.vn.web.miav.shop.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/admin/invoice_import")
public class AInvoiceImportView extends AdminControllerBase {
    @RequestMapping(value = "/{action}", method = RequestMethod.GET)
    public String getAction(Model model, @PathVariable String action) {
        switch (action) {
            case "new":
                importCssHref("/webjars/dropzone/4.2.0/min/dropzone.min.css");
                importJavaScriptSrc("/webjars/dropzone/4.2.0/min/dropzone.min.js");
//                jsCustom("/mia-v/js/custom.js");
                return contentPage("forms/invoice_import", model);
            default:
                return contentPage("list/invoice_import", model);
        }
    }

    @RequestMapping(value = "/{action}", method = RequestMethod.POST)
    public String postAction(HttpServletRequest request, Model model, @PathVariable String action) {
        UserApp userApp = getUserApp(request);
        Map<String, String[]> map = request.getParameterMap();
        Map<Integer, Integer> money = new HashMap<>();
        Map<Integer, Integer> amount = new HashMap<>();
        Map<Integer, Product> productMap = new HashMap<>();
        String[] listId = request.getParameterValues("productId");
        List<InvoiceImportDetail> invoiceImportDetailList = new ArrayList<>();
        float totalMoney = 0;
        for (String productId : listId) {
            Product product = (Product) ShopDBBuilder.newInstance(shopDBService, Product.class, "id=?", new ParameterSql[]{new ParameterSql(Integer.class, Integer.parseInt(productId))}).getEntity();
            productMap.put(Integer.parseInt(productId), product);
            amount.put(Integer.parseInt(productId), Integer.parseInt(map.get("amount_" + productId)[0]));
            money.put(Integer.parseInt(productId), Integer.parseInt(map.get("money_" + productId)[0]));
            InvoiceImportDetail detail = new InvoiceImportDetail();
            detail.setAmount(Float.parseFloat(map.get("amount_" + productId)[0]));
            detail.setPrice(Integer.parseInt(map.get("money_" + productId)[0]));
            detail.setProductId(product.getId());
            detail.setProductName(product.getName());
            detail.setProductNameSecond(product.getNameSecond());
            invoiceImportDetailList.add(detail);
            detail.setTotalMoney(detail.getAmount() * detail.getPrice());
            totalMoney += detail.getTotalMoney();

        }
        InvoiceImportHeader header = new InvoiceImportHeader();
        header.setDateImport(Utils.DateNow(Utils.DATE_YYYY_MM_DD_JP));
        header.setDateUpdate(getDataUpdate());
        header.setTotal(totalMoney);
        header.setIsComplete(1);
        header.setUserId(userApp.getId());
        header.setIsPay(1);
        shopDBService.save(header);
        int no = 1;
        for (InvoiceImportDetail obj : invoiceImportDetailList) {
            obj.setDateImport(header.getDateImport());
            obj.setDateUpdate(header.getDateUpdate());
            obj.setNo(no);
            obj.setId(header.getId());
            shopDBService.save(obj);
            no++;
            if (header.getIsComplete()==1){
                Inventory inventory = (Inventory) ShopDBBuilder.newInstance(shopDBService, Inventory.class, "productId=?", new ParameterSql[]{new ParameterSql(Integer.class, obj.getProductId())}).getEntity();
                if (inventory != null) {
                    inventory.setAmountImport(inventory.getAmountImport() + obj.getAmount());
                } else {
                    inventory = new Inventory();
                    inventory.setProductId(obj.getProductId());
                    inventory.setAmountImport(obj.getAmount());
                }
                Product product = productMap.get(obj.getProductId());
                inventory.setProductName(product.getName());
                inventory.setProductNameSecond(product.getNameSecond());
                inventory.setAmountInventory(inventory.getAmountImport() + inventory.getAmountFirst() - inventory.getAmountExport());
                inventory.setDateUpdate(obj.getDateUpdate());
                shopDBService.save(inventory);
            }

        }
        return "redirect:/admin/invoice_import";
    }
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String getHome(Model model) {
        List<InvoiceImportHeader> list = ShopDBBuilder.newInstance(shopDBService,InvoiceImportHeader.class).getList();
        model.addAttribute("list", list);
        return contentPage("list/invoice_import", model);
    }
//    @RequestMapping(value = {"/del/{id}"}, method = RequestMethod.GET)
//    public String ajaxDelete(Model model, @PathVariable int id) {
//        ShopDBBuilder.newInstance(shopDBService,Import.class, "productId=?", new ParameterSql[]{new ParameterSql(Integer.class, id)}).delete();
//        List<Import> list = ShopDBBuilder.newInstance(shopDBService,Import.class).getList();
//        model.addAttribute("list", list);
//        return "admin/fragments/list/import";
//    }
    @RequestMapping(value = {"/detail/{id}"}, method = RequestMethod.GET)
    public String detail(Model model,@PathVariable String id) {
        int productId = Integer.parseInt(id);
        List<InvoiceImportDetail> list = ShopDBBuilder.newInstance(shopDBService,InvoiceImportDetail.class,"id=?",new ParameterSql[]{new ParameterSql(Integer.class,productId)}).getList();
        model.addAttribute("list", list);
        InvoiceImportHeader importHeader = (InvoiceImportHeader) ShopDBBuilder.newInstance(shopDBService,InvoiceImportHeader.class,"id=?",new ParameterSql[]{new ParameterSql(Integer.class,productId)}).getEntity();
        model.addAttribute("total", importHeader.getTotal());
        return "admin/fragments/ajax/modal/popupImportDetail";
    }
}
