package thn.vn.web.miav.shop.controller.admin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import thn.vn.web.miav.shop.common.AdminControllerBase;
import thn.vn.web.miav.shop.dao.ShopDBBuilder;
import thn.vn.web.miav.shop.models.entity.*;
import thn.vn.web.miav.shop.models.response.ImageUploadResponse;
import thn.vn.web.miav.shop.services.StorageService;
import thn.vn.web.miav.shop.utils.ParameterSql;
import thn.vn.web.miav.shop.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/admin/product")
public class AProductView extends AdminControllerBase {


    @Autowired
    StorageService storageService;

    @ModelAttribute(value = "formData")
    public Product newEntity() {
        return new Product();
    }

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    String list(Model model) {
        List<Product> list = ShopDBBuilder.newInstance(shopDBService,Product.class).getList();
        model.addAttribute("list", list);
        return contentPage("list/product", model);
    }

    @RequestMapping(value = "/{action}", method = RequestMethod.GET)
    String action(Model model, @PathVariable String action) {
        if (action.equalsIgnoreCase("new")) {
            importCssHref("/webjars/dropzone/4.2.0/min/dropzone.min.css");
            importJavaScriptSrc("/webjars/dropzone/4.2.0/min/dropzone.min.js");
            jsCustom("/mia-v/js/custom.js");
            return contentPage("forms/product", model);
        } else if (action.equalsIgnoreCase("list")) {
            List<Product> list = ShopDBBuilder.newInstance(shopDBService,Product.class).getList();
            model.addAttribute("list", list);
            return contentPage("list/product", model);
        } else {
            return contentPage("list/product", model);
        }
    }

    @RequestMapping(value = {"/popup"}, method = RequestMethod.GET)
    public String ajaxList(Model model,@RequestParam(value = "listId", required = false) List<Integer> listId,@RequestParam(value = "isWarehouse", required = false)String isWarehouse ) {

        List<Product> list;
        List<Integer> listFilter = new ArrayList<>();
        if (Utils.isEmpty(isWarehouse)){
            isWarehouse = "1";
        }
        if (listId.size()>0){
            list = ShopDBBuilder.newInstance(shopDBService,Product.class,"isWarehouse=? and id not in (:id)",new ParameterSql[]{new ParameterSql(Integer.class,Integer.parseInt(isWarehouse))}).setListParameter("id",listId).getList();
        } else {
            list = ShopDBBuilder.newInstance(shopDBService,Product.class,"isWarehouse=?",new ParameterSql[]{new ParameterSql(Integer.class,Integer.parseInt(isWarehouse))}).getList();
        }

        model.addAttribute("list", list);
        return "admin/fragments/ajax/modal/popupProduct";
    }

    @RequestMapping(value = "/{action}/{id}", method = RequestMethod.GET)
    public String viewUpdate(Model model, @PathVariable String action, @PathVariable int id) {

        if (action.equalsIgnoreCase("update")) {
            Product product =(Product)ShopDBBuilder.newInstance(shopDBService,Product.class, "id=?", new ParameterSql[]{new ParameterSql(Integer.class, id)}).getEntity();
            model.addAttribute("product", product);
            return contentPage("forms/product", model);
        } else {
            List<Category> list = ShopDBBuilder.newInstance(shopDBService,Category.class).getList();
            model.addAttribute("list", list);
            return contentPage("list/product", model);
        }
    }

    @RequestMapping(value = "/{action}", method = RequestMethod.POST)
    public String save(HttpServletRequest request,  @ModelAttribute(value = "formData") Product formData, Model model, @PathVariable String action) {
        formData.setLabel(generalBarcode());

        List<ImageUploadResponse> imageUploadResponseList = new ArrayList<>();

        if (request.getParameterMap().get("fileName").length>0){
            String pathTemp = "/tempImage/"+getUserApp(request).getId()+"/";
            String pathTo = "/productImage/";
            for (int i =0 ; i < request.getParameterMap().get("fileName").length;i++){
                String fileName = request.getParameterMap().get("fileName")[i];
                String temp = pathTemp+fileName;
                String target = pathTo+fileName;
                storageService.moveFile(temp,target);
                imageUploadResponseList.add(new ImageUploadResponse(target));
            }
            storageService.deleteAllFile(pathTemp);
        }
        Category category = (Category) ShopDBBuilder.newInstance(shopDBService,Category.class, "id=?", new ParameterSql[]{new ParameterSql(Integer.class, formData.getCategoryId())}).getEntity();
        Brand brand = (Brand) ShopDBBuilder.newInstance(shopDBService,Brand.class, "id=?", new ParameterSql[]{new ParameterSql(Integer.class, formData.getBrandId())}).getEntity();
        Manufacturer manufacturer = (Manufacturer) ShopDBBuilder.newInstance(shopDBService,Manufacturer.class, "id=?", new ParameterSql[]{new ParameterSql(Integer.class, formData.getManufacturersId())}).getEntity();
        formData.setCategoryName(category.getName());
        formData.setBrandName(brand.getName());
        formData.setManufacturersName(manufacturer.getName());
        if (imageUploadResponseList.size()>0){
            formData.setImages(new Gson().toJson(imageUploadResponseList));
        } else {
            formData.setImages("");
        }

        formData.setDateUpdate(getDataUpdate());
        shopDBService.save(formData);
        ManualCodeProduct manualCodeProduct = new ManualCodeProduct();
        manualCodeProduct.setPrefix(category.getManualCode());
        ShopDBBuilder shopDBBuilder = new ShopDBBuilder(shopDBService,ManualCodeProduct.class);
        shopDBBuilder.setClause("prefix=? ");

        shopDBBuilder.setParameterSql(new ParameterSql[]{new ParameterSql(String.class,manualCodeProduct.getPrefix())});
        StringBuilder manualCode = new StringBuilder();
        if (shopDBBuilder.check()){
            manualCodeProduct = (ManualCodeProduct)shopDBBuilder.getEntity();
            manualCodeProduct.setNo(manualCodeProduct.getNo()+1);
            manualCode.append(Integer.parseInt(manualCodeProduct.getPrefix().trim())+manualCodeProduct.getNo());
            manualCode.append(" ");
            manualCode.append(manufacturer.getManualCode().trim());
            manualCode.append(" ");
            manualCode.append(formData.getPriceSell());
        } else {
            manualCodeProduct.setNo(1);
            manualCode.append(Integer.parseInt(manualCodeProduct.getPrefix().trim())+manualCodeProduct.getNo());
            manualCode.append(" ");
            manualCode.append(manufacturer.getManualCode().trim());
            manualCode.append(" ");
            manualCode.append(formData.getPriceSell());
        }
        shopDBService.save(manualCodeProduct);
        formData.setManualCode(manualCode.toString());
        shopDBService.save(formData);
        ProductLabel productLabel = new ProductLabel();
        productLabel.setLabel(formData.getLabel());
        productLabel.setManualLabel(formData.getManualCode());
        productLabel.setProductId(formData.getId());
        return "redirect:/admin/product";
    }

    private String generalBarcode() {
        String barcode = Utils.generateBarcode(13);
        if (ShopDBBuilder.newInstance(shopDBService,Product.class, "label=?", new ParameterSql[]{new ParameterSql(String.class, barcode)}).check()) {
            generalBarcode();
        }
        return barcode;
    }

    @RequestMapping(value = {"/del/{id}"}, method = RequestMethod.GET)
    public String ajaxDelete(Model model, @PathVariable int id) {
        ShopDBBuilder.newInstance(shopDBService,Product.class, "id=?", new ParameterSql[]{new ParameterSql(Integer.class, id)}).delete();

        List<Product> list = ShopDBBuilder.newInstance(shopDBService,Product.class).getList();
        model.addAttribute("list", list);
        return "admin/fragments/list/product";
    }
    @RequestMapping(value = {"/image/{id}"}, method = RequestMethod.GET)
    public String ajaxImageProduct(Model model, @PathVariable int id){
        Product product =  (Product)ShopDBBuilder.newInstance(shopDBService,Product.class,"id=?",new ParameterSql[]{new ParameterSql(Integer.class, id)}).getEntity();
        List<ImageUploadResponse> list = (List<ImageUploadResponse>) new Gson().fromJson(product.getImages(),
                new TypeToken<List<ImageUploadResponse>>() {
                }.getType());
        model.addAttribute("list",list);
        return "admin/fragments/ajax/modal/imageProduct";
    }
    @RequestMapping(value = {"/ajax/item/{id}"}, method = RequestMethod.GET)
    public ResponseEntity<?> ajaxDetailItem(@PathVariable int id){
        Product product = (Product)ShopDBBuilder.newInstance(shopDBService,Product.class,"id=?",new ParameterSql[]{new ParameterSql(Integer.class, id)}).getEntity();
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
    int getManualIdProduct(){
        return 0;
    }
}
