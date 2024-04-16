package changer.pitagoras.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ContratoS3 {

    String saveArquivo(MultipartFile arquivo, UUID idUsuario);

    byte[] downloadArquivo(String nomeArquivo, UUID idUsuario);

    String deleteArquivo(String nomeArquivo, UUID idUsuario);

    List<String> listAllArquivos();
}
