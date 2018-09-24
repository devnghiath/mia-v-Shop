package thn.vn.web.miav.shop.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import thn.vn.web.miav.shop.common.RestControllerBase;
import thn.vn.web.miav.shop.constants.ResponseCode;
import thn.vn.web.miav.shop.dao.ShopDBBuilder;
import thn.vn.web.miav.shop.models.entity.*;
import thn.vn.web.miav.shop.models.request.SysDataTableBody;
import thn.vn.web.miav.shop.models.response.ApiResponse;
import thn.vn.web.miav.shop.models.response.ErrorResponse;
import thn.vn.web.miav.shop.utils.ParameterSql;
import thn.vn.web.miav.shop.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/api/sys", method = RequestMethod.GET)
public class SysDataApi extends RestControllerBase {
    private <T> HashMap<String, Object> getData( String dateUpdate, int maxResult, int offset,String tableName) {
        ShopDBBuilder shopDBBuilder = ShopDBBuilder.newInstance(shopDBService);
        switch (tableName) {
            case "category":
                shopDBBuilder.select(Category.class);
                break;
            case "invoice_import_detail":
                shopDBBuilder.select(InvoiceImportDetail.class);
                break;
            case "manufacturer":
                shopDBBuilder.select(Manufacturer.class);
                break;
            case "brand":
                shopDBBuilder.select(Brand.class);
                break;
            case "product":
                shopDBBuilder.select(Product.class);
                break;
            case "invoice_import_header":
                shopDBBuilder.select(InvoiceImportHeader.class);
                break;
            case "invoice_export_detail":
                shopDBBuilder.select(InvoiceExportDetail.class);
                break;
            case "invoice_export_header":
                shopDBBuilder.select(InvoiceExportHeader.class);
                break;
            case "inventory":
                shopDBBuilder.select(Inventory.class);
                break;
        }
        if (Utils.isEmpty(dateUpdate)){
            dateUpdate = "";
        }
        List<T> list = new ArrayList<>();

        shopDBBuilder.setClause("dateUpdate>?");
        shopDBBuilder.setParameterSql( new ParameterSql[]{new ParameterSql(String.class, dateUpdate)});
        shopDBBuilder.setMaxResult(maxResult);
        shopDBBuilder.setFirstResult(offset);
        list.addAll(shopDBBuilder.getList());
        HashMap<String, Object> data = new HashMap<>();
        if (!Utils.isEmpty(maxResult)) {

            data.put("offset", (list.size() * offset) + maxResult);
            data.put("isComplete", list.size() < maxResult);
            if (list.size() < maxResult){
                data.put("lastDateUpdate", ShopDBBuilder.newInstance(shopDBService,shopDBBuilder.getClazz()).getMax("dateUpdate"));
            }
        }
        data.put("list",list);

        return data;
    }

    private <T> ApiResponse getSysData(Class<T> clazz, String dateUpdate, String maxResult, String offset) {
        List<T> list = new ArrayList<>();

        ShopDBBuilder shopDBBuilder = ShopDBBuilder.newInstance(shopDBService);
        shopDBBuilder.select(clazz);
        if (!Utils.isEmpty(dateUpdate)) {
            shopDBBuilder.setClause("dateUpdate>=?");
            shopDBBuilder.setParameterSql( new ParameterSql[]{new ParameterSql(String.class, dateUpdate)});
        }
        if (!Utils.isEmpty(maxResult)) {
            shopDBBuilder.setMaxResult(Integer.parseInt(maxResult));
            shopDBBuilder.setFirstResult(Integer.parseInt(offset));
        }
        list.addAll(shopDBBuilder.getList());

        ErrorResponse errorResponse = new ErrorResponse(ResponseCode.RESULT_OK, "");
        HashMap<String, Object> data = new HashMap<>();
        data.put("list", list);
        if (!Utils.isEmpty(maxResult)) {
            data.put("maxResult", Integer.parseInt(maxResult));
            data.put("offset", (list.size() * Integer.parseInt(offset)) + Integer.parseInt(maxResult));
            data.put("isComplete", list.size() < Integer.parseInt(maxResult));
        }

        data.put("dateUpdate", Utils.DateNow(Utils.DATE_FILE));
        ApiResponse apiResponse = new ApiResponse(errorResponse, data);
        return apiResponse;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> postSys(@Valid @RequestBody HashMap<String,SysDataTableBody> objectHashMap, HttpServletRequest request
            , @RequestParam(value = "maxResult", required = false) String maxResult){
        boolean sysComplete = true;
        HashMap<String,Object> result = new HashMap<>();
        if (Utils.isEmpty(maxResult)) {
            maxResult = "100";
        }
        for (String tableName:objectHashMap.keySet()){
            SysDataTableBody obj = objectHashMap.get(tableName);
            if (obj.getIsComplete()){
                continue;
            }
            HashMap<String, Object> data = new HashMap<>();
            data = getData(obj.getDateUpdate(), Integer.parseInt(maxResult), obj.getOffset(),tableName);
            obj.setIsComplete((Boolean) data.get("isComplete"));
            obj.setOffset((int)data.get("offset"));
            if (sysComplete){
                sysComplete = (boolean) data.get("isComplete");
            }
            result.put(tableName,data);
        }

        result.put("sysComplete",sysComplete);
        result.put("bodyParam",objectHashMap);
        ErrorResponse errorResponse = new ErrorResponse(ResponseCode.RESULT_OK, "");
        ApiResponse apiResponse = new ApiResponse(errorResponse, result);
        return ResponseEntity.ok(apiResponse);
    }





    @RequestMapping(value = "/{sysName}", method = RequestMethod.GET)
    public ResponseEntity<?> getSys(@PathVariable String sysName, @RequestParam(value = "dateUpdate", required = false) String dateUpdate,
                                    @RequestParam(value = "maxResult", required = false) String maxResult,
                                    @RequestParam(value = "offset", required = false) String offset) {
        ApiResponse apiResponse = null;
        switch (sysName) {
            case "category":
                apiResponse = getSysData(Category.class, dateUpdate, maxResult, offset);
                break;
            case "invoice_import_Detail":
                apiResponse = getSysData(InvoiceImportDetail.class, dateUpdate, maxResult, offset);
                break;
            case "manufacturer":
                apiResponse = getSysData(Manufacturer.class, dateUpdate, maxResult, offset);
                break;
            case "brand":
                apiResponse = getSysData(Brand.class, dateUpdate, maxResult, offset);
                break;
            case "product":
                apiResponse = getSysData(Product.class, dateUpdate, maxResult, offset);
                break;
            case "invoice_import_header":
                apiResponse = getSysData(InvoiceImportHeader.class, dateUpdate, maxResult, offset);
                break;
            case "invoice_export_detail":
                apiResponse = getSysData(InvoiceExportDetail.class, dateUpdate, maxResult, offset);
                break;
            case "invoice_export_header":
                apiResponse = getSysData(InvoiceExportHeader.class, dateUpdate, maxResult, offset);
                break;
            case "inventory":
                apiResponse = getSysData(Inventory.class, dateUpdate, maxResult, offset);
                break;
        }
        if (apiResponse == null) {
            apiResponse = new ApiResponse(new ErrorResponse(ResponseCode.ERROR, ""), null);
        }
        return ResponseEntity.ok(apiResponse);
    }

}
