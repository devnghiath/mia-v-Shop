package thn.vn.web.miav.shop.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import thn.vn.web.miav.shop.component.JwtTokenProvider;
import thn.vn.web.miav.shop.dao.ShopDBBuilder;
import thn.vn.web.miav.shop.models.entity.TokenApp;
import thn.vn.web.miav.shop.models.entity.UserApp;
import thn.vn.web.miav.shop.services.DataBaseService;
import thn.vn.web.miav.shop.services.ShopDBService;
import thn.vn.web.miav.shop.utils.ParameterSql;
import thn.vn.web.miav.shop.utils.Utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class AdminControllerBase extends ControllerBase{
//    @Autowired
//    protected DataBaseService dataBaseService;
    @Autowired
    protected ShopDBService shopDBService;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Override
    protected List<String> initCssCommon() {
        List<String> cssList = new ArrayList<>();
        cssList.add("/webjars/bootstrap/3.3.7/css/bootstrap.min.css");
        cssList.add("/webjars/font-awesome/4.7.0/css/font-awesome.min.css");
        return cssList;
    }

    @Override
    protected List<String> initJavaScriptSrcCommon() {
        List<String> jsList = new ArrayList<>();
        jsList.add("/webjars/jquery/2.1.4/jquery.min.js");
        jsList.add("/webjars/bootstrap/3.3.7/js/bootstrap.min.js");
        return jsList;
    }

    @Override
    protected String pathLayout() {
        return "admin";
    }

    public static String generateId(int length,String prefix){
        return Utils.generateId(length,prefix);
    }

    protected UserApp getUserApp(HttpServletRequest request){
        String bearerToken = "";
        for(Cookie cookie:request.getCookies()){
            if (cookie.getName().equals("tokenId")){
                bearerToken = cookie.getValue();
                break;
            }
        }
        ShopDBBuilder shopDBBuilder = new ShopDBBuilder(shopDBService);
        shopDBBuilder.select(TokenApp.class).setClause("tokenId=?").setParameterSql(new ParameterSql[]{new ParameterSql(String.class,bearerToken)});

        TokenApp tokenApp = (TokenApp)ShopDBBuilder.newInstance(shopDBService,TokenApp.class,"tokenId=?",new ParameterSql[]{new ParameterSql(String.class,bearerToken)}).getEntity();
        int userId = tokenProvider.getUserIdFromJWT(tokenApp.getTokenValue());
        UserApp userApp = (UserApp)ShopDBBuilder.newInstance(shopDBService,UserApp.class,"id=?",new ParameterSql[]{new ParameterSql(Integer.class,userId)}).getEntity();
        return userApp;
    }
}
