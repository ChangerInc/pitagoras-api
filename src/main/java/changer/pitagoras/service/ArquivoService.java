package changer.pitagoras.service;

import changer.pitagoras.model.Arquivo;
import changer.pitagoras.model.Circulo;
import changer.pitagoras.model.Usuario;
import changer.pitagoras.repository.ArquivoRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ArquivoService {
    @Autowired
    private ArquivoRepository arquivoRepository;
    @Getter
    private String extensaoAux;
    @Getter
    private String nomeAux;
    @Autowired
    private S3Service s3Service;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private CirculoService circuloService;

    public Arquivo fluxoDeUploadArquivo(UUID idUsuario, MultipartFile file) {
        s3Service.saveArquivo(file, idUsuario);
        String urlArquivo = s3Service.obterUrlPublica(file.getOriginalFilename(), idUsuario.toString());
        Arquivo arquivoModel = transformarMultipartFileEmArquivoModel(file, urlArquivo);
        arquivoRepository.save(arquivoModel);

        Usuario usuario = usuarioService.encontrarUsuario(idUsuario);
        usuario.getArquivos().add(arquivoModel);
        usuarioService.salvarUser(usuario);
        return arquivoModel;
    }

    public Boolean fluxoDeDeleteArquivo(UUID idUsuario, UUID idArquivo){
        if (idUsuario == null || idArquivo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Usuario user = usuarioService.encontrarUsuario(idUsuario);
        if (user == null) {
            return false;
        }
        Arquivo arq = buscarArquivo(idArquivo);
        if (arq == null) {
            return false;
        }
        user.getArquivos().remove(arq);
        usuarioService.salvarUser(user);
        s3Service.deleteArquivo(arq.getNome(), idUsuario);
        return true;
    }

    public Arquivo encontrarArq(UUID id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID não fornecido");
        }

        Arquivo arq = arquivoRepository.findByIdArquivo(id).orElse(null);

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
        Arquivo arquivo = transformarMultipartFileEmArquivoModel(file, urlArquivo);
        return arquivoRepository.save(arquivo);
    }

    public Arquivo salvar(Arquivo arq) {
        return arquivoRepository.save(arq);
    }

    public Arquivo buscarArquivo(UUID id) {
        return arquivoRepository.findByIdArquivo(id).orElse(null);
    }

    public String pegarUrlArquivo(UUID id) {
        return encontrarArq(id).getUrlArquivo();
    }

    public static String obterExtensaoArquivo(String nomeArquivo) {
        if (!nomeArquivo.contains(".")) {
            throw new TypeNotPresentException("Ponto (.) não encontrado no nome do arquivo", null);
        }

        Path path = Paths.get(nomeArquivo);
        return path.getFileName().toString().substring(path.getFileName().toString().lastIndexOf('.') + 1);
    }

    public Arquivo transformarMultipartFileEmArquivoModel(MultipartFile file, String urlArquivo){
        String nomeArquivo = file.getOriginalFilename();
        BigDecimal tamanhoArquivo = BigDecimal.valueOf(file.getSize());
        String extensao = obterExtensaoArquivo(nomeArquivo);

        Arquivo arquivo = new Arquivo(nomeArquivo, tamanhoArquivo, extensao, urlArquivo);
        return arquivo;
    }


    // ======================================= CIRCULO =================================================================

    public Arquivo fluxoDeUploadArquivoNoCirculo(UUID idCirculo, MultipartFile file) {
        s3Service.saveArquivo(file, idCirculo);
        String urlArquivo = s3Service.obterUrlPublica(file.getOriginalFilename(), idCirculo.toString());
        Arquivo arquivoModel = transformarMultipartFileEmArquivoModel(file, urlArquivo);
        arquivoRepository.save(arquivoModel);

        Circulo circulo = circuloService.pegarCirc(idCirculo);
        circulo.getArquivos().add(arquivoModel);
        circuloService.salvarCirculo(circulo);
        return arquivoModel;
    }

    public List<Arquivo> resgatarArquivosDoCirculo(UUID idCirculo) {
        Circulo circulo = circuloService.pegarCirc(idCirculo);
        if (circulo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Circulo não encontrado");
        }
        return circulo.getArquivos();
    }


}
