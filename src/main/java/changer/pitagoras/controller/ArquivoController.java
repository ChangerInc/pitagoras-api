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
    private ArquivoService service;
    @Autowired
    private S3Service s3Service;
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/{idUsuario}")
    public ResponseEntity<Arquivo> criarArquivo(@RequestBody MultipartFile file, @PathVariable UUID idUsuario) {
        String url = s3Service.saveArquivo(file, idUsuario);
        return ResponseEntity.status(201).body(service.salvar(file, url));
    }

    @GetMapping("/{idUsuario}/{idArquivo}")
    public ResponseEntity<byte[]> downloadArquivo(@PathVariable UUID idArquivo,@PathVariable UUID idUsuario) {
        Arquivo arquivo = service.buscarArquivo(idArquivo);
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
}
