package Handlers;

import Result.Result;
import Services.PersonService;

public class PersonHandler extends Handler{
    public PersonHandler() {
        getOrPost = "get";
        authenticate = true;
    }

    @Override
    protected Result workWithService(String requestURI, String reqData) {
        System.out.println(reqData);

        String[] commands = requestURI.split("/");
        PersonService service = new PersonService();
        if (commands.length>2)
            return service.singlePerson(commands[2], authToken);
        else
            return service.allPersons(authToken);
    }
}
