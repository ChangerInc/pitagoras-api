package changer.pitagoras.controller;

import changer.pitagoras.dto.CirculoMembrosDto;
import changer.pitagoras.dto.CirculoSimplesDto;
import changer.pitagoras.model.Circulo;
import changer.pitagoras.model.Membro;
import changer.pitagoras.service.CirculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/circulo")
public class CirculoController {
    @Autowired
    CirculoService circuloService;

    @GetMapping("/")
    public ResponseEntity<List<Circulo>> getAll() {
        return circuloService.getAll().isEmpty() ? ResponseEntity.status(204).body(circuloService.getAll())
                : ResponseEntity.status(200).body(circuloService.getAll());
    }

    @GetMapping("/acesso")
    public ResponseEntity<CirculoMembrosDto> getOne(@RequestBody Map<String, UUID> ids) {
        return ResponseEntity.status(200).body(circuloService.getOne(ids));
    }

    @PostMapping("/")
    public ResponseEntity<Circulo> postCirculo(@RequestBody Circulo c) {
        Circulo criado = circuloService.insert(c.getNomeCirculo(), c.getDono().getId());

        return criado == null ? ResponseEntity.status(404).build() : ResponseEntity.status(201).body(criado);
    }

    @PutMapping("/")
    public ResponseEntity<Circulo> putNome(@RequestBody CirculoSimplesDto c) {
        return ResponseEntity.status(204).body(circuloService.alterarNome(c));
    }

    @DeleteMapping("/")
    public ResponseEntity<Circulo> delCirculo(@RequestBody CirculoSimplesDto c) {
        circuloService.deletar(c);

        return ResponseEntity.status(204).build();
    }

    @PostMapping("/adicionar-membro")
    public ResponseEntity<CirculoMembrosDto> adicionarRequestBody(@RequestBody Map<String, UUID> novoMembro) {
        return ResponseEntity.status(201).body(circuloService.addMembro(novoMembro));
    }
}