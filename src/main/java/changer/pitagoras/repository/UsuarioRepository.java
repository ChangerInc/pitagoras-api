package changer.pitagoras.repository;

import changer.pitagoras.dto.UsuarioSimplesDto;
import changer.pitagoras.model.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    @Query("""
            SELECT new changer.pitagoras.dto.UsuarioSimplesDto(u.email, u.senha)
            FROM
            Usuario AS u WHERE u.email = :email AND u.senha = :senha
            """
    )
    UsuarioSimplesDto login(@Param("email") String email, @Param("senha") String senha);

    @Query("""
            SELECT new changer.pitagoras.dto.UsuarioSimplesDto(u.email, u.senha)
            FROM
            Usuario AS u WHERE u.email = ?1
            """
    )
    boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query("""
            UPDATE Usuario
            SET senha = :senha WHERE id = :id
            """
    )
    int updateSenha(@Param("senha") String senha, @Param("id") UUID id);
}