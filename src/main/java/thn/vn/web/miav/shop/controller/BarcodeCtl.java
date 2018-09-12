package thn.vn.web.miav.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import thn.vn.web.miav.shop.config.BarcodeConfig;
import thn.vn.web.miav.shop.services.BarcodeService;
import thn.vn.web.miav.shop.utils.Utils;

@Controller
@RequestMapping(value = "/label")
public class BarcodeCtl {
    @Autowired
    private BarcodeService barcodeService;
    @RequestMapping(value = "/{labelName}",method = RequestMethod.GET)
    @ResponseBody
    public byte[] getbarcode(@PathVariable String labelName,Model model){
        try {
            if (Utils.isEmpty(labelName)){
                labelName = Utils.generateBarcode(13);
            }
            return barcodeService.barcode(labelName, BarcodeConfig.Barcode.defaultConfig());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
