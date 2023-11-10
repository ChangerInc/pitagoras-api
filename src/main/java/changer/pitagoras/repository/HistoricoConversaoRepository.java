package changer.pitagoras.repository;

import changer.pitagoras.model.HistoricoConversao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface HistoricoConversaoRepository extends JpaRepository<HistoricoConversao, UUID> {
    List<HistoricoConversao> findByUsuarioId(UUID usuarioId);

}