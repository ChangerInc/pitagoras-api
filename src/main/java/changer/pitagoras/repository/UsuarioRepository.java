package changer.pitagoras.repository;

import changer.pitagoras.dto.UsuarioEmailSenhaDto;
import changer.pitagoras.model.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    @Modifying
    @Transactional
    @Query("""
            UPDATE Usuario
            SET senha = :senha WHERE id = :id
            """
    )
    Integer updateSenha(@Param("senha") String senha, @Param("id") UUID id);

    @Query("""
    select new changer.pitagoras.dto.UsuarioEmailSenhaDto(u.email, u.senha)
    from Usuario u WHERE u.email = ?1 AND u.senha = ?2
    """)
    Optional<UsuarioEmailSenhaDto> buscarUsuarioEmailSenhaDto(@Param("email") String email,
                                        @Param("senha") String senha);

    Boolean existsByEmail(@Param("email") String email);

    UsuarioEmailSenhaDto findByEmail(@Param("email") String email);

    Boolean existsBySenhaAndId(@Param("senha") String senha, @Param("id") UUID id);
}