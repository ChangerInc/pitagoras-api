package changer.pitagoras.service;

import changer.pitagoras.model.Arquivo;
import changer.pitagoras.repository.ArquivoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ArquivoService {
    @Autowired
    private ArquivoRepository repository;
    private String extensaoAux;
    private String nomeAux;

    public static String obterExtensaoArquivo(String nomeArquivo) {
        if (!nomeArquivo.contains(".")) {
            throw new TypeNotPresentException("Ponto (.) não encontrado no nome do arquivo", null);
        }

        Path path = Paths.get(nomeArquivo);
        return path.getFileName().toString().substring(path.getFileName().toString().lastIndexOf('.') + 1);
    }

    protected Map<String, String> separarExtensao(String nomeDocumento) {
        StringBuilder extensao = new StringBuilder();
        StringBuilder nome = new StringBuilder();
        Map<String, String> mapa = new HashMap<>();

        boolean ponto = false;
        for (int i = 0; i < nomeDocumento.length(); i++) {
            char charAtual = nomeDocumento.charAt(i);

            if (charAtual == '.') {
                ponto = true;
            }

            if (!ponto) {
                nome.append(charAtual);
            }

            if (ponto && charAtual != '.') {
                extensao.append(charAtual);
            }
        }

        nomeAux = nome.toString();
        extensaoAux = extensao.toString();

        return mapa;
    }

    public Arquivo salvar(MultipartFile file) {
        Arquivo arquivo = new Arquivo();
        arquivo.setIdArquivo(UUID.randomUUID());

        try {
            arquivo.setBytesArquivo(file.getResource().getContentAsByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        arquivo.setNome(file.getOriginalFilename());
        arquivo.setExtensao(obterExtensaoArquivo(file.getOriginalFilename()));
        arquivo.setTamanho(BigDecimal.valueOf(file.getSize()));

        return repository.save(arquivo);
    }

    public Arquivo buscarArquivo(UUID id) {
        return repository.findByIdArquivo(id).orElse(null);
    }
}
