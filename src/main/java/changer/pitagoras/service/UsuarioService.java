package changer.pitagoras.service;

import changer.pitagoras.dto.UsuarioSimplesDto;
import changer.pitagoras.model.Usuario;
import changer.pitagoras.repository.UsuarioRepository;
import changer.pitagoras.util.Criptograma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario novoUsuario(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            return null;
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario encontrarUsuario(UUID uuid) {
        return usuarioRepository.getReferenceById(uuid);
    }

    public void deletarUsuario(Usuario user) {
        usuarioRepository.delete(user);
    }

    public UsuarioSimplesDto login(String email, String senha) {
        return usuarioRepository.login(email, senha);
    }

    public boolean update(String senha, UUID id) {
        return usuarioRepository.updateSenha(Criptograma.encrypt(senha), id) == 0 ? false : true;
    }
}