package changer.pitagoras.service;

import changer.pitagoras.model.Arquivo;
import changer.pitagoras.repository.ArquivoRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class ArquivoService {
    @Autowired
    private ArquivoRepository repository;
    @Getter
    private String extensaoAux;
    @Getter
    private String nomeAux;
    @Autowired
    private S3Service s3Service;

    public Arquivo encontrarArq(UUID id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID não fornecido");
        }

        Arquivo arq = repository.findByIdArquivo(id).orElse(null);

        if (arq == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo não encontrado");
        }

        return arq;
    }

    public void separarExtensao(String nomeDocumento) {
        int dots = 0;
        for (char c : nomeDocumento.toCharArray()){
            if (c == '.') {
                dots++;
            }
        }

        if (dots == 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "O arquivo não possui extensão"
            );
        }
        
        if (dots > 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "O arquivo não pode possuir mais de um ponto no nome"
            );
        }

        if (nomeDocumento != null && nomeDocumento.contains(".")) {
            int lastIndex = nomeDocumento.lastIndexOf(".");
            if (lastIndex != -1 && lastIndex != 0 && lastIndex != nomeDocumento.length() - 1) {
                String extension = nomeDocumento.substring(lastIndex + 1);
                nomeAux = nomeDocumento.toString();
                extensaoAux = extension;
            }
        }
    }

    public Arquivo salvar(MultipartFile file, String urlArquivo) {
        if (file == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Arquivo vazio");
        }
        String[] partes = file.getContentType().split("/");
        String extensao = partes[1];
        Arquivo arquivo = new Arquivo(file.getOriginalFilename(), BigDecimal.valueOf(file.getSize()),
                extensao, urlArquivo);
        return repository.save(arquivo);
    }

    public Arquivo salvar(Arquivo arq) {
        return repository.save(arq);
    }

    public Arquivo buscarArquivo(UUID id) {
        return repository.findByIdArquivo(id).orElse(null);
    }

    public String pegarUrlArquivo(UUID id) {
        return encontrarArq(id).getUrlArquivo();
    }
}
