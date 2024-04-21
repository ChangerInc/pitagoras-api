package changer.pitagoras.repository;

import changer.pitagoras.dto.ConviteDto;
import changer.pitagoras.model.Convite;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConviteRepository extends JpaRepository<Convite, UUID> {

    @Query("select count(emailConvidado) from Convite where emailConvidado = ?1 and statusConvite = 0")
    Integer consultarQtdNotificacoes(String email);

    @Transactional
    @Modifying
    @Query("UPDATE Convite c SET c.statusConvite = ?1 WHERE c.idCirculo = ?2")
    int mudarStatusConvite(int status, UUID idCirculo);

    @Transactional
    @Modifying
    @Query("UPDATE Convite c SET c.statusConvite = ?1 WHERE c.idCirculo = ?2 AND c.emailConvidado = ?3")
    int mudarStatusConvite(int status, UUID idCirculo, String emailConvidado);

    List<Convite> findAllByEmailConvidadoAndStatusConvite(String emailConvidado, int statusConvite);

}
