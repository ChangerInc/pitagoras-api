package changer.pitagoras.controller;

import changer.pitagoras.dto.ArquivoApenasBytesDto;
import changer.pitagoras.model.HistoricoConversao;
import changer.pitagoras.service.HistoricoConversaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/historico-conversao")
public class HistoricoConversaoController {

    @Autowired
        private HistoricoConversaoService historicoConversaoService;

    @PostMapping
    public ResponseEntity<HistoricoConversao> criarHistorico(@RequestBody HistoricoConversao historico) {
        return ResponseEntity.ok(historicoConversaoService.salvarHistoricoConversao(historico));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<HistoricoConversao>> listarHistoricoPorUsuario(@PathVariable UUID usuarioId) {
        return new ResponseEntity<>(historicoConversaoService.buscarHistoricoPorUsuario(usuarioId), HttpStatus.OK);
    }

    @GetMapping("/arquivo/{idArquivo}")
    public ResponseEntity<byte[]> pegarBytesArquivoPeloId(@PathVariable UUID idArquivo) {
        ArquivoApenasBytesDto arquivoDto = historicoConversaoService.pegarArquivoBytesPeloId(idArquivo);
        return ResponseEntity.status(200).body(arquivoDto.getBytesArquivo());
    }
}