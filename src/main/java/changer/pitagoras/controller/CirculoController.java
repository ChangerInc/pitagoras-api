package changer.pitagoras.controller;

import changer.pitagoras.dto.CirculoMembrosDto;
import changer.pitagoras.dto.CirculoSimplesDto;
import changer.pitagoras.model.Circulo;
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
    public ResponseEntity<List<CirculoMembrosDto>> get() {
        return ResponseEntity.status(200).body(circuloService.getAll());
    }

    @GetMapping("/todos/{idUser}")
    public ResponseEntity<List<CirculoMembrosDto>> todosCircUser(@PathVariable UUID idUser) {
        return ResponseEntity.status(200).body(circuloService.getAllById(idUser));
    }

    @GetMapping("/acesso")
    public ResponseEntity<CirculoMembrosDto> getOne(@RequestBody Map<String, UUID> ids) {
        return ResponseEntity.status(200).body(circuloService.getOne(ids));
    }

    @PostMapping("/")
    public ResponseEntity<CirculoMembrosDto> postCirculo(@RequestBody Circulo c) {
        return ResponseEntity.status(201).body(circuloService.insert(c.getNomeCirculo(), c.getDono().getId()));
    }

    @PatchMapping("/")
    public ResponseEntity<CirculoMembrosDto> patchNome(@RequestBody CirculoSimplesDto c) {
        return ResponseEntity.status(204).body(circuloService.alterarNome(c));
    }

    @DeleteMapping("/")
    public ResponseEntity<Circulo> delCirculo(@RequestBody Map<String, UUID> ids) {
        circuloService.deletar(ids);

        return ResponseEntity.status(204).build();
    }

    @PostMapping("/adicionar-membro")
    public ResponseEntity<CirculoMembrosDto> adicionarRequestBody(@RequestBody Map<String, UUID> novoMembro) {
        return ResponseEntity.status(201).body(circuloService.addMembro(novoMembro));
    }
}