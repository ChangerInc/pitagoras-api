package changer.pitagoras.service;

import changer.pitagoras.dto.CirculoMembrosDto;
import changer.pitagoras.dto.CirculoSimplesDto;
import changer.pitagoras.dto.UsuarioNomeEmailDto;
import changer.pitagoras.model.Circulo;
import changer.pitagoras.model.Membro;
import changer.pitagoras.model.Usuario;
import changer.pitagoras.repository.CirculoRepository;
//import changer.pitagoras.repository.MembroRepository;
import changer.pitagoras.repository.MembroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class CirculoService {
    @Autowired
    private CirculoRepository circuloRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private MembroRepository membroRepository;

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

    protected void validacao(UUID idCirc, UUID idDono) {

        if (circuloRepository.existsById(idCirc)) {
            if (circuloRepository.existe(idCirc, idDono) == null) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }

            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public Circulo alterarNome(CirculoSimplesDto c) {
        validacao(c.getIdCirc(), c.getIdDono());

        return circuloRepository.updateNome(c.getNome(), c.getIdCirc()) != 0
                ? circuloRepository.findById(c.getIdCirc()).get() : null;
    }

    public void deletar(CirculoSimplesDto c) {
        validacao(c.getIdCirc(), c.getIdDono());

        circuloRepository.deleteById(c.getIdCirc());
    }

//    public CirculoMembrosDto addMembro(Map<String, UUID> novoMembro) {
    public Membro addMembro(Map<String, UUID> novoMembro) {
        UUID idUser = novoMembro.get("idUser");
        UUID idCirc = novoMembro.get("idCirc");

        validacao(idCirc, novoMembro.get("idDono"));
        Circulo auxCirc = circuloRepository.findById(idCirc).get();
        Usuario auxUser = usuarioService.encontrarUsuario(idUser);

        UsuarioNomeEmailDto userSimples = new UsuarioNomeEmailDto(idUser, auxUser.getNome(), auxUser.getEmail());

        Membro novo = new Membro(auxUser, auxCirc);

        return membroRepository.save(novo);
        /*return new CirculoMembrosDto(
                idCirc,
                auxCirc.getNomeCirculo(),
                userSimples,
                auxCirc.getDataCriacao()*//*,
                membroRepository.findAllByCircEquals(idCirc)*//*
        );*/
    }
}