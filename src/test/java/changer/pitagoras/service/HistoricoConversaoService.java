package changer.pitagoras.service;


import changer.pitagoras.dto.ArquivoApenasBytesDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HistoricoConversaoServiceTest {

    @Mock
    private HistoricoConversaoRepository historicoConversaoRepository;

    @MockBean
    private UsuarioService usuarioService;

    @InjectMocks
    private HistoricoConversaoService historicoConversaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("2.1Testar salvarHistoricoConversao")
    void testSalvarHistoricoConversao() {
        HistoricoConversao historico = new HistoricoConversao();
        when(historicoConversaoRepository.save(any(HistoricoConversao.class))).thenReturn(historico);

        HistoricoConversao resultado = historicoConversaoService.salvarHistoricoConversao(historico);

        assertNotNull(resultado);
        verify(historicoConversaoRepository, times(1)).save(historico);
    }

    @Test
    @DisplayName("2.2 Testar buscarHistoricoPorUsuario com histórico existente")
    void testBuscarHistoricoPorUsuarioExistente() {
        UUID usuarioId = UUID.randomUUID();
        List<HistoricoConversao> historicos = Arrays.asList(new HistoricoConversao(), new HistoricoConversao());
        when(historicoConversaoRepository.findByUsuarioId(usuarioId)).thenReturn(historicos);

        List<HistoricoConversao> resultado = historicoConversaoService.buscarHistoricoPorUsuario(usuarioId);

        assertFalse(resultado.isEmpty());
        assertEquals(2, resultado.size());
        verify(historicoConversaoRepository, times(1)).findByUsuarioId(usuarioId);
    }

    @Test
    @DisplayName("2.3 Testar buscarHistoricoPorUsuario sem histórico")
    void testBuscarHistoricoPorUsuarioSemHistorico() {
        UUID usuarioId = UUID.randomUUID();
        when(historicoConversaoRepository.findByUsuarioId(usuarioId)).thenReturn(Arrays.asList());

        assertThrows(ResponseStatusException.class, () -> {
            historicoConversaoService.buscarHistoricoPorUsuario(usuarioId);
        });
    }

    @Test
    @DisplayName("2.4 Testar pegarArquivoBytesPeloId com arquivo existente")
    void testPegarArquivoBytesPeloIdExistente() {
        UUID id = UUID.randomUUID();
        String nomeArquivo = "arquivo_teste.pdf";
        byte[] conteudo = new byte[]{ /* bytes do arquivo */ };

        ArquivoApenasBytesDto arquivoDto = new ArquivoApenasBytesDto(id, nomeArquivo, conteudo);
        when(historicoConversaoRepository.findBytesArquivoAndIdConversaoAndNomeArquivoByIdConversao(id)).thenReturn(arquivoDto);

        ArquivoApenasBytesDto resultado = historicoConversaoService.pegarArquivoBytesPeloId(id);

        assertNotNull(resultado);
        assertEquals(nomeArquivo, resultado.getNomeArquivo());
        assertEquals(id, resultado.getIdConversao());
        verify(historicoConversaoRepository, times(1)).findBytesArquivoAndIdConversaoAndNomeArquivoByIdConversao(id);
    }
    @Test
    @DisplayName("2.5 Deve lançar ResponseStatusException quando o arquivo não é encontrado")
    void testPegarArquivoBytesPeloIdNaoEncontrado() {
        UUID id = UUID.randomUUID();

        when(historicoConversaoRepository.findBytesArquivoAndIdConversaoAndNomeArquivoByIdConversao(id)).thenReturn(null);

        assertThrows(ResponseStatusException.class, () -> {
            historicoConversaoService.pegarArquivoBytesPeloId(id);
        });
    }

    @Test
    @DisplayName("2.6 Testar salvarHistoricoConversao com entrada inválida")
    void testSalvarHistoricoConversaoEntradaInvalida() {
        HistoricoConversao historicoInvalido = null;

        assertThrows(IllegalArgumentException.class, () -> {
            historicoConversaoService.salvarHistoricoConversao(historicoInvalido);
        });
    }
    @Test
    @DisplayName("2.7 Testar buscarHistoricoPorUsuario com ID de usuário inválido")
    void testBuscarHistoricoPorUsuarioIdInvalido() {
        UUID usuarioIdInvalido = null;

        assertThrows(IllegalArgumentException.class, () -> {
            historicoConversaoService.buscarHistoricoPorUsuario(usuarioIdInvalido);
        });
    }
    @Test
    @DisplayName("2.8 Testar pegarArquivoBytesPeloId com ID de arquivo inválido")
    void testPegarArquivoBytesPeloIdInvalido() {
        UUID idInvalido = null;

        assertThrows(IllegalArgumentException.class, () -> {
            historicoConversaoService.pegarArquivoBytesPeloId(idInvalido);
        });
    }
    @Test
    @DisplayName("2.9 Testar comportamento em caso de falha do repositório")
    void testFalhaRepositorio() {
        HistoricoConversao historico = new HistoricoConversao();
        when(historicoConversaoRepository.save(any(HistoricoConversao.class))).thenThrow(new RuntimeException("Erro simulado"));

        assertThrows(RuntimeException.class, () -> {
            historicoConversaoService.salvarHistoricoConversao(historico);
        });
    }

}
