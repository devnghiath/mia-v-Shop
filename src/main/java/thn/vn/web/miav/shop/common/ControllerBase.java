package thn.vn.web.miav.shop.common;

import org.springframework.ui.Model;
import thn.vn.web.miav.shop.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public abstract class ControllerBase {
    protected List<String> listCss = new ArrayList<>();
    protected List<String> listJs = new ArrayList<>();
    protected List<String> footerJs = new ArrayList<>();
    protected void importJavaScriptSrc(String src){
        listJs.add(src);
    }
    protected void importCssHref(String href){
        listCss.add(href);
    }
    protected String viewName(String htmlName){
        if (htmlName.isEmpty()){
            return pathLayout();
        }
        return pathLayout()+"/"+htmlName;
    }
    public String contentPage(String contentHtmlName, Model model){
        List<String> cssList = new ArrayList<>();
        cssList.addAll(initCssCommon());
        if (listCss.size()>0){
            cssList.addAll(listCss);
        }
        cssList.add("/mia-v/css/mia-v.min.css");
        model.addAttribute("cssList",cssList);

        List<String> jsList = new ArrayList<>();
        jsList.addAll(initJavaScriptSrcCommon());

        jsList.add("/webjars/jquery-validation/1.14.0/jquery.validate.min.js");
        jsList.add("/mia-v/js/mia-v.min.js");
        if (listJs.size()>0){
            jsList.addAll(listJs);
        }
        if (footerJs.size()>0){
            jsList.addAll(footerJs);
        }
        listCss.clear();
        footerJs.clear();
        model.addAttribute("jsList",jsList);

        listJs.clear();
        model.addAttribute("template","admin/fragments/"+contentHtmlName);
        return viewName("index-admin");
    }
    public String getDataUpdate(){
        return  Utils.DateNow(Utils.DATE_FILE);
    }
    protected abstract List<String> initCssCommon();
    protected abstract List<String> initJavaScriptSrcCommon();
    protected abstract String pathLayout();
    protected void jsCustom(String src){
        listJs.add(src);
    }
}
