package changer.pitagoras.controller;

import changer.pitagoras.model.Circulo;
import changer.pitagoras.service.CirculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/circulos")
public class CirculoController {
    @Autowired
    CirculoService service;

    @GetMapping("/")
    public ResponseEntity<List<Circulo>> getAll() {
        return service.getAll().isEmpty() ? ResponseEntity.status(204).build()
                : ResponseEntity.status(200).body(service.getAll());
    }

    @PostMapping("/")
    public ResponseEntity<Circulo> postCirculo(@RequestBody Circulo c) {

        return ResponseEntity.status(220).build();
    }
}