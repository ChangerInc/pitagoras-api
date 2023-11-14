package changer.pitagoras.service;

import changer.pitagoras.model.HistoricoConversao;
import changer.pitagoras.repository.HistoricoConversaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class HistoricoConversaoService {

    @Autowired
    private HistoricoConversaoRepository historicoConversaoRepository;

    public HistoricoConversao salvarHistoricoConversao(HistoricoConversao historico) {
        return historicoConversaoRepository.save(historico);
    }
    public List<HistoricoConversao> buscarHistoricoPorUsuario(UUID usuarioId) {
        // Implemente a lógica para buscar o histórico por usuário
        return historicoConversaoRepository.findByUsuarioId(usuarioId);
    }

}