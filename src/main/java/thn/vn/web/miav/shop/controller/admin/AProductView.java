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
import thn.vn.web.miav.shop.models.entity.Brand;
import thn.vn.web.miav.shop.models.entity.Category;
import thn.vn.web.miav.shop.models.entity.Manufacturer;
import thn.vn.web.miav.shop.models.entity.Product;
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
        List<Product> list = dataBaseService.getAll(Product.class);
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
            List<Product> list = dataBaseService.getAll(Product.class);
            model.addAttribute("list", list);
            return contentPage("list/product", model);
        } else {
            return contentPage("list/product", model);
        }
    }

    @RequestMapping(value = {"/popup"}, method = RequestMethod.GET)
    public String ajaxList(Model model) {
        List<Product> list = dataBaseService.getAll(Product.class);
        model.addAttribute("list", list);
        return "admin/fragments/ajax/modal/popupProduct";
    }

    @RequestMapping(value = "/{action}/{id}", method = RequestMethod.GET)
    public String viewUpdate(Model model, @PathVariable String action, @PathVariable int id) {

        if (action.equalsIgnoreCase("update")) {
            Product product = dataBaseService.find(Product.class, "id=?", new ParameterSql[]{new ParameterSql(Integer.class, id)});
            model.addAttribute("product", product);
            return contentPage("forms/product", model);
        } else {
            List<Category> list = dataBaseService.getAll(Category.class);
            model.addAttribute("list", list);
            return contentPage("list/product", model);
        }
    }

    @RequestMapping(value = "/{action}", method = RequestMethod.POST)
    public String save(HttpServletRequest request,  @ModelAttribute(value = "formData") Product formData, Model model, @PathVariable String action) {
        formData.setLabel(generalBarcode());
//        if (!Utils.isEmpty(file.getOriginalFilename())) {
//            String nameFile = storageService.store(file, formData.getLabel());
//            formData.setImages("/upload/" + nameFile);
//        } else {
//            formData.setImages("");
//        }
        List<ImageUploadResponse> imageUploadResponseList = new ArrayList<>();
        if (request.getParameterMap().get("fileName").length>0){
            for (int i =0 ; i < request.getParameterMap().get("fileName").length;i++){
                String fileName = request.getParameterMap().get("fileName")[i];
                String pathTemp = "/tempImage/"+getUserApp(request).getId()+"/"+fileName;
                String pathTo = "/productImage/"+fileName;
                storageService.moveFile(pathTemp,pathTo);
                imageUploadResponseList.add(new ImageUploadResponse(pathTo));
            }
        }
        Category category = dataBaseService.find(Category.class, "id=?", new ParameterSql[]{new ParameterSql(Integer.class, formData.getCategoryId())});
        Brand brand = dataBaseService.find(Brand.class, "id=?", new ParameterSql[]{new ParameterSql(Integer.class, formData.getBrandId())});
        Manufacturer manufacturer = dataBaseService.find(Manufacturer.class, "id=?", new ParameterSql[]{new ParameterSql(Integer.class, formData.getManufacturersId())});
        formData.setCategoryName(category.getName());
        formData.setBrandName(brand.getName());
        formData.setManufacturersName(manufacturer.getName());
        if (imageUploadResponseList.size()>0){
            formData.setImages(new Gson().toJson(imageUploadResponseList));
        } else {
            formData.setImages("");
        }
        formData.setDateUpdate(Utils.DateNow(Utils.DATE_FILE));
        dataBaseService.save(formData);
        return "redirect:/admin/product";
    }

    private String generalBarcode() {
        String barcode = Utils.generateBarcode(13);
        if (dataBaseService.checked(Product.class, "label=?", new ParameterSql[]{new ParameterSql(String.class, barcode)})) {
            generalBarcode();
        }
        return barcode;
    }

    @RequestMapping(value = {"/del/{id}"}, method = RequestMethod.GET)
    public String ajaxDelete(Model model, @PathVariable int id) {
        dataBaseService.delete(Product.class, "id=?", new ParameterSql[]{new ParameterSql(Integer.class, id)});
        List<Product> list = dataBaseService.getAll(Product.class);
        model.addAttribute("list", list);
        return "admin/fragments/list/product";
    }
    @RequestMapping(value = {"/image/{id}"}, method = RequestMethod.GET)
    public String ajaxImageProduct(Model model, @PathVariable int id){
        Product product = dataBaseService.find(Product.class,"id=?",new ParameterSql[]{new ParameterSql(Integer.class, id)});
        List<ImageUploadResponse> list = (List<ImageUploadResponse>) new Gson().fromJson(product.getImages(),
                new TypeToken<List<ImageUploadResponse>>() {
                }.getType());
        model.addAttribute("list",list);
        return "admin/fragments/ajax/modal/imageProduct";
    }
    @RequestMapping(value = {"/ajax/item/{id}"}, method = RequestMethod.GET)
    public ResponseEntity<?> ajaxDetailItem(@PathVariable int id){
        Product product = dataBaseService.find(Product.class,"id=?",new ParameterSql[]{new ParameterSql(Integer.class, id)});
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
}
