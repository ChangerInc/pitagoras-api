package changer.pitagoras.service;

import changer.pitagoras.model.Circulo;
import changer.pitagoras.model.Usuario;
import changer.pitagoras.repository.CirculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CirculoService {
    @Autowired
    CirculoRepository circuloRepository;
    @Autowired
    UsuarioService usuarioService;

    public List<Circulo> getAll() {
        return circuloRepository.findAll();
    }

    public Optional<Circulo> getOne(UUID id) {
        return circuloRepository.findById(id);
    }

    public Circulo insert(String nome, UUID idDono) {
        Usuario dono = usuarioService.encontrarUsuario(idDono);

        return dono == null ? null : circuloRepository.save(new Circulo(nome, dono));
    }
}