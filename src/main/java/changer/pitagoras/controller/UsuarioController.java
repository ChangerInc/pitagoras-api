package changer.pitagoras.controller;

import changer.pitagoras.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import changer.pitagoras.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping({"/usuario"})
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    public UsuarioController() {
    }

    @GetMapping("/")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        List<Usuario> lista = usuarioService.listarUsuarios();

        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(lista);
    }

    @PostMapping("/")
    public ResponseEntity<Usuario> postCadastro(@RequestBody Usuario novoUsuario) {
        for (Usuario user : usuarioService.listarUsuarios()) {
            if (novoUsuario.getEmail().equals(user.getEmail())) {
                return ResponseEntity.status(409).build();
            }
        }

        usuarioService.novoUsuario(novoUsuario);

        return ResponseEntity.status(201).body(novoUsuario);
    }

    @GetMapping({"/login"})
    public ResponseEntity<Usuario> realizarLogin(@RequestBody Usuario usuarioLogado) {
        Usuario user = usuarioService.login(usuarioLogado.getEmail(), usuarioLogado.getSenha());

        if (user == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.status(200).body(user);
    }

    @PutMapping("/{uuid}/{novaSenha}")
    public ResponseEntity<Usuario> atualizarSenha(
            @PathVariable UUID uuid, @PathVariable String novaSenha) {
        Usuario user = usuarioService.encontrarUsuario(uuid).;

        if (user == null) {
            return ResponseEntity.status(404).build();
        }

        user.atualizarSenha(novaSenha);

        return ResponseEntity.status(200).body(user);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Usuario> excluirUsuario(@PathVariable UUID uuid) {
        Usuario encontrado = usuarioService.encontrarUsuario(uuid);

        if (encontrado == null) {
            ResponseEntity.status(404).build();
        }

        usuarioService.deletarUsuario(encontrado);

        return ResponseEntity.status(200).build();
    }
}