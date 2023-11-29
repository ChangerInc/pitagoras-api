package changer.pitagoras.service;

import changer.pitagoras.dto.ArquivoApenasBytesDto;
import changer.pitagoras.dto.HistoricoMapper;
import changer.pitagoras.model.HistoricoConversao;
import changer.pitagoras.repository.HistoricoConversaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class HistoricoConversaoService {

    @Autowired
    private HistoricoConversaoRepository historicoConversaoRepository;


    public HistoricoConversao salvarHistoricoConversao(HistoricoConversao historico) {
        if (historico == null) {
            throw new IllegalArgumentException("O histórico não pode ser nulo.");
        }
        return historicoConversaoRepository.save(historico);
    }
    public List<HistoricoConversao> buscarHistoricoPorUsuario(UUID usuarioId) {
        List<HistoricoConversao> lista = historicoConversaoRepository.findByUsuarioId(usuarioId);
        if (usuarioId == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo.");
        }
        if (lista.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(204), "Não há arquivos recentes");
        }

        // Implemente a lógica para buscar o histórico por usuário
        return lista;
    }

    public ArquivoApenasBytesDto pegarArquivoBytesPeloId(UUID id){
        ArquivoApenasBytesDto arquivo = historicoConversaoRepository.findBytesArquivoAndIdConversaoAndNomeArquivoByIdConversao(id);
        if (id == null) {
            throw new IllegalArgumentException("ID do arquivo não pode ser nulo.");
        }
        if(arquivo == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo não encontrado");
        }
        return arquivo;
    }

}