package changer.pitagoras.repository;

import changer.pitagoras.model.Circulo;
import changer.pitagoras.model.Membro;
import changer.pitagoras.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MembroRepository extends JpaRepository<Membro, UUID> {
    List<Membro> findAllByCirculoEquals(Circulo id);
    List<Membro> findAllByMembroEquals(Usuario user);
    Membro findByCirculoAndMembro(Circulo circulo, Usuario membro);
}