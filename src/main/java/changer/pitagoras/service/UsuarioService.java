package changer.pitagoras.service;

import changer.pitagoras.config.security.GerenciadorTokenJwt;
import changer.pitagoras.dto.*;
import changer.pitagoras.dto.autenticacao.UsuarioLoginDto;
import changer.pitagoras.dto.autenticacao.UsuarioTokenDto;
import changer.pitagoras.model.HistoricoConversao;
import changer.pitagoras.model.Usuario;
import changer.pitagoras.repository.HistoricoConversaoRepository;
import changer.pitagoras.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import changer.pitagoras.util.ListaObj;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
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

    @Autowired
    private HistoricoConversaoRepository historicoConversaoRepository;

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public List<UsuarioAdmDto> listarUsuariosAdm() {
        return usuarioRepository.findUsersAdm();
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

    public UsuarioNomeEmailDto encontrarUsuarioPorEmail(UsuarioEmailSenhaDto dto) {
        Optional<UsuarioNomeEmailDto> usuario =
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

        // Verifique se o usuário existe
        Usuario usuario = encontrarUsuario(id);
        if (usuario == null) {
            return 404; // Não encontrado
        }

        // Verifique se a senha atual está correta
        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            return 400; // Senha atual incorreta
        }

        // Verifique se a nova senha é igual à senha atual
        if (passwordEncoder.matches(senhaNova, usuario.getSenha())) {
            return 409; // Nova senha igual à senha atual
        }

        // Atualize a senha do usuário
        usuarioRepository.updateSenha(passwordEncoder.encode(senhaNova), id);

        return 200; // Atualização bem-sucedida
    }


    public UsuarioEmailSenhaDto converterParaUsuarioLoginDTO(String email, String senha) {
        return new UsuarioEmailSenhaDto(email, senha);
    }

    public UsuarioNomeEmailDto converterParaUsuarioSemSenhaDTO(Usuario usuario) {
        return new UsuarioNomeEmailDto(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }

    public ListaObj<Usuario> ordenaPorNome(List<Usuario> lista) {
        ListaObj<Usuario> listaObj = new ListaObj<>(lista.size());

        for (Usuario usuario : lista) {
            listaObj.adiciona(usuario);
        }

        for (int i = 0; i < listaObj.getTamanho() - 1; i++) {
            int indMenor = i;
            for (int j = i + 1; j < listaObj.getTamanho(); j++) {
                if (listaObj.getElemento(j).getEmail().compareToIgnoreCase(listaObj.getElemento(indMenor).getEmail()) < 0) {
                    indMenor = j;
                }
            }
            Usuario aux = listaObj.getElemento(i);
            listaObj.adiciona(i, listaObj.getElemento(indMenor));
            listaObj.adiciona(indMenor, aux);
        }

        return listaObj;
    }

    public Usuario pesquisaBinaria(ListaObj<Usuario> listaObj, String email) {
        int inicio = 0;
        int fim = listaObj.getTamanho() - 1;

        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;
            int comp = listaObj.getElemento(meio).getEmail().compareToIgnoreCase(email);

            if (comp == 0) {
                return listaObj.getElemento(meio);
            } else if (comp < 0) {
                inicio = meio + 1;
            } else {
                fim = meio - 1;
            }
        }

        return null;
    }

    public Usuario criar(UsuarioCriacaoDto usuarioCriacaoDto) {
        if(usuarioRepository.existsByEmail(usuarioCriacaoDto.getEmail())){
            return null;
        }
        final Usuario novoUsuario = UsuarioMapper.of(usuarioCriacaoDto);
        novoUsuario.setPlano(false);
        novoUsuario.setDataCriacaoConta(LocalDateTime.now());
        String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());
        novoUsuario.setSenha(senhaCriptografada);
        return usuarioRepository.save(novoUsuario);
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

    public int atualizarFoto(byte[] novaFoto, UUID codigo) {
        return usuarioRepository.atualizarFoto(novaFoto, codigo);
    }

    public byte[] getFoto(UUID codigo) {
        return usuarioRepository.getFoto(codigo);
    }

    public boolean fotoExiste(UUID codigo) {
        return usuarioRepository.existsById(codigo);
    }

    public HistoricoConversao salvarArquivo(UUID codigo, MultipartFile file){
        Optional<Usuario> usuario = usuarioRepository.findById(codigo);
       if (usuario.isEmpty()){
           return null;
       }
        HistoricoConversao historicoConversao = new HistoricoConversao();
        historicoConversao.setIdConversao(UUID.randomUUID());
        historicoConversao.setUsuario(usuario.get());
        try {
            historicoConversao.setBytesArquivo(file.getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }
        historicoConversao.setDataConversao(LocalDateTime.now());
        historicoConversao.setNome(file.getOriginalFilename());
        historicoConversao.setExtensaoAtual(obterExtensaoArquivo(file.getOriginalFilename()));
        historicoConversao.setTamanho(BigDecimal.valueOf(file.getSize()));

        return historicoConversaoRepository.save(historicoConversao);
    }


    private static String obterExtensaoArquivo(String nomeArquivo) {
        Path path = Paths.get(nomeArquivo);
        return path.getFileName().toString().contains(".")
                ? path.getFileName().toString().substring(path.getFileName().toString().lastIndexOf('.') + 1)
                : "";
    }
}