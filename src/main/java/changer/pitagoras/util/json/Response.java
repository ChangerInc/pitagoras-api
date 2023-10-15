package changer.pitagoras.util.json;

public class Response {
    private Result result;
    private Error error;
    private Warning warning;

    public Response(Result result, Error error, Warning warning) {
        this.result = result;
        this.error = error;
        this.warning = warning;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Warning getWarning() {
        return warning;
    }

    public void setWarning(Warning warning) {
        this.warning = warning;
    }
}