package changer.pitagoras.service;

import changer.pitagoras.config.security.GerenciadorTokenJwt;
import changer.pitagoras.dto.UsuarioAdmDto;
import changer.pitagoras.dto.UsuarioCriacaoDto;
import changer.pitagoras.dto.UsuarioEmailSenhaDto;
import changer.pitagoras.dto.UsuarioNomeEmailDto;
import changer.pitagoras.model.Usuario;
import changer.pitagoras.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import changer.pitagoras.service.UsuarioService;


import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
@SpringBootTest
class UsuarioServiceTest {

    @Autowired
    private UsuarioService service;
    @MockBean
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder encoder;
    @MockBean
    private GerenciadorTokenJwt gerenciadorTokenJwt;
    @MockBean
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    @DisplayName("1.1 Deve retornar os dados do usuário recem criado se ele foi cadastrado com sucesso")
    public void testCriarUsuario() {
        // Dados de teste
        UsuarioCriacaoDto usuarioCriacaoDto = new UsuarioCriacaoDto();
        usuarioCriacaoDto.setNome("leele");
        usuarioCriacaoDto.setEmail("laele@gmail.com");
        usuarioCriacaoDto.setSenha("12345678");

        when(usuarioRepository.existsByEmail(usuarioCriacaoDto.getEmail())).thenReturn(false);
        doAnswer(invocation -> {
            Usuario usuarioSalvo = invocation.getArgument(0);
            usuarioSalvo.setId(UUID.randomUUID());
            return usuarioSalvo;
        }).when(usuarioRepository).save(any(Usuario.class));

        Usuario usuario = service.criar(usuarioCriacaoDto);

        assertEquals(usuario.getNome(), usuarioCriacaoDto.getNome());
        assertEquals(usuario.getEmail(), usuarioCriacaoDto.getEmail());
        assertTrue(encoder.matches(usuarioCriacaoDto.getSenha(), usuario.getSenha()));
        assertEquals(usuario.getPlano(), false);
        assertNotNull(usuario.getDataCriacaoConta());
    }
    @Test
    @DisplayName("1.2 Deve retornar null caso o email já esteja cadastrado")
    public void testEmailExistente(){

        UsuarioCriacaoDto usuarioCriacaoDto = new UsuarioCriacaoDto();
        usuarioCriacaoDto.setNome("segundo");
        usuarioCriacaoDto.setEmail("laeale@gmail.com");
        usuarioCriacaoDto.setSenha("121212");

        when(usuarioRepository.existsByEmail(usuarioCriacaoDto.getEmail())).thenReturn(true);

        Usuario segundoUsuario = service.criar(usuarioCriacaoDto);

        assertNull(segundoUsuario);
    }

    @Test
    @DisplayName("1.3 Deve retornar uma lista de usuarios ao chamar listarUsuarios")
    public void testListarUsuarios(){
        List<Usuario> listaUsuarios = new ArrayList<>();
        listaUsuarios.add(new Usuario());
        listaUsuarios.add(new Usuario());
        listaUsuarios.add(new Usuario());
        when(usuarioRepository.findAll()).thenReturn(listaUsuarios);

        assertNotNull(listaUsuarios);
        assertEquals(listaUsuarios, service.listarUsuarios());
    }

    @Test
    @DisplayName("1.4 Retornar o usuario informando o uuid dele")
    public void testEncontrarUsuario(){
        UUID idProcurado = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setId(idProcurado);

        when(usuarioRepository.findById(idProcurado)).thenReturn(Optional.of(usuario));

        assertEquals(usuario, service.encontrarUsuario(idProcurado));
    }

    @Test
    @DisplayName("1.5 Retornar null caso o usuario não exista pelo uuid")
    public void testEncontrarUsuarioErrado(){
        UUID idProcurado = UUID.randomUUID();

        when(usuarioRepository.findById(idProcurado)).thenReturn(Optional.empty());

        assertNull(service.encontrarUsuario(idProcurado));
    }

    @Test
    @DisplayName("1.6 Deve retornar uma lista de UsuarioAdmDto ao chamar listarUsuariosAdm")
    public void testListarUsuariosAdm(){
        List<UsuarioAdmDto> listaUsuarios = new ArrayList<>();
        listaUsuarios.add(new UsuarioAdmDto(UUID.randomUUID(), "nome", "12345678@email.com", "12345678"));
        listaUsuarios.add(new UsuarioAdmDto(UUID.randomUUID(), "nome", "12345678@email.com", "12345678"));
        listaUsuarios.add(new UsuarioAdmDto(UUID.randomUUID(), "nome", "12345678@email.com", "12345678"));
        when(usuarioRepository.findUsersAdm()).thenReturn(listaUsuarios);

        assertNotNull(listaUsuarios);
        assertEquals(listaUsuarios, service.listarUsuariosAdm());
    }

    @Test
    @DisplayName("1.7 Deve obter a extensão do arquivo corretamente")
    public void testObterExtensaoArquivo(){
        String arquivo = "ola.png";
        assertEquals("png", service.obterExtensaoArquivo(arquivo));
        arquivo = "ola.jpeg";
        assertEquals("jpeg", service.obterExtensaoArquivo(arquivo));
        arquivo = "ola.psd";
        assertEquals("psd", service.obterExtensaoArquivo(arquivo));
        arquivo = "ola.doc";
        assertEquals("doc", service.obterExtensaoArquivo(arquivo));
        arquivo = "ola.pdf";
        assertEquals("pdf", service.obterExtensaoArquivo(arquivo));
        arquivo = "ola.xls";
        assertEquals("xls", service.obterExtensaoArquivo(arquivo));
        arquivo = "ola";
        String finalArquivo = arquivo;
        assertThrows(TypeNotPresentException.class, () -> service.obterExtensaoArquivo(finalArquivo));
    }

    @Test
    @DisplayName("1.8 Deve encontrar um usuário pelo email e senha")
    public void testEncontrarUsuarioPorEmail() {
        UsuarioEmailSenhaDto dto = new UsuarioEmailSenhaDto("email@teste.com", "senha123");
        UsuarioNomeEmailDto expectedUser = new UsuarioNomeEmailDto(UUID.randomUUID(), "Nome", "email@teste.com");

        when(usuarioRepository.buscarUsuarioEmailSenhaDto(dto.getEmail(), dto.getSenha())).thenReturn(Optional.of(expectedUser));

        UsuarioNomeEmailDto result = service.encontrarUsuarioPorEmail(dto);

        assertNotNull(result);
        assertEquals(expectedUser.getEmail(), result.getEmail());
        assertEquals(expectedUser.getNome(), result.getNome());
    }

    @Test
    @DisplayName("1.9 Deve retornar null ao não encontrar um usuário pelo email e senha")
    public void testEncontrarUsuarioPorEmailInexistente() {
        UsuarioEmailSenhaDto dto = new UsuarioEmailSenhaDto("email@inexistente.com", "senha123");

        when(usuarioRepository.buscarUsuarioEmailSenhaDto(dto.getEmail(), dto.getSenha())).thenReturn(Optional.empty());

        UsuarioNomeEmailDto result = service.encontrarUsuarioPorEmail(dto);

        assertNull(result);
    }

    @Test
    @DisplayName("1.10 Deve atualizar a senha do usuário com sucesso")
    public void testUpdateSenha() {
        UUID idUsuario = UUID.randomUUID();
        Map<String, String> senhas = Map.of("senhaAtual", "senha123", "senhaNova", "novaSenha123");
        Usuario usuario = new Usuario();
        usuario.setSenha("senha123");

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(senhas.get("senhaAtual"), usuario.getSenha())).thenReturn(true);
        when(passwordEncoder.encode(senhas.get("senhaNova"))).thenReturn("novaSenha123");

        int resultado = service.update(senhas, idUsuario);

        assertEquals(200, resultado);
    }

    @Test
    @DisplayName("1.11 Deve retornar código de erro ao tentar atualizar senha com senha atual incorreta")
    public void testUpdateSenhaSenhaAtualIncorreta() {
        UUID idUsuario = UUID.randomUUID();
        Map<String, String> senhas = Map.of("senhaAtual", "senhaErrada", "senhaNova", "novaSenha123");
        Usuario usuario = new Usuario();
        usuario.setSenha("senha123");

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(senhas.get("senhaAtual"), usuario.getSenha())).thenReturn(false);

        int resultado = service.update(senhas, idUsuario);

        assertEquals(400, resultado);
    }
}