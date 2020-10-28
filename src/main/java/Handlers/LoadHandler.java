package Handlers;

import Request.LoadRequest;
import Result.Result;
import Services.LoadService;
import com.google.gson.Gson;

public class LoadHandler extends Handler{
    public LoadHandler() {
        getOrPost = "post";
        authenticate = false;
    }

    @Override
    protected Result workWithService(String requestURI, String reqData) {
        System.out.println(reqData);

        Gson gson = new Gson();
        LoadRequest request = gson.fromJson(reqData, LoadRequest.class);
        LoadService service = new LoadService();
        return service.load(request);
    }
}
