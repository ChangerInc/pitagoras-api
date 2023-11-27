package changer.pitagoras.service;

import changer.pitagoras.dto.ArquivoApenasBytesDto;
import changer.pitagoras.dto.HistoricoMapper;
import changer.pitagoras.model.HistoricoConversao;
import changer.pitagoras.repository.HistoricoConversaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        return historicoConversaoRepository.save(historico);
    }
    public List<HistoricoConversao> buscarHistoricoPorUsuario(UUID usuarioId) {
        List<HistoricoConversao> lista = historicoConversaoRepository.findByUsuarioId(usuarioId);

        if (lista.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(204), "Não há arquivos recentes");
        }

        // Implemente a lógica para buscar o histórico por usuário
        return lista;
    }

    public ArquivoApenasBytesDto pegarArquivoBytesPeloId(UUID id){
        ArquivoApenasBytesDto arquivo = historicoConversaoRepository.findBytesArquivoAndIdConversaoAndNomeArquivoByIdConversao(id);
        if(arquivo == null){
            return null;
        }
        return arquivo;
    }

}