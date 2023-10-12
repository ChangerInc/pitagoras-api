package changer.pitagoras.service;

import changer.pitagoras.dto.UsuarioNomeEmailDto;
import changer.pitagoras.dto.UsuarioEmailSenhaDto;
import changer.pitagoras.model.Usuario;
import changer.pitagoras.repository.UsuarioRepository;
import changer.pitagoras.util.Criptograma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
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
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(uuid);
        return usuarioOptional.orElse(null);
    }

    public UsuarioEmailSenhaDto encontrarUsuarioPorEmail(UsuarioEmailSenhaDto dto) {
        Optional<UsuarioEmailSenhaDto> usuario =
                usuarioRepository.buscarUsuarioEmailSenhaDto(dto.getEmail(),dto.getSenha());
        System.out.println(usuario);
        return usuario.orElse(null);
    }

    public void deletarUsuario(Usuario user) {
        usuarioRepository.delete(user);
    }



    public boolean update(String senha, UUID id) {
        return usuarioRepository.updateSenha(Criptograma.encrypt(senha), id) != 0;
    }

    public UsuarioEmailSenhaDto converterParaUsuarioLoginDTO(String email, String senha) {
        return new UsuarioEmailSenhaDto(email, senha);
    }

    public UsuarioNomeEmailDto converterParaUsuarioSemSenhaDTO(Usuario usuario) {
        return new UsuarioNomeEmailDto(usuario.getNome(), usuario.getEmail());
    }
}