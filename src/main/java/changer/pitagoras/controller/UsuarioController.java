package changer.pitagoras.controller;

import changer.pitagoras.dto.ConviteDto;
import changer.pitagoras.dto.UsuarioCriacaoDto;
import changer.pitagoras.dto.UsuarioNomeEmailDto;
import changer.pitagoras.dto.autenticacao.UsuarioLoginDto;
import changer.pitagoras.dto.autenticacao.UsuarioTokenDto;
import changer.pitagoras.model.Arquivo;
import changer.pitagoras.model.Circulo;
import changer.pitagoras.model.Convite;
import changer.pitagoras.model.Usuario;
import changer.pitagoras.service.*;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.HashMap;
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
    @Autowired
    private ArquivoService arquivoService;
    @Autowired
    private S3Service s3Service;;
    @Autowired
    private CirculoService circuloService;;

    public UsuarioController() {
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @GetMapping("/{email}")
    public ResponseEntity<Usuario> getByNome(@PathVariable String email) {
        List<Usuario> lista = usuarioService.listarUsuarios();
        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        Usuario userPesquisado = usuarioService.pesquisaBinaria(
                usuarioService.ordenaPorNome(usuarioService.listarUsuarios()), email
        );

        return ResponseEntity.status(200).body(userPesquisado);
    }

    @PostMapping("/")
    public ResponseEntity<Usuario> criar(@RequestBody @Valid UsuarioCriacaoDto usuarioCriacaoDto) {
        Usuario usuario = usuarioService.cadastrarUsuario(usuarioCriacaoDto);
        if (usuario == null)
            return ResponseEntity.status(409).build();

        return ResponseEntity.status(201).body(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioTokenDto> login(@RequestBody @Valid UsuarioLoginDto usuarioLoginDto) {
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
    public ResponseEntity<String> patchFoto(@PathVariable UUID idUsuario, @RequestParam("file") MultipartFile novaFoto){
        s3Service.saveArquivo(novaFoto, idUsuario);
        String urlImagem = s3Service.obterUrlPublica(novaFoto.getOriginalFilename(), idUsuario.toString());
        usuarioService.atualizarFoto(urlImagem, idUsuario);
        return ResponseEntity.status(200).body(urlImagem);
    }

    @GetMapping(value = "/foto/{codigo}")
    public ResponseEntity<String> getFoto(@PathVariable UUID codigo){

        if (usuarioService.fotoExiste(codigo))
            return ResponseEntity.status(200).body(usuarioService.getFoto(codigo));

        return ResponseEntity.status(404).build();
    }

    @GetMapping("/notificacoes/{email}")
    public ResponseEntity<Integer> getQtdNotificacoes(@PathVariable String email){
        Integer notificacoes = usuarioService.buscarNotificacoes(email);
        return ResponseEntity.status(200).body(notificacoes);
    }

    @GetMapping("/convites/{email}")
    public ResponseEntity<List<ConviteDto>> getConvites(@PathVariable String email){
        List<ConviteDto> convites = usuarioService.buscarConvites(email, 0);
        return ResponseEntity.status(200).body(convites);
    }
}