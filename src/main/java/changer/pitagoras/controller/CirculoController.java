package changer.pitagoras.controller;

import changer.pitagoras.model.Circulo;
import changer.pitagoras.service.CirculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

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

    @PutMapping("/{novo}")
    public ResponseEntity<Circulo> putNome(@PathVariable String novo, @RequestBody Map<UUID, UUID> ids) {
        int stts = circuloService.validacao(ids);

        if (stts > 400) {
            return ResponseEntity.status(stts).build();
        }

        return ResponseEntity.status(stts).body(circuloService.alterarNome(novo, ids.get("idCirculo")));
    }
    
    @DeleteMapping("/")
    public ResponseEntity<Circulo> delCirculo(@RequestBody Map<UUID, UUID> ids) {
        int stts = circuloService.validacao(ids);

        if (stts > 400) {
            return ResponseEntity.status(stts).build();
        }

        circuloService.deletar(ids.get("idCirculo"));
        return ResponseEntity.status(stts).build();
    }
    


    @GetMapping("/downloadUsuariosCSV")
    public ResponseEntity<InputStreamResource> downloadCSV() {
        String filename = "usuarios.csv";
        ListaObj<Usuario> lista = // obtenha sua lista aqui
        exportToCSV(lista, filename);
        
        File file = new File(filename);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + file.getName())
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(resource);
    }

}