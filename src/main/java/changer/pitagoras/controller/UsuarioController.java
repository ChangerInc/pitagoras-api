package changer.pitagoras.controller;

import changer.pitagoras.config.VertopalConnector;
import changer.pitagoras.dto.UsuarioNomeEmailDto;
import changer.pitagoras.dto.UsuarioEmailSenhaDto;
import changer.pitagoras.model.Usuario;
import changer.pitagoras.util.ListaObj;
import org.springframework.beans.factory.annotation.Autowired;
import changer.pitagoras.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/usuario")
//@Api(value = "Usuários", description = "Operações relacionadas a usuários")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    public UsuarioController() {
    }

    @GetMapping("/completo")
    public ResponseEntity<ListaObj<Usuario>> listarUsuarios() {
        List<Usuario> lista = usuarioService.listarUsuarios();
        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.status(200).body(usuarioService.ordenaPorNome(lista));
    }

    @GetMapping("/{email}")
    public ResponseEntity<Usuario> getByNome(@PathVariable String email) {
        if (listarUsuarios().getStatusCode().value() == 200) {
            Usuario userPesquisado = usuarioService.pesquisaBinaria(
                    usuarioService.ordenaPorNome(usuarioService.listarUsuarios()), email
            );

            if (userPesquisado != null) {
                return ResponseEntity.status(200).body(userPesquisado);
            }
        }

        return ResponseEntity.status(404).build();
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
    public ResponseEntity<UsuarioNomeEmailDto> login(@RequestBody UsuarioEmailSenhaDto usuario) {
        UsuarioNomeEmailDto response = usuarioService.encontrarUsuarioPorEmail(usuario);

        if (response != null) {
            return ResponseEntity.status(200).body(response);
        }
        return ResponseEntity.status(401).build();
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<UsuarioNomeEmailDto> atualizarSenha(
            @PathVariable UUID uuid,
            @RequestBody Map<String, String> senhas) {

        if (senhas.get("senhaAtual") == null || senhas.get("senhaNova") == null) {
            return ResponseEntity.status(400).build();
        }
        int result = usuarioService.update(senhas, uuid);

        if (result == 404) {
            return ResponseEntity.status(result).build();
        }

        UsuarioNomeEmailDto usuario = usuarioService.converterParaUsuarioSemSenhaDTO(
                usuarioService.encontrarUsuario(uuid)
        );

        return result == 200 ? ResponseEntity.status(result).body(usuario) : ResponseEntity.status(result).build();
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
}