package thn.vn.web.miav.shop.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import thn.vn.web.miav.shop.common.RestControllerBase;
import thn.vn.web.miav.shop.constants.ResponseCode;
import thn.vn.web.miav.shop.models.entity.*;
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
    private <T> ApiResponse getSysData(Class<T>clazz ,String dateUpdate,String maxResult,String offset){
        List<T> list = new ArrayList<>();
        if (Utils.isEmpty(dateUpdate)) {
            if (Utils.isEmpty(maxResult)) {
                list.addAll(dataBaseService.getAll(clazz));
            } else {
                list.addAll(dataBaseService.getAll(clazz,Integer.parseInt(maxResult),Integer.parseInt(offset)));
            }
        } else {
            if (Utils.isEmpty(maxResult)) {
                list.addAll(dataBaseService.getAll(clazz, "dateUpdate>=?", new ParameterSql[]{new ParameterSql(String.class, dateUpdate)}));
            } else {
                list.addAll(dataBaseService.getAll(clazz, "dateUpdate>=?",Integer.parseInt(maxResult),Integer.parseInt(offset), new ParameterSql[]{new ParameterSql(String.class, dateUpdate)}));
            }
        }
        int maxItem = 0;
        if (Utils.isEmpty(dateUpdate)) {
            maxItem = dataBaseService.getAll(clazz).size();
        } else {
            maxItem = dataBaseService.getAll(clazz, "dateUpdate>=?", new ParameterSql[]{new ParameterSql(String.class, dateUpdate)}).size();
        }
        ErrorResponse errorResponse = new ErrorResponse(ResponseCode.RESULT_OK, "");
        HashMap<String, Object> data = new HashMap<>();
        data.put("list", list);
        if (!Utils.isEmpty(maxResult)){
            data.put("maxResult", Integer.parseInt(maxResult));
            data.put("offset", (list.size()*Integer.parseInt(offset))+Integer.parseInt(maxResult));
            int total = (list.size()*Integer.parseInt(offset))+Integer.parseInt(maxResult) +Integer.parseInt(maxResult) ;
            data.put("isComplete",total>maxItem);
        }

        data.put("dateUpdate", Utils.DateNow(Utils.DATE_FILE));
        ApiResponse apiResponse = new ApiResponse(errorResponse, data);
        return apiResponse;
    }

    @RequestMapping(value = "/{sysName}", method = RequestMethod.GET)
    public ResponseEntity<?> getSys(@PathVariable String sysName,@RequestParam(value = "dateUpdate", required = false) String dateUpdate,
                                    @RequestParam(value = "maxResult", required = false) String maxResult,
                                    @RequestParam(value = "offset", required = false) String offset) {
        ApiResponse apiResponse = null;
        switch (sysName){
            case "category":
                apiResponse = getSysData(Category.class,dateUpdate,maxResult,offset);
                break;
            case "import":
                apiResponse = getSysData(Import.class,dateUpdate,maxResult,offset);
                break;
            case "manufacturer":
                apiResponse = getSysData(Manufacturer.class,dateUpdate,maxResult,offset);
                break;
            case "brand":
                apiResponse = getSysData(Brand.class,dateUpdate,maxResult,offset);
                break;
            case "product":
                apiResponse = getSysData(Product.class,dateUpdate,maxResult,offset);
                break;
            case "import_header":
                apiResponse = getSysData(ImportHeader.class,dateUpdate,maxResult,offset);
                break;
            case "export":
                apiResponse = getSysData(Export.class,dateUpdate,maxResult,offset);
                break;
            case "export_header":
                apiResponse = getSysData(ExportHeader.class,dateUpdate,maxResult,offset);
                break;
            case "inventory":
                apiResponse = getSysData(Inventory.class,dateUpdate,maxResult,offset);
                break;
        }
        if (apiResponse == null){
            apiResponse = new ApiResponse(new ErrorResponse(ResponseCode.ERROR,""),null);
        }
        return ResponseEntity.ok(apiResponse);
    }
}
