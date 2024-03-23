package changer.pitagoras.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ContratoS3 {

    String saveArquivo(MultipartFile arquivo);

    byte[] downloadArquivo(String nomeArquivo);

    String deleteArquivo(String nomeArquivo);

    List<String> listAllArquivos();
}
