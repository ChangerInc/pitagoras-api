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

//    @ApiOperation(value = "Obter a lista de todos os usuários")
    @GetMapping("/completo")
    public ResponseEntity<Usuario[]> listarUsuarios() {
        List<Usuario> lista = usuarioService.listarUsuarios();
        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        ListaObj<Usuario> listaObj = new ListaObj<>(lista.size());
        for (Usuario usuario : lista) {
            listaObj.adiciona(usuario);
        }

        usuarioService.ordenaPorNome(listaObj);  // Chama o método de ordenação

        int indice = usuarioService.pesquisaBinaria(listaObj, "nomeDoUsuario");  // Chama o método de pesquisa binária

        if (indice != -1) {
            System.out.println("Usuário encontrado: " + listaObj.getElemento(indice));
        } else {
            System.out.println("Usuário não encontrado");
        }

        return ResponseEntity.status(200).body(listaObj.getVetor());
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
    public ResponseEntity<UsuarioNomeEmailDto> atualizarSenha(
            @PathVariable UUID uuid,
            @RequestBody Map<String, String> senhas) {

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