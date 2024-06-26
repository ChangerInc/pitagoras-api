package changer.pitagoras.service;

import changer.pitagoras.config.S3Config;
import changer.pitagoras.config.security.GerenciadorTokenJwt;
import changer.pitagoras.dto.*;
import changer.pitagoras.dto.autenticacao.UsuarioLoginDto;
import changer.pitagoras.dto.autenticacao.UsuarioTokenDto;
import changer.pitagoras.model.Arquivo;
import changer.pitagoras.model.Circulo;
import changer.pitagoras.model.Convite;
import changer.pitagoras.model.Usuario;
import changer.pitagoras.repository.CirculoRepository;
import changer.pitagoras.repository.ConviteRepository;
import changer.pitagoras.repository.UsuarioRepository;
import changer.pitagoras.util.FotoPerfilGerador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import changer.pitagoras.util.ListaObj;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import java.io.IOException;

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
    private CirculoRepository circuloRepository;

    @Autowired
    private ConviteRepository conviteRepository;
    @Autowired
    private S3Service s3Service;

    public Usuario salvarUser(Usuario user) {
        return usuarioRepository.save(user);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public List<UsuarioAdmDto> listarUsuariosAdm() {
        return usuarioRepository.findUsersAdm();
    }

    public Usuario encontrarUsuario(UUID uuid) {
        Usuario usuario = usuarioRepository.findById(uuid).orElse(null);

        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }

        return usuario;
    }

    public UsuarioNomeEmailDto encontrarUsuarioPorEmail(UsuarioEmailSenhaDto dto) {
        Optional<UsuarioNomeEmailDto> usuario =
                usuarioRepository.buscarUsuarioEmailSenhaDto(dto.getEmail(), dto.getSenha());
        return usuario.orElse(null);
    }

    public Usuario encontrarUsuarioPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

        if (usuario == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado.");
        }

        return usuario;
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

    public Usuario cadastrarUsuario(UsuarioCriacaoDto usuarioCriacaoDto) {
        Usuario novoUsuario = criar(usuarioCriacaoDto);
        if(novoUsuario == null){
            return null;
        }
        String urlDoAvatar = gerarFoto(novoUsuario);
        atualizarFoto(urlDoAvatar, novoUsuario.getId());
        return novoUsuario;
    }

    public Usuario criar(UsuarioCriacaoDto usuarioCriacaoDto) {
        if (usuarioRepository.existsByEmail(usuarioCriacaoDto.getEmail())) {
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

    public int atualizarFoto(String novaFoto, UUID idUsuario) {
        return usuarioRepository.atualizarFoto(novaFoto, idUsuario);
    }

    public String getFoto(UUID codigo) {
        return usuarioRepository.getFoto(codigo);
    }

    public boolean fotoExiste(UUID codigo) {
        return usuarioRepository.existsById(codigo);
    }

    private byte[] obterBytesDaImagemPadrao() {
        try {
            ClassPathResource resource = new ClassPathResource("perfil-de-usuario.png");
            return Files.readAllBytes(resource.getFile().toPath());
        } catch (IOException e) {
            e.printStackTrace(); // Tratar a exceção adequadamente no seu aplicativo
            return new byte[0]; // Retorna um array vazio em caso de falha na leitura
        }
    }

    public List<Arquivo> pegarArq(UUID id) {
        Usuario usuario = encontrarUsuario(id);
        return usuario.getArquivos();
    }

    public List<Arquivo> resgatarArquivos(UUID id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informações faltando");
        }

        return pegarArq(id);
    }

    public Integer buscarNotificacoes(String email) {
        return conviteRepository.consultarQtdNotificacoes(email);
    }

    public List<ConviteDto> buscarConvites(String email, int statusConvite){
        List<Convite> convites = conviteRepository.findAllByEmailConvidadoAndStatusConvite(email, 0);
        List<ConviteDto> conviteDtos = new ArrayList<>();
        for (Convite con : convites){
            UUID idDoCirculo = con.getIdCirculo();
            Usuario dono = encontrarUsuario(con.getIdAnfitriao());
            CirculoRepository.NomeCirculoProjection projecaoCirculo = circuloRepository.findNomeCirculoById(idDoCirculo);
            String nomeDoCirculo = projecaoCirculo.getNomeCirculo();

            UsuarioRepository.NomeProjection projecaoUser = usuarioRepository.findNomeById(con.getIdAnfitriao());
            String nomeUsuario = projecaoUser.getNome();

            ConviteDto dto = new ConviteDto(dono.getFotoPerfil(), nomeUsuario, nomeDoCirculo, idDoCirculo, con.getDataRegistro());
            conviteDtos.add(dto);
        }
        return conviteDtos;
    }

    public List<Circulo> getGrupos(UUID uuid) {
        return encontrarUsuario(uuid).getCirculos();
    }

    public String gerarFoto(Usuario novoUsuario) {
        String iniciais = FotoPerfilGerador.gerarLetras(novoUsuario.getNome());
        BufferedImage fotoPerfil = FotoPerfilGerador.gerarFotoPerfil(iniciais);
        String urlDoAvatar = "";
        // Convertendo BufferedImage para MultipartFile
        try {
            MultipartFile multipartFile = FotoPerfilGerador.convertBufferedImageToMultipartFile(fotoPerfil, "perfil-de-usuario.png");
            s3Service.saveArquivo(multipartFile, novoUsuario.getId());
            urlDoAvatar = s3Service.obterUrlPublica("perfil-de-usuario.png", novoUsuario.getId().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urlDoAvatar;
    }
}