package changer.pitagoras.controller;

import changer.pitagoras.dto.UsuarioCriacaoDto;
import changer.pitagoras.model.Usuario;
import changer.pitagoras.repository.UsuarioRepository;
import changer.pitagoras.service.UsuarioService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class UsuarioControllerTest {

    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder encoder;

    @Test
    @DisplayName("Deve retornar os dados do usuário recem criado conforme ele foi cadastrado")
    public void testCriarUsuario() {
        // Dados de teste
        UsuarioCriacaoDto usuarioCriacaoDto = new UsuarioCriacaoDto();
        usuarioCriacaoDto.setNome("leele");
        usuarioCriacaoDto.setEmail("laele@gmail.com");
        usuarioCriacaoDto.setSenha("çfsdijfsidhg");

        // Configuração do serviço para retornar o objeto criado
        when(usuarioService.criar(any(UsuarioCriacaoDto.class))).thenAnswer(invocation -> {
            UsuarioCriacaoDto dto = invocation.getArgument(0);
            Usuario usuario = new Usuario();
            usuario.setNome(dto.getNome());
            usuario.setEmail(dto.getEmail());
            usuario.setPlano(false);
            usuario.setFotoPerfil(null);
            usuario.setDataCriacaoConta(LocalDateTime.now());
            return usuario;
        });

        Usuario usuario = usuarioService.criar(usuarioCriacaoDto);

        assertEquals(usuario.getNome(), usuarioCriacaoDto.getNome());
        assertEquals(usuario.getEmail(), usuarioCriacaoDto.getEmail());
        assertEquals(usuario.getSenha(), encoder.encode(usuarioCriacaoDto.getSenha()));
        assertEquals(usuario.getPlano(), false);
        assertNull(usuario.getFotoPerfil());
        assertNotNull(usuario.getDataCriacaoConta());
    }
}

