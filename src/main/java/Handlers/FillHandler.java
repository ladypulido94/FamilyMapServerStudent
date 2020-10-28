package Handlers;

import Result.Result;
import Services.FillService;
import com.google.gson.Gson;

public class FillHandler extends Handler {

    public FillHandler() {
        getOrPost = "post";
        authenticate = false;
    }
    @Override
    protected Result workWithService(String requestURI, String reqData) {

        System.out.println(reqData);

        String[] commands = requestURI.split("/");
        String username = commands[2];

        Gson gson = new Gson();
        FillService service = new FillService();

        if (commands.length>3)
            return service.fill(username, Integer.parseInt(commands[3]));
        else
            return service.fill(username, 4);
    }
}
