package changer.pitagoras.service;

import changer.pitagoras.config.VertopalConnector;
import changer.pitagoras.model.Arquivo;
import changer.pitagoras.model.HistoricoConversao;
import changer.pitagoras.model.Usuario;
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
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
public class VertopalService {
    @Autowired
    private RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private HistoricoConversaoService historicoConversaoService;
    @Autowired
    private ArquivoService arquivoService;
    @Autowired
    private UsuarioService usuarioService;
    private Arquivo auxArquivo;
    @Value("${api.access.token}")
    private String accessToken;
    @Value("${api.data.app}")
    private String app;
    private JSONObject jsonObject;
    private JSONObject result;
    private JSONObject output;
    private JSONObject error;
    private UUID user;

    public VertopalService() {
    }

    public String enviarArquivo(MultipartFile file, UUID user) {
        if (file != null && !file.isEmpty()) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("Authorization", "Bearer " + accessToken);

            if (user != null) {
                this.user = user;
            }

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

    public String converterArquivo(String extensao) {
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
            auxArquivo.setExtensao(extensao);
            return requisicao.getBody();
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Você não informou o formato para qual deseja fazer a conversão"
            );
        }
    }

    public String obterUrl() {
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
        processarArquivo(requisicao.getBody());
        return requisicao.getBody();
    }

    public void salvarArquivo(File file, ResponseEntity<byte[]> requisicao) {
        try {
            FileUtils.writeByteArrayToFile(file, Objects.requireNonNull(requisicao.getBody()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processarArquivo(byte[] documento) {
        // Processar resposta
        result = jsonObject.getJSONObject("result");
        output = result.getJSONObject("output");

        // separar o nome da extensão do arquivo
        arquivoService.separarExtensao(output.getString("name"));
        usuarioService.salvarArquivo(
                user,
                arquivoService.salvar(
                        new Arquivo(
                                arquivoService.getNomeAux(),
                                new BigDecimal(output.getLong("size")),
                                arquivoService.getExtensaoAux()
                        )
                )
        );
    }
}