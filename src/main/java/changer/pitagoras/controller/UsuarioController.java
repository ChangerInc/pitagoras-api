package changer.pitagoras.controller;

import changer.pitagoras.dto.UsuarioNomeEmailDto;
import changer.pitagoras.dto.UsuarioEmailSenhaDto;
import changer.pitagoras.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import changer.pitagoras.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    public UsuarioController() {
    }

    @GetMapping("/completo")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> lista = usuarioService.listarUsuarios();
        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(lista);
    }

    @PostMapping("/")
    public ResponseEntity<Usuario> postCadastro(@RequestBody Usuario novoUsuario) {
        Usuario usuarioCriado = usuarioService.novoUsuario(novoUsuario);
        if (usuarioCriado == null) {
            return ResponseEntity.status(409).build();
        }
        usuarioService.novoUsuario(novoUsuario);
        return ResponseEntity.status(201).body(novoUsuario);
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioEmailSenhaDto> login(@RequestBody UsuarioEmailSenhaDto usuario){
        if(usuarioService.encontrarUsuarioPorEmail(usuario) != null){
            return ResponseEntity.status(200).body(usuario);
        }
        return ResponseEntity.status(401).build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<UsuarioNomeEmailDto> atualizarSenha(@PathVariable UUID uuid, @RequestBody String senha) {
        UsuarioNomeEmailDto usuario =
                usuarioService.converterParaUsuarioSemSenhaDTO(usuarioService.encontrarUsuario(uuid));
        return usuarioService.update(senha, uuid)
                ? ResponseEntity.status(200).body(usuario)
                : ResponseEntity.status(404).build();
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
}