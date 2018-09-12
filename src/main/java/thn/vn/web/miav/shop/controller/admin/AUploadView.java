package thn.vn.web.miav.shop.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import thn.vn.web.miav.shop.common.AdminControllerBase;

import thn.vn.web.miav.shop.models.entity.UserApp;
import thn.vn.web.miav.shop.models.response.ImageUploadResponse;
import thn.vn.web.miav.shop.services.StorageService;
import thn.vn.web.miav.shop.utils.Utils;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AUploadView extends AdminControllerBase {
    @Autowired
    StorageService storageService;
    @RequestMapping(value = {"/admin/uploadImageProduct"},method = RequestMethod.POST,produces= MediaType.APPLICATION_JSON_VALUE)
    public String uploadImage(@RequestParam("file") MultipartFile file, Model model, HttpServletRequest request){
        String fileName = Utils.DateNow(Utils.DATE_FILE_NOW);
        UserApp userApp = getUserApp(request);
        if (!Utils.isEmpty(file.getOriginalFilename())) {
            fileName = storageService.storeTempImageProduct(file,userApp.getId(), fileName);
        } else {
            fileName = "";
        }
        ImageUploadResponse image = new ImageUploadResponse(fileName);
        model.addAttribute("item", image);
        model.addAttribute("name", "fileName");
        return "admin/fragments/ajax/modal/inputHidden";
    }
}
