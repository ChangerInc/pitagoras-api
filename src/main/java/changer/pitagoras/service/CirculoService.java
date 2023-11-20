package changer.pitagoras.service;

import changer.pitagoras.dto.CirculoMembrosDto;
import changer.pitagoras.dto.CirculoSimplesDto;
import changer.pitagoras.dto.UsuarioFotoDto;
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

import java.util.*;

@Service
public class CirculoService {
    @Autowired
    private CirculoRepository circuloRepository;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private MembroRepository membroRepository;

    protected void validacao(UUID idCirc, UUID idDono) {

        if (!circuloRepository.existsById(idCirc)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (circuloRepository.existe(idCirc, idDono).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    protected CirculoMembrosDto gerarCirculoMembros(UUID dono, UUID idCirc) {
        validacao(idCirc, dono);

        Circulo auxCirc = circuloRepository.findById(idCirc).get();
        Usuario auxUser = usuarioService.encontrarUsuario(dono);

        List<Membro> auxMembro = membroRepository.findAllByCirculoEquals(auxCirc);
        List<UsuarioFotoDto> membros = new ArrayList<>();

        if (!auxMembro.isEmpty()) {
            membros = converterListaMembros(auxMembro);
        }

        return new CirculoMembrosDto(
                idCirc,
                auxCirc.getNomeCirculo(),
                converteUserSimples(auxUser),
                auxCirc.getDataCriacao(),
                membros
        );
    }

    protected UsuarioFotoDto converteUserSimples(Usuario membro) {
        return new UsuarioFotoDto(membro.getId(), membro.getNome(), membro.getFotoPerfil());
    }

    protected List<UsuarioFotoDto> converterListaMembros(List<Membro> lista) {
        List<UsuarioFotoDto> membros = new ArrayList<>();

        for (Membro m : lista) {
            membros.add(converteUserSimples(m.getMembro()));
        }

        return membros;
    }

    protected List<CirculoMembrosDto> converterListaCirculos(List<Circulo> lista) {
        List<CirculoMembrosDto> circulos = new ArrayList<>();

        for (Circulo c : lista) {
            circulos.add(gerarCirculoMembros(c.getDono().getId(), c.getId()));
        }

        return circulos;
    }

    public List<CirculoMembrosDto> getAll() {
        return converterListaCirculos(circuloRepository.findAll());
    }

    public CirculoMembrosDto getOne(Map<String, UUID> ids) {
        validacao(ids.get("idCirc"), ids.get("idDono"));

        return gerarCirculoMembros(ids.get("idDono"), ids.get("idCirc"));
    }

    public CirculoMembrosDto insert(String nome, UUID idDono) {
        Usuario dono = usuarioService.encontrarUsuario(idDono);

        if (dono == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Circulo auxCirculo = circuloRepository.save(new Circulo(nome, dono));

        return gerarCirculoMembros(idDono, auxCirculo.getId());
    }

    public CirculoMembrosDto alterarNome(CirculoSimplesDto c) {
        UUID idCirc = c.getIdCirc();
        UUID idDono = c.getIdDono();

        if (idDono == null || idCirc == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body faltando informações");
        }

        validacao(idCirc, idDono);

        Optional<Circulo> auxCirc = circuloRepository.findById(idCirc);

        if (circuloRepository.updateNome(c.getNome(), c.getIdCirc()) != 0 && auxCirc.isPresent()) {
            return gerarCirculoMembros(idDono, idCirc);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Circulo não encontrado");
        }
    }

    public void deletar(Map<String, UUID> ids) {
        validacao(ids.get("idCirc"), ids.get("idDono"));

        circuloRepository.deleteById(ids.get("idCirc"));
    }

    public CirculoMembrosDto addMembro(Map<String, UUID> novoMembro) {
        UUID idUser = novoMembro.get("idUser");
        UUID idCirc = novoMembro.get("idCirc");
        UUID idDono = novoMembro.get("idDono");

        validacao(idCirc, idDono);

        Circulo auxCirc = circuloRepository.findById(idCirc).get();
        Usuario auxUser = usuarioService.encontrarUsuario(idUser);

        Membro novo = new Membro(auxUser, auxCirc);

        membroRepository.save(novo);

        return gerarCirculoMembros(idDono, idCirc);
    }

    public List<CirculoMembrosDto> getAllById(UUID idUser) {
        Usuario user = usuarioService.encontrarUsuario(idUser);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }

        return converterListaCirculos(circuloRepository.findAllByDono(user));
    }
}