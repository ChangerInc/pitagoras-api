package changer.pitagoras.service;

import changer.pitagoras.dto.UsuarioCriacaoDto;
import changer.pitagoras.model.Usuario;
import changer.pitagoras.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = UsuarioService.class)
class UsuarioServiceTest {
    @Autowired
    UsuarioService service;
    @Autowired
    PasswordEncoder passwordEncoder;
    @MockBean
    UsuarioRepository repository;

    @Test
    void validaSeCadastrarUsuarioDevolveOsDadosCorretamente(){
        Usuario usuario = new Usuario();
        usuario.setNome("Leone");
        usuario.setEmail("lele@gmail.com");
        usuario.setSenha(passwordEncoder.encode("12345678"));

        UsuarioCriacaoDto dadosUser = new UsuarioCriacaoDto();

    }
}