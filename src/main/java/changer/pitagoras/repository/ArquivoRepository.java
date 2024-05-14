package changer.pitagoras.repository;

import changer.pitagoras.dto.CirculoPesquisaDto;
import changer.pitagoras.model.Arquivo;
import changer.pitagoras.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ArquivoRepository extends JpaRepository<Arquivo, UUID> {

    Optional<Arquivo> findByIdArquivo(UUID idArquivo);
}