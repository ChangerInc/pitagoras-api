package changer.pitagoras.controller;

import changer.pitagoras.dto.CirculoMembrosDto;
import changer.pitagoras.dto.CirculoSimplesDto;
import changer.pitagoras.dto.NovoMembroDto;
import changer.pitagoras.model.Arquivo;
import changer.pitagoras.model.Circulo;
import changer.pitagoras.service.CirculoService;
import changer.pitagoras.service.UsuarioService;
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
    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/")
    public ResponseEntity<List<CirculoMembrosDto>> get() {
        return ResponseEntity.status(200).body(circuloService.getAll());
    }

    @GetMapping("/todos/{idUser}")
    public ResponseEntity<List<CirculoMembrosDto>> todosCircUser(@PathVariable UUID idUser) {
        return ResponseEntity.status(200).body(circuloService.getAllById(idUser));
    }

    @GetMapping("/acesso")
    public ResponseEntity<Circulo> getOne(@RequestBody Map<String, UUID> ids) {
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
    public ResponseEntity<Boolean> adicionarRequestBody(@RequestBody NovoMembroDto membro) {
        return ResponseEntity.status(201).body(circuloService.addMembro(membro));
    }

    // TIREI 1

    @DeleteMapping("/arquivos/{idCirculo}/{idArquivo}")
    public ResponseEntity<Boolean> removerArquivoNaTurminha(@PathVariable UUID idCirculo, @PathVariable UUID idArquivo) {
        return circuloService.removerArquivoNoGrupo(idCirculo, idArquivo)
                ? ResponseEntity.status(200).build()
                : ResponseEntity.status(400).build();
    }

    @GetMapping("/pesquisar/{nomeCirculo}/{idUser}")
    public ResponseEntity<List<CirculoMembrosDto>> searchByName(
            @PathVariable String nomeCirculo, @PathVariable UUID idUser) {
        return ResponseEntity.status(200).body(circuloService.findByNomeCirculoContaining(nomeCirculo, idUser));
    }

    @DeleteMapping("/limpar/{idCirculo}")
    public ResponseEntity<Boolean> removerATurminhaTodaDoCirculo(@PathVariable UUID idCirculo) {
        return circuloService.removerTodosOsMembrosDoCIrculo(idCirculo)
                ? ResponseEntity.status(200).build()
                : ResponseEntity.status(400).build();
    }

    @PostMapping("/convidar/{idCirculo}")
    public ResponseEntity<Boolean> convidarPessoaProCirculo
            (@PathVariable UUID idCirculo, @RequestParam UUID idAnfitriao, @RequestParam String emailDoConvidado) {
        return circuloService.convidarPessoa(idCirculo, idAnfitriao, emailDoConvidado) ?
        ResponseEntity.status(200).build() : ResponseEntity.status(400).build();
    }

    @PatchMapping("/convite/botao/{acaoBotao}")
    public ResponseEntity<Boolean> acaoBotaoDoConvite
            (@RequestParam UUID idCirculo,
             @RequestParam UUID idUsuario,
             @PathVariable Integer acaoBotao){

        circuloService.decisaoConvite(idCirculo, idUsuario, acaoBotao);
        return ResponseEntity.status(200).build();
    }

    @PatchMapping("/sair/{idCirculo}/{idUsuario}")
    public ResponseEntity<Boolean> sairDoCirculo(@PathVariable UUID idCirculo, @PathVariable UUID idUsuario){
        return circuloService.sairDoCirculo(idUsuario, idCirculo) ?
            ResponseEntity.status(200).build() : ResponseEntity.status(404).build();
    }

}