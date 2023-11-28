package changer.pitagoras.controller;

import changer.pitagoras.dto.CirculoMembrosDto;
import changer.pitagoras.dto.CirculoPesquisaDto;
import changer.pitagoras.dto.CirculoSimplesDto;
import changer.pitagoras.dto.NovoMembroDto;
import changer.pitagoras.model.Circulo;
import changer.pitagoras.model.HistoricoConversao;
import changer.pitagoras.service.CirculoService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Boolean> adicionarRequestBody(@RequestBody @Valid NovoMembroDto membroNovo) {
        return ResponseEntity.status(201).body(circuloService.addMembro(membroNovo));
    }

    @PatchMapping("/publicar/{idCirculo}/{idArquivo}")
    public ResponseEntity<Boolean> adicionarArquivoNaTurminha(@PathVariable UUID idCirculo, @PathVariable UUID idArquivo) {
        return circuloService.adicionarArquivoNoGrupo(idCirculo, idArquivo) ? ResponseEntity.status(200).build() : ResponseEntity.status(400).build();
    }

    @DeleteMapping("/excluir/{idCirculo}/{idArquivo}")
    public ResponseEntity<Boolean> removerArquivoNaTurminha(@PathVariable UUID idCirculo, @PathVariable UUID idArquivo) {
        return circuloService.removerArquivoNoGrupo(idCirculo, idArquivo) ? ResponseEntity.status(200).build() : ResponseEntity.status(400).build();
    }

    @GetMapping("/arquivos/{idCirculo}")
    public ResponseEntity<List<HistoricoConversao>> pegarTodosArquivosDoCirculo(@PathVariable UUID idCirculo) {
        List<HistoricoConversao> arquivos = circuloService.resgatarArquivosDoCirculo(idCirculo);

        if(arquivos.isEmpty()){
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(arquivos);
    }

    @GetMapping("/pesquisar/{nomeCirculo}/{idUser}")
    public ResponseEntity<List<CirculoPesquisaDto>> searchByName(
            @PathVariable String nomeCirculo, @PathVariable UUID idUser) {
        List<CirculoPesquisaDto> circulos = circuloService.findByNomeCirculoContaining(nomeCirculo, idUser);

        return ResponseEntity.status(200).body(circulos);
    }
}