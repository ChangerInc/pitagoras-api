package changer.pitagoras.controller;

import changer.pitagoras.model.Arquivo;
import changer.pitagoras.service.ArquivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/arquivo")
public class ArquivoController {
    @Autowired
    private ArquivoService service;

    @PostMapping("/")
    public ResponseEntity<Arquivo> criarArquivo(@RequestBody Arquivo arq) {
        return ResponseEntity.status(201).body(service.salvar(arq));
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadArquivo(@PathVariable UUID id) {
        return ResponseEntity.status(200).body(service.pegarArquivo(id));
    }
}
