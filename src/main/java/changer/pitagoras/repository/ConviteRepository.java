package changer.pitagoras.repository;

import changer.pitagoras.dto.ConviteDto;
import changer.pitagoras.model.Convite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConviteRepository extends JpaRepository<Convite, UUID> {

    @Query("select count(emailConvidado) from Convite where emailConvidado = ?1 and statusConvite = 0")
    Integer consultarQtdNotificacoes(String email);

    @Query("SELECT DISTINCT new changer.pitagoras.dto.ConviteDto(u.fotoPerfil, u.nome, cir.nomeCirculo) " +
            "FROM Usuario u " +
            "JOIN Circulo cir ON u.id = cir.dono.id " +
            "JOIN Convite con ON con.idAnfitriao = cir.dono.id " +
            "WHERE con.emailConvidado = ?1 AND con.statusConvite = 0")
    List<ConviteDto> findConviteInfoByEmailConvidado(String emailConvidado);

}
