package Result;

public class SuccessResult extends Result{

    private String message;

    public SuccessResult(String message){
        this.success = true;
        this.message = message;
    }

    public SuccessResult(){
        this.success = true;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
