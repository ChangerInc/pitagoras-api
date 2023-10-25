package changer.pitagoras.service;

import changer.pitagoras.config.security.GerenciadorTokenJwt;
import changer.pitagoras.dto.UsuarioCriacaoDto;
import changer.pitagoras.dto.UsuarioMapper;
import changer.pitagoras.dto.UsuarioNomeEmailDto;
import changer.pitagoras.dto.UsuarioEmailSenhaDto;
import changer.pitagoras.dto.autenticacao.UsuarioLoginDto;
import changer.pitagoras.dto.autenticacao.UsuarioTokenDto;
import changer.pitagoras.model.Usuario;
import changer.pitagoras.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import changer.pitagoras.util.ListaObj;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private GerenciadorTokenJwt gerenciadorTokenJwt;
    @Autowired
    private AuthenticationManager authenticationManager;

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario novoUsuario(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            return null;
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario encontrarUsuario(UUID uuid) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(uuid);
        return usuarioOptional.orElse(null);
    }

    public UsuarioEmailSenhaDto encontrarUsuarioPorEmail(UsuarioEmailSenhaDto dto) {
        Optional<UsuarioEmailSenhaDto> usuario =
                usuarioRepository.buscarUsuarioEmailSenhaDto(dto.getEmail(), dto.getSenha());
        System.out.println(usuario);
        return usuario.orElse(null);
    }

    public void deletarUsuario(Usuario user) {
        usuarioRepository.delete(user);
    }


    public int update(Map<String, String> senhas, UUID id) {
        String senhaAtual = senhas.get("senhaAtual");
        String senhaNova = senhas.get("senhaNova");

        if (encontrarUsuario(id) == null)
            return 404;

        if (usuarioRepository.existsBySenhaAndId(passwordEncoder.encode(senhaNova), id))
            return 409;

        if (usuarioRepository.existsBySenhaAndId(passwordEncoder.encode(senhaAtual), id)) {
            usuarioRepository.updateSenha(passwordEncoder.encode(senhaNova), id);
            return 200;
        }

        return 400;
    }

    public UsuarioEmailSenhaDto converterParaUsuarioLoginDTO(String email, String senha) {
        return new UsuarioEmailSenhaDto(email, senha);
    }

    public UsuarioNomeEmailDto converterParaUsuarioSemSenhaDTO(Usuario usuario) {
        return new UsuarioNomeEmailDto(usuario.getNome(), usuario.getEmail());
    }

    public void ordenaPorNome(ListaObj<Usuario> listaObj) {
        for (int i = 0; i < listaObj.getTamanho() - 1; i++) {
            int indMenor = i;
            for (int j = i + 1; j < listaObj.getTamanho(); j++) {
                if (listaObj.getElemento(j).getNome().compareToIgnoreCase(listaObj.getElemento(indMenor).getNome()) < 0) {
                    indMenor = j;
                }
            }
            Usuario aux = listaObj.getElemento(i);
            listaObj.adiciona(i, listaObj.getElemento(indMenor));
            listaObj.adiciona(indMenor, aux);
        }
    }

    public int pesquisaBinaria(ListaObj<Usuario> listaObj, String nome) {
        int inicio = 0;
        int fim = listaObj.getTamanho() - 1;

        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;
            int comp = listaObj.getElemento(meio).getNome().compareToIgnoreCase(nome);

            if (comp == 0) {
                return meio;
            } else if (comp < 0) {
                inicio = meio + 1;
            } else {
                fim = meio - 1;
            }
        }

        return -1;
    }

    public void criar(UsuarioCriacaoDto usuarioCriacaoDto) {
        final Usuario novoUsuario = UsuarioMapper.of(usuarioCriacaoDto);

        String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());
        novoUsuario.setSenha(senhaCriptografada);

        this.usuarioRepository.save(novoUsuario);
    }

    public UsuarioTokenDto autenticar(UsuarioLoginDto usuarioLoginDto) {

        final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                usuarioLoginDto.getEmail(), usuarioLoginDto.getSenha());

        final Authentication authentication = this.authenticationManager.authenticate(credentials);

        Usuario usuarioAutenticado =
                usuarioRepository.findByEmail(usuarioLoginDto.getEmail())
                        .orElseThrow(
                                () -> new ResponseStatusException(404, "Email do usuário não cadastrado", null)
                        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final String token = gerenciadorTokenJwt.generateToken(authentication);

        return UsuarioMapper.of(usuarioAutenticado, token);
    }

}