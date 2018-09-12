package thn.vn.web.miav.shop.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import thn.vn.web.miav.shop.common.RestControllerBase;
import thn.vn.web.miav.shop.constants.ResponseCode;
import thn.vn.web.miav.shop.models.entity.Category;
import thn.vn.web.miav.shop.models.entity.Product;
import thn.vn.web.miav.shop.models.response.ApiResponse;
import thn.vn.web.miav.shop.models.response.ErrorResponse;
import thn.vn.web.miav.shop.utils.ParameterSql;
import thn.vn.web.miav.shop.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/api/sys", method = RequestMethod.GET)
public class SysDataApi extends RestControllerBase {
    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public ResponseEntity<?> getCategory(@RequestParam(value = "dateUpdate",required = false)String dateUpdate){
        List<Category> list = new ArrayList<>();
        if (Utils.isEmpty(dateUpdate)){
            list.addAll(dataBaseService.getAll(Category.class));
        } else {
            list.addAll(dataBaseService.getAll(Category.class,"dateUpdate>=?",new ParameterSql[]{new ParameterSql(String.class,dateUpdate)}));
        }
        ErrorResponse errorResponse = new ErrorResponse(ResponseCode.RESULT_OK,"");
        HashMap<String,Object> data = new HashMap<>();
        data.put("list",list);
        data.put("dateUpdate",Utils.DateNow(Utils.DATE_FILE));
        ApiResponse apiResponse = new ApiResponse(errorResponse,data);
        return ResponseEntity.ok(apiResponse);
    }
    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public ResponseEntity<?> getProduct(@RequestParam(value = "dateUpdate",required = false)String dateUpdate){
        List<Product> list = new ArrayList<>();
        if (Utils.isEmpty(dateUpdate)){
            list.addAll(dataBaseService.getAll(Product.class));
        } else {
            list.addAll(dataBaseService.getAll(Product.class,"dateUpdate>=?",new ParameterSql[]{new ParameterSql(String.class,dateUpdate)}));
        }
        ErrorResponse errorResponse = new ErrorResponse(ResponseCode.RESULT_OK,"");
        HashMap<String,Object> data = new HashMap<>();
        data.put("list",list);
        data.put("dateUpdate",Utils.DateNow(Utils.DATE_FILE));
        ApiResponse apiResponse = new ApiResponse(errorResponse,data);
        return ResponseEntity.ok(apiResponse);
    }
}
