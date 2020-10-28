package Handlers;

import Result.Result;
import Services.ClearService;

public class ClearHandler extends Handler {
    public ClearHandler() {
        getOrPost = "post";
        authenticate = false;
    }

    @Override
    protected Result workWithService(String requestURI, String reqData) {
        System.out.println(reqData);

        ClearService service = new ClearService();

        return service.clear();
    }
}
