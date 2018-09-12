package thn.vn.web.miav.shop.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import thn.vn.web.miav.shop.services.DataBaseService;

import java.util.ArrayList;
import java.util.List;

public abstract class ClientControllerBase extends ControllerBase{
    @Autowired
    protected DataBaseService dataBaseService;
    @Override
    protected List<String> initCssCommon() {
        return null;
    }

    @Override
    protected List<String> initJavaScriptSrcCommon() {
        return null;
    }

    @Override
    protected String pathLayout() {
        return "client";
    }

    protected void importJavaScriptSrc(String src){
        listJs.add(src);
    }
    protected void importCssHref(String href){
        listCss.add(href);
    }

}
