package com.changer.projeto.api.repository;

import com.changer.projeto.api.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UUID, Usuario> {

}
