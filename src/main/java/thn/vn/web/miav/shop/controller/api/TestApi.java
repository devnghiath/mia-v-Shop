package thn.vn.web.miav.shop.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import thn.vn.web.miav.shop.component.JwtTokenProvider;
import thn.vn.web.miav.shop.constants.ResponseCode;
import thn.vn.web.miav.shop.models.entity.TokenApp;
import thn.vn.web.miav.shop.models.entity.UserApp;
import thn.vn.web.miav.shop.models.response.ApiResponse;
import thn.vn.web.miav.shop.models.response.ErrorResponse;
import thn.vn.web.miav.shop.models.response.JwtAuthenticationResponse;
import thn.vn.web.miav.shop.models.response.LoginResponse;
import thn.vn.web.miav.shop.services.DataBaseService;
import thn.vn.web.miav.shop.utils.ParameterSql;
import thn.vn.web.miav.shop.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController

public class TestApi {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenProvider tokenProvider;
    @Autowired
    private DataBaseService dataBaseService;

    @RequestMapping(value = "/api/user/", method = RequestMethod.GET)
    ResponseEntity<List<UserApp>> getApi(){
        return  new ResponseEntity<List<UserApp>>(dataBaseService.getAll(UserApp.class,"role=?",new ParameterSql[]{new ParameterSql(Integer.class,1)}), HttpStatus.OK);
    }
    @RequestMapping(value = "/api/user/{role}", method = RequestMethod.GET)
    ResponseEntity<List<UserApp>> getUser(@PathVariable("role") Integer role){
        return  new ResponseEntity<List<UserApp>>(dataBaseService.getAll(UserApp.class,"role=?",new ParameterSql[]{new ParameterSql(Integer.class,role)}), HttpStatus.OK);
    }
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    ResponseEntity<?> login(@Valid @RequestBody UserApp loginRequest, HttpServletRequest request){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserName(),
                        loginRequest.getPassWord()
                )
        );
        String clientType = request.getHeader("clientType");
        String os = request.getHeader("os");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        TokenApp tokenApp = new TokenApp();
        UserApp userApp = dataBaseService.find(UserApp.class,"email=? or userName=?",new ParameterSql[]{new ParameterSql(String.class,loginRequest.getUserName()),new ParameterSql(String.class,loginRequest.getUserName())});
        String tokenId =Utils.getMD5EncryptedValue(userApp.getId()+ Utils.DateNow(Utils.DATE_FILE_NOW));
        tokenApp.setTokenValue(jwt);
        tokenApp.setTokenId(tokenId);
        if (clientType.equalsIgnoreCase("mobile")) {
            tokenApp.setTokenType(2);
        } else {
            tokenApp.setTokenType(1);
        }
        tokenApp.setUserId(userApp.getId());
        dataBaseService.save(tokenApp);
        ErrorResponse errorResponse = new ErrorResponse(ResponseCode.RESULT_OK,"");
        ApiResponse apiResponse = new ApiResponse(errorResponse,new LoginResponse(userApp.getUserName(),tokenId,userApp.getEmail()));
        return ResponseEntity.ok(apiResponse);
    }
}
