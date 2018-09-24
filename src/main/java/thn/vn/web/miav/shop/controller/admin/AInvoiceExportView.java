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
@RequestMapping(value = "/admin/invoice_export")
public class AInvoiceExportView extends AdminControllerBase {
    @RequestMapping(value = "/{action}", method = RequestMethod.GET)
    public String getNew(Model model,@PathVariable String action){
        if (action.equalsIgnoreCase("new")){
            return contentPage("forms/invoice_export", model);
        } else {
            return contentPage("list/invoice_export", model);
        }
    }
    @RequestMapping(value = "/{action}", method = RequestMethod.POST)
    public String postSell(HttpServletRequest request, Model model, @PathVariable String action) {
        UserApp userApp = getUserApp(request);
        Map<String, String[]> map = request.getParameterMap();
        Map<Integer, Integer> money = new HashMap<>();
        Map<Integer, Integer> amount = new HashMap<>();
        Map<Integer, Product> productMap = new HashMap<>();
        String[] listId = request.getParameterValues("productId");
        List<InvoiceExportDetail> invoiceImportDetailList = new ArrayList<>();
        float totalMoney = 0;
        for (String productId : listId) {
            Product product = (Product) ShopDBBuilder.newInstance(shopDBService, Product.class, "id=?", new ParameterSql[]{new ParameterSql(Integer.class, Integer.parseInt(productId))}).getEntity();
            productMap.put(Integer.parseInt(productId), product);
            amount.put(Integer.parseInt(productId), Integer.parseInt(map.get("amount_" + productId)[0]));
            money.put(Integer.parseInt(productId), Integer.parseInt(map.get("money_" + productId)[0]));
            InvoiceExportDetail detail = new InvoiceExportDetail();
            detail.setAmount(Float.parseFloat(map.get("amount_" + productId)[0]));
            detail.setPrice(Integer.parseInt(map.get("money_" + productId)[0]));
            detail.setProductId(product.getId());
            detail.setProductName(product.getName());
            detail.setProductNameSecond(product.getNameSecond());
            invoiceImportDetailList.add(detail);
            detail.setTotalMoney(detail.getAmount() * detail.getPrice());
            totalMoney += detail.getTotalMoney();

        }
        InvoiceExportHeader header = new InvoiceExportHeader();
        header.setDateExport(Utils.DateNow(Utils.DATE_YYYY_MM_DD_JP));
        header.setDateUpdate(getDataUpdate());
        header.setTotal(totalMoney);
        header.setIsComplete(1);
        header.setIsPay(1);
        header.setUserId(userApp.getId());
        shopDBService.save(header);
        int no = 1;
        for (InvoiceExportDetail obj : invoiceImportDetailList) {
            obj.setDateExport(header.getDateExport());
            obj.setDateUpdate(header.getDateUpdate());
            obj.setNo(no);
            obj.setId(header.getId());
            shopDBService.save(obj);
            no++;
            if (header.getIsComplete()==1){
                Inventory inventory = (Inventory) ShopDBBuilder.newInstance(shopDBService, Inventory.class, "productId=?", new ParameterSql[]{new ParameterSql(Integer.class, obj.getProductId())}).getEntity();
                if (inventory != null) {
                    inventory.setAmountExport(inventory.getAmountExport() + obj.getAmount());
                } else {
                    inventory = new Inventory();
                    inventory.setProductId(obj.getProductId());
                    inventory.setAmountExport(obj.getAmount());
                }
                Product product = productMap.get(obj.getProductId());
                inventory.setProductName(product.getName());
                inventory.setProductNameSecond(product.getNameSecond());
                inventory.setAmountInventory(inventory.getAmountImport() + inventory.getAmountFirst() - inventory.getAmountExport());
                inventory.setDateUpdate(obj.getDateUpdate());
                shopDBService.save(inventory);
            }

        }

        return "redirect:/admin/invoice_export";
    }
    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String home(Model model) {
        List<InvoiceExportHeader> list = ShopDBBuilder.newInstance(shopDBService,InvoiceExportHeader.class).getList();
        model.addAttribute("list", list);
        return contentPage("list/invoice_export", model);
    }
    @RequestMapping(value = {"/detail/{id}"}, method = RequestMethod.GET)
    public String detail(Model model,@PathVariable String id) {
        List<InvoiceExportDetail> list = ShopDBBuilder.newInstance(shopDBService,InvoiceExportDetail.class,"id=?",new ParameterSql[]{new ParameterSql(Integer.class,Integer.parseInt(id))}).getList();
        model.addAttribute("list", list);
        InvoiceExportHeader exportHeader = (InvoiceExportHeader) ShopDBBuilder.newInstance(shopDBService,InvoiceExportHeader.class,"id=?",new ParameterSql[]{new ParameterSql(Integer.class,Integer.parseInt(id))}).getEntity();
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
