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

    List<Usuario> usuarios = new ArrayList<>();
    @Autowired
    private UsuarioService usuarioService;

    public UsuarioController() {
    }

    @GetMapping("/")
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @PostMapping("/")
    public ResponseEntity<Usuario> realizarCadastro(
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
    public ResponseEntity<Usuario> realizarLogin(@RequestBody Usuario usuarioLogado) {
        for (Usuario user : usuarios) {
            if (usuarioLogado.getEmail().equals(user.getEmail()) &&
                    usuarioLogado.getSenha().equals(user.getSenha())) {
                return ResponseEntity.status(200).body(user);
            }
        }

        return ResponseEntity.status(404).build();
    }

    @PutMapping("/{novaSenha}")
    public ResponseEntity<Usuario> atualizarSenha(
            @RequestBody Usuario usuario, @PathVariable String novaSenha) {
        Usuario user = realizarLogin(usuario).getBody();

        if (user == null) {
            return ResponseEntity.status(404).build();
        }

        user.atualizarSenha(novaSenha);

        return ResponseEntity.status(200).body(user);
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Usuario> excluirUsuario(@PathVariable UUID uuid) {
        boolean excluiu = usuarios.removeIf(user -> user.getId().equals(uuid));

        int status = excluiu ? 200 : 404;

        return ResponseEntity.status(status).build();
    }
}