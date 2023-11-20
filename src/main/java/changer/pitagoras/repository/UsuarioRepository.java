package changer.pitagoras.repository;

import changer.pitagoras.dto.UsuarioAdmDto;
import changer.pitagoras.dto.UsuarioNomeEmailDto;
import changer.pitagoras.dto.UsuarioTxtDto;
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
    select new changer.pitagoras.dto.UsuarioNomeEmailDto(u.id, u.nome, u.email)
    from Usuario u WHERE u.email = ?1 AND u.senha = ?2
    """)
    Optional<UsuarioNomeEmailDto> buscarUsuarioEmailSenhaDto(@Param("email") String email,
                                                             @Param("senha") String senha);

    Boolean existsByEmail(@Param("email") String email);

    Optional<Usuario> findByEmail(String email);

    Boolean existsBySenhaAndId(String senha, UUID id);

    @Transactional
    @Modifying
    @Query("update Usuario u set u.fotoPerfil = ?1 where u.id = ?2")
    int atualizarFoto(byte[] foto, UUID codigo);

    @Query("select u.fotoPerfil from Usuario u where u.id = ?1")
    byte[] getFoto(UUID codigo);

    @Query("select new changer.pitagoras.dto.UsuarioAdmDto(u.id, u.nome, u.email, u.senha) from Usuario u")
    List<UsuarioAdmDto> findUsersAdm();

    @Query("select new changer.pitagoras.dto.UsuarioTxtDto(u.id as idUsuario, u.nome, u.email, u.senha, u.plano, u.dataCriacaoConta) from Usuario u")
    List<UsuarioTxtDto> findUsuarioTxtDto();
}