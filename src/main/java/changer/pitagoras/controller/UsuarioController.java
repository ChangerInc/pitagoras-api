package changer.pitagoras.controller;

import changer.pitagoras.dto.UsuarioCriacaoDto;
import changer.pitagoras.dto.UsuarioNomeEmailDto;
import changer.pitagoras.dto.autenticacao.UsuarioLoginDto;
import changer.pitagoras.dto.autenticacao.UsuarioTokenDto;
import changer.pitagoras.model.HistoricoConversao;
import changer.pitagoras.model.Usuario;
import changer.pitagoras.service.ChangerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import changer.pitagoras.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/usuario")
//@Api(value = "Usuários", description = "Operações relacionadas a usuários")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ChangerService changerService;

    public UsuarioController() {
    }


    @GetMapping("/{email}")
    public ResponseEntity<Usuario> getByNome(@PathVariable String email) {
        List<Usuario> lista = usuarioService.listarUsuarios();
        if (lista.isEmpty())
            return ResponseEntity.status(204).build();

        Usuario userPesquisado = usuarioService.pesquisaBinaria(
                usuarioService.ordenaPorNome(usuarioService.listarUsuarios()), email
        );

        return ResponseEntity.status(200).body(userPesquisado);
    }

    @PostMapping("/")
    public ResponseEntity<Usuario> criar(@RequestBody @Valid UsuarioCriacaoDto usuarioCriacaoDto) {
        Usuario usuario = usuarioService.criar(usuarioCriacaoDto);
        if (usuario == null)
            return ResponseEntity.status(409).build();

        return ResponseEntity.status(201).body(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioTokenDto> login(@RequestBody UsuarioLoginDto usuarioLoginDto) {
        UsuarioTokenDto usuarioTokenDto = this.usuarioService.autenticar(usuarioLoginDto);

        return ResponseEntity.status(200).body(usuarioTokenDto);
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<?> atualizarSenha(
            @PathVariable UUID uuid,
            @RequestBody Map<String, String> senhas) {

        if (senhas == null || !senhas.containsKey("senhaAtual") || !senhas.containsKey("senhaNova")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        int result = usuarioService.update(senhas, uuid);

        HttpStatus httpStatus;

        if (result == 200) {
            UsuarioNomeEmailDto usuario = usuarioService.converterParaUsuarioSemSenhaDTO(usuarioService.encontrarUsuario(uuid));
            httpStatus = HttpStatus.OK;
            return ResponseEntity.status(httpStatus).body(usuario);
        } else {
            httpStatus = HttpStatus.valueOf(result);
            return ResponseEntity.status(httpStatus).build();
        }
    }


    @DeleteMapping("/{uuid}")
    public ResponseEntity<Usuario> excluirUsuario(@PathVariable UUID uuid) {
        Usuario encontrado = usuarioService.encontrarUsuario(uuid);
        if (encontrado == null) {
            return ResponseEntity.status(404).build();
        }
        usuarioService.deletarUsuario(encontrado);
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/home/{id}")
    public ResponseEntity<UsuarioNomeEmailDto> chamarUsuarioPorIdSemSenha(@PathVariable UUID id) {
        UsuarioNomeEmailDto usuario =
                usuarioService.converterParaUsuarioSemSenhaDTO(usuarioService.encontrarUsuario(id));
        if (usuario == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.status(200).body(usuario);
    }

    @SneakyThrows
    @PatchMapping(value = "/foto/{codigo}")
    public ResponseEntity<byte[]> patchFoto(@PathVariable UUID codigo, @RequestParam("file") MultipartFile novaFoto){
        int atualizado = usuarioService.atualizarFoto(novaFoto.getBytes(), codigo);
        int status = atualizado == 1 ? 200 : 404;

        return ResponseEntity.status(status).body(novaFoto.getBytes());
    }

    @GetMapping(value = "/foto/{codigo}", produces = "image/**")
    public ResponseEntity<byte[]> getFoto(@PathVariable UUID codigo){

        if (usuarioService.fotoExiste(codigo))
            return ResponseEntity.status(200).body(usuarioService.getFoto(codigo));

        return ResponseEntity.status(404).build();
    }

    @PostMapping(value = "/upload/{codigo}")
    public ResponseEntity<UUID> uploadArquivo(@PathVariable UUID codigo, @RequestParam("file") MultipartFile file){

        HistoricoConversao atualizado = usuarioService.salvarArquivo(codigo, file);
        if (atualizado == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.status(200).body(atualizado.getIdConversao());

    }

    @DeleteMapping("/excluir/{codigo}/{idConversao}")
    public ResponseEntity<Boolean[]> removerArquivo(@PathVariable UUID codigo, @PathVariable UUID idConversao){
        System.out.println(codigo + "\n" + idConversao);
        Boolean atualizado = usuarioService.deletarArquivo(codigo, idConversao);
        if (!atualizado) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.status(200).build();

    }
}