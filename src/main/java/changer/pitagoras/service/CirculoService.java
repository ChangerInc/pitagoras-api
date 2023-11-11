package changer.pitagoras.service;

import changer.pitagoras.dto.CirculoMembrosDto;
import changer.pitagoras.dto.CirculoSimplesDto;
import changer.pitagoras.dto.UsuarioNomeEmailDto;
import changer.pitagoras.dto.autenticacao.MembroDto;
import changer.pitagoras.model.Circulo;
import changer.pitagoras.model.Membro;
import changer.pitagoras.model.Usuario;
import changer.pitagoras.repository.CirculoRepository;
//import changer.pitagoras.repository.MembroRepository;
import changer.pitagoras.repository.MembroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
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

    protected CirculoMembrosDto gerarCirculoMembros(Map<String, UUID> ids) {
        UUID dono = ids.get("idUser");
        UUID idCirc = ids.get("idCirc");

        validacao(idCirc, dono);

        Circulo auxCirc = circuloRepository.findById(idCirc).get();
        Usuario auxUser = usuarioService.encontrarUsuario(dono);

        List<Membro> auxMembro = membroRepository.findAllByCirculoEquals(auxCirc);
        List<MembroDto> membros = new ArrayList<>();

        if (!auxMembro.isEmpty()) {
            membros = converterLista(auxMembro);
        }

        return new CirculoMembrosDto(
                idCirc,
                auxCirc.getNomeCirculo(),
                converteUserSimples(auxUser),
                auxCirc.getDataCriacao(),
                membros
        );
    }

    protected UsuarioNomeEmailDto converteUserSimples(Usuario membro) {
        return new UsuarioNomeEmailDto(membro.getId(), membro.getNome(), membro.getEmail());
    }

    protected List<MembroDto> converterLista(List<Membro> lista) {
        List<MembroDto> membros = new ArrayList<>();

        for (Membro m : lista) {
            membros.add(new MembroDto(converteUserSimples(m.getMembro()), m.getDataInclusao()));
        }

        return membros;
    }

    public List<Circulo> getAll() {
        return circuloRepository.findAll();
    }

    public CirculoMembrosDto getOne(Map<String, UUID> ids) {
        return gerarCirculoMembros(ids);
    }

    public Circulo insert(String nome, UUID idDono) {
        Usuario dono = usuarioService.encontrarUsuario(idDono);

        return dono == null ? null : circuloRepository.save(new Circulo(nome, dono));
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
    public CirculoMembrosDto addMembro(Map<String, UUID> novoMembro) {
        UUID idUser = novoMembro.get("idUser");
        UUID idCirc = novoMembro.get("idCirc");

        validacao(idCirc, novoMembro.get("idDono"));

        Circulo auxCirc = circuloRepository.findById(idCirc).get();
        Usuario auxUser = usuarioService.encontrarUsuario(idUser);

        UsuarioNomeEmailDto userSimples = new UsuarioNomeEmailDto(idUser, auxUser.getNome(), auxUser.getEmail());

        Membro novo = new Membro(auxUser, auxCirc);

        membroRepository.save(novo);
        return new CirculoMembrosDto(
                idCirc,
                auxCirc.getNomeCirculo(),
                userSimples,
                auxCirc.getDataCriacao(),
                converterLista(membroRepository.findAllByCirculoEquals(auxCirc))
        );
    }
}