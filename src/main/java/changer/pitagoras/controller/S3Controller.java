package changer.pitagoras.controller;

import changer.pitagoras.config.S3Config;
import changer.pitagoras.model.CredenciaisS3;
import changer.pitagoras.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Properties;
import java.util.UUID;

@RestController
@RequestMapping("/s3")
public class S3Controller {

    @Autowired
    private S3Service s3Service;;
    @Autowired
    private S3Config s3Config;

    @PostMapping("/upload")
    public String upload(@RequestParam("arquivo") MultipartFile arquivo,
                         @RequestParam UUID idUsuario){
        return s3Service.saveArquivo(arquivo, idUsuario);
    }

    @GetMapping("/download/{nomeArquivo}")
    public ResponseEntity<byte[]> download(@PathVariable("nomeArquivo") String nomeArquivo,
                                           @RequestParam UUID idUsuario){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", MediaType.ALL_VALUE);
        headers.add("Content-Disposition", "attachment; nomeArquivo="+nomeArquivo);
        byte[] bytes = s3Service.downloadArquivo(nomeArquivo, idUsuario);
        return  ResponseEntity.status(200).headers(headers).body(bytes);
    }

    @GetMapping("/url/{nomeArquivo}")
    public ResponseEntity<String> url(@PathVariable("nomeArquivo") String nomeArquivo,
                                           @RequestParam UUID idUsuario){
        String urlImagem = s3Service.obterUrlPublica(nomeArquivo, idUsuario.toString());
        return  ResponseEntity.status(200).body(urlImagem);
    }


    @DeleteMapping("/delete/{nomeArquivo}")
    public  String deleteArquivo(@PathVariable("nomeArquivo") String nomeArquivo,
                                 @RequestParam UUID idUsuario){
        return s3Service.deleteArquivo(nomeArquivo, idUsuario);
    }

    @GetMapping("/list")
    public List<String> getAllArquivos(){
        return s3Service.listAllArquivos();
    }

    @PostMapping("/update-credentials")
    public String updateCredentials(@RequestBody CredenciaisS3 request) throws IOException {
        Properties properties = new Properties();
        properties.setProperty("accessKey", request.getAccessKey());
        properties.setProperty("secret", request.getSecret());
        properties.setProperty("region", request.getRegion());
        properties.setProperty("bucketName", request.getBucketName());
        properties.setProperty("sessionToken", request.getSessionToken());

        try (FileOutputStream outputStream = new FileOutputStream("src/main/resources/s3.properties")) {
            properties.store(outputStream, null);
        }

        s3Config.setAccessKey(request.getAccessKey());
        s3Config.setSecret(request.getSecret());
        s3Config.setRegion(request.getRegion());
        s3Config.setBucketName(request.getBucketName());
        s3Config.setSessionToken(request.getSessionToken());

        return "Credenciais atualizadas com sucesso.";
    }

    @GetMapping("/credentials")
    public CredenciaisS3 getCredentials() {
        return new CredenciaisS3(s3Config.getAccessKey(), s3Config.getSecret(), s3Config.getRegion(), s3Config.getBucketName(), s3Config.getSessionToken());
    }
}
