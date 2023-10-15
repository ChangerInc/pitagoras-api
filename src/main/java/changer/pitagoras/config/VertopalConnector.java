package changer.pitagoras.config;

public enum VertopalConnector {

    CONVERT("/convert/file"),
    UPLOAD("/upload/file"),
    URL("/download/url");

    private final String uri;
    private static final String BASE_URL = "https://api.vertopal.com/v1";

    VertopalConnector(String uri) {
        this.uri = uri;
    }

    public String getURL() {
        return BASE_URL + uri;
    }
}

