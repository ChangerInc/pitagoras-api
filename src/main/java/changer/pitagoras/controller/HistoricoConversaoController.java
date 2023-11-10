package changer.pitagoras.controller;

import changer.pitagoras.model.HistoricoConversao;
import changer.pitagoras.service.HistoricoConversaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/historicoconversao")
public class HistoricoConversaoController {

    @Autowired
    private HistoricoConversaoService historicoConversaoService;

    @PostMapping
    public ResponseEntity<HistoricoConversao> criarHistorico(@RequestBody HistoricoConversao historico) {
        HistoricoConversao novoHistorico = historicoConversaoService.salvarHistoricoConversao(historico);
        return ResponseEntity.ok(novoHistorico);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<HistoricoConversao>> listarHistoricoPorUsuario(@PathVariable UUID usuarioId) {
        List<HistoricoConversao> historicos = historicoConversaoService.buscarHistoricoPorUsuario(usuarioId);
        return new ResponseEntity<>(historicos, HttpStatus.OK);
    }

}