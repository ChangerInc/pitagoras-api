package changer.pitagoras.controller;

import changer.pitagoras.model.Arquivo;
import changer.pitagoras.service.ArquivoService;
import changer.pitagoras.service.S3Service;
import changer.pitagoras.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/arquivo")
public class ArquivoController {
    @Autowired
    private ArquivoService arquivoService;
    @Autowired
    private S3Service s3Service;
    @Autowired
    private UsuarioService usuarioService;

    // ============================================= USUARIO ===========================================================

    @PostMapping("/{idUsuario}")
    public ResponseEntity<String> uploadArquivo(@PathVariable UUID idUsuario, @RequestParam("file") MultipartFile file) {
        Arquivo arquivo = arquivoService.fluxoDeUploadArquivo(idUsuario, file);
        return ResponseEntity.status(201).body(arquivo.getUrlArquivo());
    }

    @DeleteMapping("/{idUsuario}/{idArquivo}")
    public ResponseEntity<Boolean> removerArquivo(@PathVariable UUID idUsuario, @PathVariable UUID idArquivo) {
        return arquivoService.fluxoDeDeleteArquivo(idUsuario, idArquivo)
                ? ResponseEntity.status(200).build()
                : ResponseEntity.status(404).build();
    }

    @GetMapping("/{idUsuario}/{idArquivo}")
    public ResponseEntity<byte[]> downloadArquivo(@PathVariable UUID idUsuario, @PathVariable UUID idArquivo) {
        Arquivo arquivo = arquivoService.buscarArquivo(idArquivo);
        String nomeArquivo = arquivo.getNome();
        return ResponseEntity.status(200).body(s3Service.downloadArquivo(nomeArquivo, idUsuario));
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<Arquivo>> getArquivosById(
            @PathVariable UUID idUsuario) {
        List<Arquivo> arqs = usuarioService.resgatarArquivos(idUsuario);

        return arqs.isEmpty()
                ? ResponseEntity.status(204).build()
                : ResponseEntity.status(200).body(arqs);
    }


    // ============================================= CIRCULO ===========================================================

    @PatchMapping("/circulo/{idCirculo}/{idArquivo}")
    public ResponseEntity<String> adicionarArquivoNaTurminha
            (@PathVariable UUID idCirculo, @RequestParam("file") MultipartFile file) {

        Arquivo arquivo = arquivoService.fluxoDeUploadArquivoNoCirculo(idCirculo, file);
        return ResponseEntity.status(201).body(arquivo.getUrlArquivo());
    }

    @GetMapping("/circulo/{idCirculo}")
    public ResponseEntity<List<Arquivo>> exibirTodosArquivosDoCirculo(@PathVariable UUID idCirculo) {
        List<Arquivo> arquivos = arquivoService.resgatarArquivosDoCirculo(idCirculo);

        return arquivos.isEmpty()
                ? ResponseEntity.status(204).build()
                : ResponseEntity.status(200).body(arquivos);
    }

    @GetMapping("/circulo/{idCirculo}/{idArquivo}")
    public ResponseEntity<byte[]> downloadArquivoDoCirculo(@PathVariable UUID idCirculo, @PathVariable UUID idArquivo) {
        Arquivo arquivo = arquivoService.buscarArquivo(idArquivo);
        String nomeArquivo = arquivo.getNome();
        return ResponseEntity.status(200).body(s3Service.downloadArquivo(nomeArquivo, idCirculo));
    }

    @DeleteMapping("/circulo/{idCirculo}/{idArquivo}")
    public ResponseEntity<Boolean> removerArquivoDoCIrculo(@PathVariable UUID idCirculo, @PathVariable UUID idArquivo) {
        return arquivoService.fluxoDeDeleteArquivoDoCirculo(idCirculo, idArquivo)
                ? ResponseEntity.status(200).build()
                : ResponseEntity.status(404).build();
    }

}
