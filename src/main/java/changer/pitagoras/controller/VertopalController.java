package changer.pitagoras.controller;

import changer.pitagoras.service.VertopalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/vertopal")
public class VertopalController {
    @Autowired  
    VertopalService vertopalService = new VertopalService();

    @PostMapping("/enviar")
    public String enviarArquivo(@RequestParam("file") MultipartFile file, @RequestParam Optional<UUID> user){
        return vertopalService.enviarArquivo(file, user.orElse(null));
    }

    @PostMapping("/converter")
    public String converterArquivo(@RequestParam("extensao")String extensao){
        return vertopalService.converterArquivo(extensao);
    }

    @GetMapping("/url")
    public ResponseEntity<String> obterUrl(){
        return ResponseEntity.status(200).body(vertopalService.obterUrl());
    }

    @GetMapping("/baixar")
    public ResponseEntity<byte[]> baixarArquivo(){
        return ResponseEntity.status(200).body(vertopalService.recuperarArquivo());
    }
}
