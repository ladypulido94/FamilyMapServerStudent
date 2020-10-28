package Handlers;

import Request.LoginRequest;
import Result.Result;
import Services.LoginService;
import com.google.gson.Gson;

public class LoginHandler extends Handler {

    public LoginHandler() {
        getOrPost = "post";
        authenticate = false;
    }

    @Override
    protected Result workWithService(String requestURI, String reqData) {
        System.out.println(reqData);

        Gson gson = new Gson();
        LoginRequest request = gson.fromJson(reqData, LoginRequest.class);
        LoginService service = new LoginService();
        return service.login(request);
    }
}
