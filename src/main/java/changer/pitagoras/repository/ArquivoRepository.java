package changer.pitagoras.repository;

import changer.pitagoras.model.Arquivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ArquivoRepository extends JpaRepository<Arquivo, UUID> {

    Optional<Arquivo> findByIdArquivo(UUID idArquivo);
}