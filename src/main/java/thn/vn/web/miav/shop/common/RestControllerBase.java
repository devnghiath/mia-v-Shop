package thn.vn.web.miav.shop.common;

import org.springframework.beans.factory.annotation.Autowired;
import thn.vn.web.miav.shop.component.JwtTokenProvider;
import thn.vn.web.miav.shop.services.DataBaseService;
import thn.vn.web.miav.shop.services.ShopDBService;
import thn.vn.web.miav.shop.utils.Utils;

public abstract class RestControllerBase {
    @Autowired
    protected DataBaseService dataBaseService;
    @Autowired
    protected ShopDBService shopDBService;
    @Autowired
    private JwtTokenProvider tokenProvider;
    public String getDataUpdate(){
        return  Utils.DateNow(Utils.DATE_FILE_NOW);
    }
}
