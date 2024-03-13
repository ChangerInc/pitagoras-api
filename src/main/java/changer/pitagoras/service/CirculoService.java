package changer.pitagoras.service;

import changer.pitagoras.dto.*;
import changer.pitagoras.model.*;
import changer.pitagoras.repository.*;
//import changer.pitagoras.repository.MembroRepository;
import changer.pitagoras.util.Criptograma;
import changer.pitagoras.util.FilaObj;
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
    private ArquivoService arquivoService;
    @Autowired
    private MembroRepository membroRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    protected void validacao(UUID idCirc, UUID idDono) {

        if (!circuloRepository.existsById(idCirc)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (circuloRepository.existe(idCirc, idDono).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    private Circulo pegarCirc(UUID id) {
        Circulo circ = circuloRepository.findById(id).orElse(null);

        if (circ == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Circulo não encontrado");
        }

        return circ;
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

    public Boolean addMembro(NovoMembroDto membroNovo) {
        Optional<Circulo> auxCirc = circuloRepository.findById(membroNovo.getIdCirculo());

        if(auxCirc.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Circulo não encontrado");
        }

        Membro novo = new Membro(
                usuarioService.encontrarUsuarioPorEmail(membroNovo.getEmail()),
                auxCirc.get()
        );
        membroRepository.save(novo);

        return true;
    }

    public List<CirculoMembrosDto> getAllById(UUID idUser) {
        Usuario user = usuarioService.encontrarUsuario(idUser);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }

        List<CirculoMembrosDto> lista = converterListaCirculos(circuloRepository.findAllByDonoOrderByDataCriacao(user));

        for (Membro m : membroRepository.findAllByMembroEquals(user)) {
            Circulo c = m.getCirculo();
            lista.add(gerarCirculoMembros(c.getDono().getId(), c.getId()));
        }

        // Criação da fila e inserção dos elementos
        FilaObj<CirculoMembrosDto> fila = new FilaObj<>(lista.size());
        for (CirculoMembrosDto circulo : lista) {
            fila.insert(circulo);
        }

        // Criação da lista ordenada
        List<CirculoMembrosDto> listaOrdenada = new ArrayList<>();
        while (!fila.isEmpty()) {
            listaOrdenada.add(fila.poll());
        }

        return listaOrdenada;
    }

    public List<Arquivo> resgatarArquivos(UUID idCirculo) {
        Circulo circulo = circuloRepository.findById(idCirculo).orElse(null);

        if (circulo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Circulo não encontrado");
        }

        return circulo.getArquivos();
    }

    public List<CirculoPesquisaDto> findByNomeCirculoContaining(String nomeCirculo, UUID idUser) {
        Usuario user = usuarioService.encontrarUsuario(idUser);
        List<CirculoPesquisaDto> lista = circuloRepository.findByNomeCirculoContaining(
                nomeCirculo, user);
        List<Membro> circMembro = membroRepository.findAllByMembroEquals(user);

        for (Membro m : circMembro) {
            Circulo c = m.getCirculo();
            if (c.getNomeCirculo().contains(nomeCirculo)) {
                lista.add(new CirculoPesquisaDto(c.getId(), c.getNomeCirculo()));
            }
        }

        return lista;
    }

    public boolean removerArquivoNoGrupo(UUID idCirculo, UUID idArquivo) {
        Optional<Circulo> circulo = circuloRepository.findById(idCirculo);
        if (circulo.isEmpty()) {
            return false;
        }

        List<Arquivo> arquivos = circulo.get().getArquivos();
        for (Arquivo a : arquivos) {
            if (a.getIdArquivo().equals(idArquivo)) {
                arquivos.remove(a);
                break;
            }
        }

        circuloRepository.save(circulo.get());
        return true;
    }

    public Boolean removerTodosOsMembrosDoCIrculo(UUID idCirculo){
        Integer deletados = circuloRepository.deletarTodosMembrosDoCirculo(idCirculo);
        return deletados > 0;
    }

    public Boolean adicionarArquivoNoGrupo(UUID idCirculo, UUID idArquivo) {
        Arquivo arquivo = arquivoService.encontrarArq(idArquivo);
        Circulo circulo = pegarCirc(idCirculo);

        circulo.getArquivos().add(arquivo);
        circuloRepository.save(circulo);
        return true;
    }
}