package changer.pitagoras.service;

import changer.pitagoras.model.Usuario;
import changer.pitagoras.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class ChangerService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void exportaUsuarioParaCSV(String filename) {
        List<Usuario> usuarios = usuarioRepository.findAll();

        try (FileWriter writer = new FileWriter(filename)) {

            for (Usuario usuario : usuarios) {
                writer.append(usuario.getId().toString()).append(";");
                writer.append(usuario.getNome()).append(";");
                writer.append(usuario.getEmail()).append(";");
                writer.append(usuario.getSenha()).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
