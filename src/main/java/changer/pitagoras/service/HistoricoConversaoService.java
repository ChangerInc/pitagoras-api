package changer.pitagoras.service;

import changer.pitagoras.dto.ArquivoApenasBytesDto;
import changer.pitagoras.dto.HistoricoMapper;
import changer.pitagoras.model.HistoricoConversao;
import changer.pitagoras.repository.HistoricoConversaoRepository;
import changer.pitagoras.util.PilhaObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
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
        if (usuarioId == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo.");
        }

        List<HistoricoConversao> lista = historicoConversaoRepository.findByUsuarioIdOrderByDataConversao(usuarioId);

        if (lista.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Não há arquivos recentes");
        }

        // Criação da pilha e empilhamento dos elementos
        PilhaObj<HistoricoConversao> pilha = new PilhaObj<>(lista.size());
        for (HistoricoConversao historico : lista) {
            pilha.push(historico);
        }

        // Criação da lista ordenada
        List<HistoricoConversao> listaOrdenada = new ArrayList<>();
        while (!pilha.isEmpty()) {
            listaOrdenada.add(pilha.pop());
        }

        return listaOrdenada;
    }

    public ArquivoApenasBytesDto pegarArquivoBytesPeloId(UUID id){
        if (id == null) {
            throw new IllegalArgumentException("ID do arquivo não pode ser nulo.");
        }

        ArquivoApenasBytesDto arquivo = historicoConversaoRepository
                .findBytesArquivoAndIdConversaoAndNomeArquivoByIdConversao(id);

        if(arquivo == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Arquivo não encontrado");
        }

        return arquivo;
    }
}