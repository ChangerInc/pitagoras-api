package changer.pitagoras.service;

import changer.pitagoras.config.VertopalConnector;
import changer.pitagoras.util.json.Response;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Service
public class VertopalService {
    @Autowired
    private RestTemplate restTemplate = new RestTemplate();
    @Value("${api.access.token}")
    private String accessToken;
    @Value("${api.data.app}")
    private String app;
    String convertURL = VertopalConnector.CONVERT.getURL();
    String downloadURL = VertopalConnector.DOWNLOAD.getURL();
    String uploadURL = VertopalConnector.UPLOAD.getURL();
    String urlDownload = VertopalConnector.URL.getURL();
    Gson gson = new Gson();
    private Response respostaUpload;
    private Response respostaConversao;
    private Response respostaUrl;

    public VertopalService() {
    }

    public String enviarArquivo(MultipartFile file){
        if (file != null && !file.isEmpty()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("Authorization", "Bearer " + accessToken);

            MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();
            data.add("data", ("{\"app\":\"%s\"}").formatted(app));
            data.add("file", file.getResource());

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(data, headers);
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(uploadURL, HttpMethod.POST, requestEntity, String.class);

            respostaUpload = gson.fromJson(responseEntity.getBody(), Response.class);
            return responseEntity.getBody();
        } else {
            return "Arquivo não selecionado";
        }
    }

    public String converterArquivo(String extensao){
        if (extensao != null && !extensao.isEmpty()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("Authorization", "Bearer " + accessToken);

            MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();
            data.add("data", ("{\n" +
                    "\"app\":\"%s\", \n" +
                    "\"connector\":\"%s\",\n" +
                    "\"parameters\": {\n" +
                    "        \"output\": \"%s\"\n" +
                    "    }\n" +
                    "}").formatted(app, respostaUpload.getResult().getOutput().getConnector(), extensao));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(data, headers);
            ResponseEntity<String> responseEntity =
                    restTemplate.exchange(convertURL, HttpMethod.POST, requestEntity, String.class);

            respostaConversao = gson.fromJson(responseEntity.getBody(), Response.class);
            return responseEntity.getBody();
        } else {
            return "Você não informou o formato para qual deseja fazer a conversão";
        }
    }

    public String obterUrl(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();
        data.add("data", ("{\n" +
                "    \"app\": \"%s\",\n" +
                "    \"connector\": \"%s\"\n" +
                "}").formatted(app, respostaConversao.getResult().getOutput().getConnector()));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(data, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(urlDownload, HttpMethod.POST, requestEntity, String.class);

        respostaUrl = gson.fromJson(responseEntity.getBody(), Response.class);
        return responseEntity.getBody();
    }

    public void recuperarArquivo(String local) {
        obterUrl();
        File file = new File(local+"/"+respostaUrl.getResult().getOutput().getName());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();
        data.add("data", ("{\n" +
                "    \"app\": \"%s\",\n" +
                "    \"connector\": \"%s\"\n" +
                "}").formatted(app, respostaUrl.getResult().getOutput().getConnector()));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(data, headers);
        ResponseEntity<byte[]> responseEntity = restTemplate
                .exchange(respostaUrl.getResult().getOutput().getUrl(), HttpMethod.POST, requestEntity, byte[].class);
        salvarArquivo(file, responseEntity);
    }
    public void salvarArquivo(File file, ResponseEntity<byte[]> responseEntity){
        try {
            FileUtils.writeByteArrayToFile(file, Objects.requireNonNull(responseEntity.getBody()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

