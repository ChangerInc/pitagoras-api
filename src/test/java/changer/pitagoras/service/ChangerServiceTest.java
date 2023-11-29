package changer.pitagoras.service;

import changer.pitagoras.dto.UsuarioTxtDto;
import changer.pitagoras.model.Usuario;
import changer.pitagoras.repository.UsuarioRepository;
import changer.pitagoras.util.GerenciadorDeArquivoTxt;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class ChangerServiceTest {

    @InjectMocks
    private ChangerService service;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ChangerService changerService;

    @Mock
    private GerenciadorDeArquivoTxt gerenciadorDeArquivoTxt;

    @Test
    @DisplayName("3.1 Teste ExportaUsuarioParaCSV com Sucesso")
    void testExportaUsuarioParaCSVComSucesso() throws IOException {
        List<Usuario> usuariosMock = new ArrayList<>();
        Usuario usuario1 = new Usuario();
        usuario1.setId(UUID.randomUUID());
        usuario1.setNome("Usuario Teste 1");
        usuario1.setEmail("teste1@exemplo.com");
        usuario1.setSenha("senha1");
        usuariosMock.add(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setId(UUID.randomUUID());
        usuario2.setNome("Usuario Teste 2");
        usuario2.setEmail("teste2@exemplo.com");
        usuario2.setSenha("senha2");
        usuariosMock.add(usuario2);

        when(usuarioRepository.findAll()).thenReturn(usuariosMock);

        String filename = "usuarios.csv";
        service.exportaUsuarioParaCSV(filename);

        verify(usuarioRepository, times(1)).findAll();

        File file = new File(filename);
        assertTrue(file.exists());

        List<String> lines = Files.readAllLines(file.toPath());
        assertEquals(2, lines.size()); // Verifica se há duas linhas (para dois usuários)
        assertTrue(lines.get(0).contains("Usuario Teste 1"));
        assertTrue(lines.get(1).contains("Usuario Teste 2"));

        file.delete();
    }

}
