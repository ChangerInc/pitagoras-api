package changer.pitagoras.service;

import changer.pitagoras.config.VertopalConnector;
import changer.pitagoras.model.HistoricoConversao;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
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
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Service
public class VertopalService {
    @Autowired
    private RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private HistoricoConversaoService historicoConversaoService;
    @Value("${api.access.token}")
    private String accessToken;
    @Value("${api.data.app}")
    private String app;
    private JSONObject jsonObject;
    private JSONObject result;
    private JSONObject output;
    private JSONObject error;

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
            ResponseEntity<String> requisicao =
                    restTemplate.exchange(VertopalConnector.UPLOAD.getURL(), HttpMethod.POST, requestEntity, String.class);

            jsonObject = new JSONObject(requisicao.getBody());
            return requisicao.getBody();
        } else {
            return "Arquivo não selecionado";
        }
    }

    public String converterArquivo(String extensao){
        if (extensao != null && !extensao.isEmpty()) {
            result = jsonObject.getJSONObject("result");
            output = result.getJSONObject("output");

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
                    "}").formatted(app, output.getString("connector"), extensao));

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(data, headers);
            ResponseEntity<String> requisicao =
                    restTemplate.exchange(VertopalConnector.CONVERT.getURL(), HttpMethod.POST, requestEntity, String.class);

            jsonObject = new JSONObject(requisicao.getBody());
            return requisicao.getBody();
        } else {
            return "Você não informou o formato para qual deseja fazer a conversão";
        }
    }

    public String obterUrl(){
        result = jsonObject.getJSONObject("result");
        output = result.getJSONObject("output");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();
        data.add("data", ("{\n" +
                "    \"app\": \"%s\",\n" +
                "    \"connector\": \"%s\"\n" +
                "}").formatted(app, output.getString("connector")));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(data, headers);
        ResponseEntity<String> requisicao =
                restTemplate.exchange(VertopalConnector.URL.getURL(), HttpMethod.POST, requestEntity, String.class);

        jsonObject = new JSONObject(requisicao.getBody());
        result = jsonObject.getJSONObject("result");
        output = result.getJSONObject("output");

        return output.getString("name");
    }

    public byte[] recuperarArquivo() {
        result = jsonObject.getJSONObject("result");
        output = result.getJSONObject("output");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, Object> data = new LinkedMultiValueMap<>();
        data.add("data", ("{\n" +
                "    \"app\": \"%s\",\n" +
                "    \"connector\": \"%s\"\n" +
                "}").formatted(app, output.getString("connector")));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(data, headers);
        ResponseEntity<byte[]> requisicao = restTemplate
                .exchange(output.getString("url"), HttpMethod.POST, requestEntity, byte[].class);
        System.out.println(requisicao.getBody());
        processarArquivo(requisicao);
        return requisicao.getBody();
    }
    public void salvarArquivo(File file, ResponseEntity<byte[]> requisicao){
        try {
            FileUtils.writeByteArrayToFile(file, Objects.requireNonNull(requisicao.getBody()));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processarArquivo(ResponseEntity<byte[]> response) {

        // Processar resposta
//       jsonObject = new JSONObject(response.getBody());
       result = jsonObject.getJSONObject("result");
       output = result.getJSONObject("output");

        String name = output.getString("name");
        BigDecimal size = new BigDecimal(output.getLong("size"));

        // Salvar informações no banco de dados
        HistoricoConversao historico = new HistoricoConversao();
        historico.setIdConversao(UUID.randomUUID());
        historico.setNome(name);
        historico.setTamanho(size);
        // Definir outros campos necessários, como extensões e link de download

        historicoConversaoService.salvarHistoricoConversao(historico);
    }
}

