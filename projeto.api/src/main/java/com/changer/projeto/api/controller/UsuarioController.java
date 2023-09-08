package com.changer.projeto.api.controller;

import com.changer.projeto.api.Usuario;
import com.changer.projeto.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping({"/usuario"})
public class UsuarioController {

    List<Usuario> usuarios = new ArrayList<>();
    @Autowired
    UsuarioRepository usuarioRepository;

    public UsuarioController() {
    }

    @GetMapping({"/"})
    public ResponseEntity <List<Usuario>> getUsuarios() {
        if (usuarios.isEmpty()) {
//            usuarioRepository.
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(usuarios);
    }

    @PostMapping("/")
    public ResponseEntity <Usuario> realizarCadastro(
            @RequestBody Usuario novoUsuario) {
        for (Usuario user : usuarios) {
            if (novoUsuario.getEmail().equals(user.getEmail())) {
                return ResponseEntity.status(409).build();
            }
        }
        usuarios.add(novoUsuario);
        return ResponseEntity.status(201).body(novoUsuario);
    }

    @GetMapping({"/login"})
    public ResponseEntity <Usuario> realizarLogin(
            @RequestBody Usuario usuarioLogado) {
        for (Usuario user : usuarios) {
            if (usuarioLogado.getEmail().equals(user.getEmail()) &&
                    usuarioLogado.getSenha().equals(user.getSenha())) {
                return ResponseEntity.status(200).body(user);
            }
        }
        return ResponseEntity.status(204).build();
    }

    @PutMapping("/{novaSenha}")
    public ResponseEntity <Usuario> atualizarSenha(
            @RequestBody Usuario usuario, @PathVariable String novaSenha){
        for (Usuario user : usuarios) {
            if (usuario.getEmail().equals(user.getEmail()) &&
                    usuario.getSenha().equals(user.getSenha())) {
                user.atualizarSenha(novaSenha);
                return ResponseEntity.status(200).body(user);
            }
        }
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping("/")
    public ResponseEntity <Usuario> excluirUsuario(@RequestBody Usuario usuario){
        for (Usuario user : usuarios) {
            if (usuario.getEmail().equals(user.getEmail()) &&
                    usuario.getSenha().equals(user.getSenha())) {
                usuarios.remove(user);
                return ResponseEntity.status(200).body(user);
            }
        }
        return ResponseEntity.status(500).build();
    }
}
