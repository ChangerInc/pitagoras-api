package changer.pitagoras.service;

import org.springframework.web.reactive.function.client.WebClient;

public class VertopalService {
    WebClient client = WebClient.create("https://api.vertopal.com/v1");
//    UriBuilder<RequestBodySpec> uriBuilder =
//    RequestBodySpec bodySpec = uriSpec
}
