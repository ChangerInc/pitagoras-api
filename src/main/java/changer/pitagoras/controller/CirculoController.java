package changer.pitagoras.controller;

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
@RequestMapping("/circulos")
public class CirculoController {
    @Autowired
    CirculoService circuloService;

    @GetMapping("/")
    public ResponseEntity<List<Circulo>> getAll() {
        return circuloService.getAll().isEmpty() ? ResponseEntity.status(204).body(circuloService.getAll())
                : ResponseEntity.status(200).body(circuloService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Circulo> getOne(@PathVariable UUID id) {
        if (circuloService.getOne(id).isPresent()) {
            return ResponseEntity.status(200).body(circuloService.getOne(id).get());
        }

        return ResponseEntity.status(404).build();
    }

    @PostMapping("/")
    public ResponseEntity<Circulo> postCirculo(@RequestBody Circulo c) {
        Circulo criado = circuloService.insert(c.getNomeCirculo(), c.getDono().getId());

        return criado == null ? ResponseEntity.status(404).build() : ResponseEntity.status(201).body(criado);
    }

    @PutMapping("/")
    public ResponseEntity<Circulo> putNome(@RequestBody CirculoSimplesDto c) {
        int stts = circuloService.validacao(c.getIdCirc(), c.getIdDono());

        if (stts > 400) {
            return ResponseEntity.status(stts).build();
        }

        return ResponseEntity.status(stts).body(circuloService.alterarNome(c.getNome(), c.getIdCirc()));
    }
    
    @DeleteMapping("/")
    public ResponseEntity<Circulo> delCirculo(@RequestBody CirculoSimplesDto c) {
        int stts = circuloService.validacao(c.getIdCirc(), c.getIdDono());

        if (stts > 400) {
            return ResponseEntity.status(stts).build();
        }

        circuloService.deletar(c.getIdCirc());
        return ResponseEntity.status(stts).build();
    }
}