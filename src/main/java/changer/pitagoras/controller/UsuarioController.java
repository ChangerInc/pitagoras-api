package changer.pitagoras.controller;

import changer.pitagoras.dto.UsuarioCriacaoDto;
import changer.pitagoras.dto.UsuarioNomeEmailDto;
import changer.pitagoras.dto.autenticacao.UsuarioLoginDto;
import changer.pitagoras.dto.autenticacao.UsuarioTokenDto;
import changer.pitagoras.model.Usuario;
import changer.pitagoras.util.ListaObj;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import changer.pitagoras.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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
    @SecurityRequirement(name = "Bearer")
    public ResponseEntity<Void> criar(@RequestBody @Valid UsuarioCriacaoDto usuarioCriacaoDto) {
        this.usuarioService.criar(usuarioCriacaoDto);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioTokenDto> login(@RequestBody UsuarioLoginDto usuarioLoginDto) {
        UsuarioTokenDto usuarioTokenDto = this.usuarioService.autenticar(usuarioLoginDto);

        return ResponseEntity.status(200).body(usuarioTokenDto);
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