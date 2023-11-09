package changer.pitagoras.repository;

import changer.pitagoras.model.Membro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MembroRepository extends JpaRepository<Membro, UUID> {
    /*@Query("SELECT m FROM Membro m WHERE m.id_circulo = :circuloId")
    List<Membro> findAllByCirculoEquals(@Param("circuloId") UUID circuloId);*/
}