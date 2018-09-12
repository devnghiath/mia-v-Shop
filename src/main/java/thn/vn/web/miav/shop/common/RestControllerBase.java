package thn.vn.web.miav.shop.common;

import org.springframework.beans.factory.annotation.Autowired;
import thn.vn.web.miav.shop.component.JwtTokenProvider;
import thn.vn.web.miav.shop.services.DataBaseService;

public abstract class RestControllerBase {
    @Autowired
    protected DataBaseService dataBaseService;
    @Autowired
    private JwtTokenProvider tokenProvider;
}
