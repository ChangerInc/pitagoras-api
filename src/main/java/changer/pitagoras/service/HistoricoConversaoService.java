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

    public HistoricoConversao registrarConversaoEHistorico(HistoricoConversao resultado) {
    HistoricoConversao historico = new HistoricoConversao();
        historico.setNome(resultado.getNome());
        historico.setTamanho(resultado.getTamanho());
        historico.setExtensaoAnterior(resultado.getExtensaoAnterior());
        historico.setExtensaoAtual(resultado.getExtensaoAtual());
        historico.setLinkDownload(resultado.getLinkDownload());
        historico.setUsuario(resultado.getUsuario());

    // Salvar o histórico no banco de dados
        return salvarHistoricoConversao(historico);
}
    public HistoricoConversao salvarHistoricoConversao(HistoricoConversao historico) {
        return historicoConversaoRepository.save(historico);
    }
    public List<HistoricoConversao> buscarHistoricoPorUsuario(UUID usuarioId) {
        // Implemente a lógica para buscar o histórico por usuário
        return historicoConversaoRepository.findByUsuarioId(usuarioId);
    }


    // Adicione outros métodos conforme necessário, por exemplo, para buscar o histórico por usuário
}