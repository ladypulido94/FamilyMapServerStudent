package Handlers;

import Request.RegisterRequest;
import Result.Result;
import Services.RegisterService;
import com.google.gson.Gson;

public class RegisterHandler extends Handler {
    public RegisterHandler() {
        getOrPost = "post";
        authenticate = false;
    }

    @Override
    protected Result workWithService(String requestURI, String reqData) {
        System.out.println(reqData);

        Gson gson = new Gson();
        RegisterRequest request = gson.fromJson(reqData, RegisterRequest.class);
        RegisterService service = new RegisterService();

        return service.register(request);
    }
}
