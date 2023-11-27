package changer.pitagoras.service;

import changer.pitagoras.config.security.GerenciadorTokenJwt;
import changer.pitagoras.dto.UsuarioAdmDto;
import changer.pitagoras.dto.UsuarioCriacaoDto;
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


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Test
    @DisplayName("Deve retornar os dados do usuário recem criado se ele foi cadastrado com sucesso")
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
        assertNull(usuario.getFotoPerfil());
        assertNotNull(usuario.getDataCriacaoConta());
    }
    @Test
    @DisplayName("Deve retornar null caso o email já esteja cadastrado")
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
    @DisplayName("Deve retornar uma lista de usuarios ao chamar listarUsuarios")
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
    @DisplayName("Retornar o usuario informando o uuid dele")
    public void testEncontrarUsuario(){
        UUID idProcurado = UUID.randomUUID();
        Usuario usuario = new Usuario();
        usuario.setId(idProcurado);

        when(usuarioRepository.findById(idProcurado)).thenReturn(Optional.of(usuario));

        assertEquals(usuario, service.encontrarUsuario(idProcurado));
    }

    @Test
    @DisplayName("Retornar null caso o usuario não exista pelo uuid")
    public void testEncontrarUsuarioErrado(){
        UUID idProcurado = UUID.randomUUID();

        when(usuarioRepository.findById(idProcurado)).thenReturn(Optional.empty());

        assertNull(service.encontrarUsuario(idProcurado));
    }

    @Test
    @DisplayName("Deve retornar uma lista de UsuarioAdmDto ao chamar listarUsuariosAdm")
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
    @DisplayName("Deve obter a extensão do arquivo corretamente")
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
}