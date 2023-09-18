package requisitions;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.util.UriBuilder;

public class WebClientRequisitions {
    WebClient client = WebClient.create("https://api.vertopal.com/v1");
//    UriBuilder<RequestBodySpec> uriBuilder =
//    RequestBodySpec bodySpec = uriSpec
}
