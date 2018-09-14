package thn.vn.web.miav.shop.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
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

import javax.servlet.http.Cookie;
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
    @RequestMapping(value = "/error/api", method = RequestMethod.GET)
    ResponseEntity<?> apiError(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        String tokenId = "";
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            tokenId =  bearerToken.substring(7, bearerToken.length());
        }
        ErrorResponse errorResponse = new ErrorResponse(ResponseCode.ERROR, "hello");
        ApiResponse apiResponse = new ApiResponse(errorResponse, null);
        TokenApp tokenApp = dataBaseService.find(TokenApp.class,"tokenId=?",new ParameterSql[]{new ParameterSql(String.class,tokenId)});
        if (tokenApp == null){
            errorResponse.setErrorCode(ResponseCode.ERROR_NOT_AUTH);
            errorResponse.setMessage("token ERROR_NOT_AUTH");
            return ResponseEntity.ok(apiResponse);
        } else {
            int tokenError = tokenProvider.getErrorToken(tokenId);
            switch (tokenError){
                case 0:
                    break;
                case 1:
                    errorResponse.setErrorCode(ResponseCode.ERROR_TOKEN_INVALID_SIG);
                    errorResponse.setMessage("token khong hop le");
                    break;
                case 2:
                    errorResponse.setErrorCode(ResponseCode.ERROR_TOKEN_INVALID);
                    errorResponse.setMessage("token ERROR_TOKEN_INVALID");
                    break;
                case 3:
                    errorResponse.setErrorCode(ResponseCode.ERROR_TOKEN_EXPIRED);
                    errorResponse.setMessage("token ERROR_TOKEN_EXPIRED");
                    break;
                    default:
                        errorResponse.setErrorCode(ResponseCode.ERROR_NOT_AUTH);
                        errorResponse.setMessage("token ERROR_NOT_AUTH");
                        break;

            }
            return ResponseEntity.ok(apiResponse);

        }
    }
    private String getJwtFromRequest(HttpServletRequest request) {
        String url_filter = request.getRequestURI();
        String bearerToken = request.getHeader("Authorization");


        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

}
