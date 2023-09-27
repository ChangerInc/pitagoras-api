package changer.pitagoras.service;

import changer.pitagoras.model.Usuario;
import changer.pitagoras.repository.UsuarioRepository;
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
        return usuarioRepository.save(usuario);
    }

    public Usuario encontrarUsuario(UUID uuid) {
        return usuarioRepository.getReferenceById(uuid);
    }

    public void deletarUsuario(Usuario user) {
        usuarioRepository.delete(user);
    }

    public Usuario login(String email, String senha) {
        List<Usuario> resultado = usuarioRepository.login(email, senha);
        if (resultado.isEmpty()) {
            return null;
        }

        return resultado.get(0);
    }
}