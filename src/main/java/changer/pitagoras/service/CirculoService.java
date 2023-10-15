package changer.pitagoras.service;

import changer.pitagoras.dto.CirculoSimplesDto;
import changer.pitagoras.model.Circulo;
import changer.pitagoras.model.Usuario;
import changer.pitagoras.repository.CirculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
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

    public int validacao(Map<UUID, UUID> ids) {
        UUID idCirc = ids.get("idCirculo");
        UUID idDono = ids.get("idDono");

        if (circuloRepository.existsById(idCirc)) {
            if (circuloRepository.existe(idCirc, idDono)) {
                return 200;
            }

            return 401;
        }

        return 404;
    }

    public Circulo alterarNome(String nome, UUID id) {
        return circuloRepository.updateNome(nome, id) != 0
                ? circuloRepository.findById(id).get() : null;
    }

    public void deletar(UUID id) {
        circuloRepository.deleteById(id);
    }
}