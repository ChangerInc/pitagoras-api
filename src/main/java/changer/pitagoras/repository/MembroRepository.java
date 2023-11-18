package changer.pitagoras.repository;

import changer.pitagoras.model.Circulo;
import changer.pitagoras.model.Membro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MembroRepository extends JpaRepository<Membro, UUID> {
    List<Membro> findAllByCirculoEquals(Circulo id);
}