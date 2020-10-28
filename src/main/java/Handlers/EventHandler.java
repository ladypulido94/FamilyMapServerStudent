package Handlers;

import Result.Result;
import Services.EventService;

public class EventHandler extends Handler{

    public EventHandler() {
        getOrPost = "get";
        authenticate = true;
    }
    @Override
    protected Result workWithService(String requestURI, String reqData) {
        System.out.println(reqData);

        String[] commands = requestURI.split("/");
        EventService service = new EventService();

        if (commands.length>2) {
            return service.singleEvent(commands[2], authToken);
        }
        else {
            return service.allEvents(authToken);
        }
    }
}
