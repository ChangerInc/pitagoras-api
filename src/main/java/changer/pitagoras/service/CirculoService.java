package changer.pitagoras.service;

import changer.pitagoras.dto.*;
import changer.pitagoras.model.*;
import changer.pitagoras.repository.*;
//import changer.pitagoras.repository.MembroRepository;
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
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ConviteRepository conviteRepository;

    protected void validacao(UUID idCirc, UUID idDono) {
        if (!circuloRepository.existsById(idCirc)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Circulo não encontrado");
        }

        if (!circuloRepository.findById(idCirc).get().getDono().getId().equals(idDono)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não é dono do circulo");
        }
    }

    protected Circulo validacaoGet(UUID idCirc, UUID idDono) {
        Circulo auxCirc = circuloRepository.findById(idCirc).orElse(null);

        if (auxCirc == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Circulo não encontrado");
        }

        if (!auxCirc.getDono().getId().equals(idDono)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não é dono do circulo");
        }

        return auxCirc;
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

        List<UsuarioFotoDto> membros = !auxCirc.getMembros().isEmpty()
                ? converterListaMembros(auxCirc.getMembros())
                : new ArrayList<>();

        return new CirculoMembrosDto(
                idCirc,
                auxCirc.getNomeCirculo(),
                dono,
                membros,
                auxCirc.getArquivos()
        );
    }

    protected CirculoMembrosDto gerarCirculoMembros(Circulo c) {
        List<UsuarioFotoDto> membros = c.getMembros().isEmpty()
                ? new ArrayList<>()
                : converterListaMembros(c.getMembros());

        return new CirculoMembrosDto(
                c.getId(),
                c.getNomeCirculo(),
                c.getDono().getId(),
                membros,
                c.getArquivos()
        );
    }

    protected List<UsuarioFotoDto> converterListaMembros(List<Usuario> lista) {
        List<UsuarioFotoDto> membros = new ArrayList<>();

        for (Usuario m : lista) {
            membros.add(UsuarioMapper.of(m));
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


    public Circulo getOne(Map<String, UUID> ids) {
        return validacaoGet(ids.get("idCirc"), ids.get("idDono"));
    }

    public CirculoMembrosDto insert(String nome, UUID idDono) {
        return gerarCirculoMembros(circuloRepository.save(new Circulo(nome, usuarioService.encontrarUsuario(idDono))));
    }

    public CirculoMembrosDto alterarNome(CirculoSimplesDto c) {
        if (c.getIdDono() == null || c.getIdCirc() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Body faltando informações");
        }

        Circulo auxCirc = validacaoGet(c.getIdCirc(), c.getIdDono());

        if (circuloRepository.updateNome(c.getNome(), c.getIdCirc()) != 0) {
            return gerarCirculoMembros(auxCirc.getDono().getId(), auxCirc.getId());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Circulo não encontrado");
        }
    }

    public void deletar(Map<String, UUID> ids) {
        validacao(ids.get("idCirc"), ids.get("idDono"));
        setStatusConvite(3,ids.get("idCirc"));
        circuloRepository.deleteById(ids.get("idCirc"));
    }

    public Boolean addMembro(NovoMembroDto membroNovo) {
        Circulo auxCirc = pegarCirc(membroNovo.getIdCirculo());

        auxCirc.getMembros().add(usuarioService.encontrarUsuario(membroNovo.getIdUsuario()));

        circuloRepository.save(auxCirc);
        return true;
    }

    public List<CirculoMembrosDto> getAllById(UUID idUser) {
        return converterListaCirculos(
                usuarioService.encontrarUsuario(idUser).getCirculos()
        );
    }

    public List<Arquivo> resgatarArquivos(UUID idCirculo) {
        Circulo circulo = circuloRepository.findById(idCirculo).orElse(null);

        if (circulo == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Circulo não encontrado");
        }

        return circulo.getArquivos();
    }

    public List<CirculoMembrosDto> findByNomeCirculoContaining(String nomeCirculo, UUID idUser) {
        List<CirculoMembrosDto> listCompleta = getAllById(idUser);
        List<CirculoMembrosDto> listPesquisa = new ArrayList<>();

        for (CirculoMembrosDto c : listCompleta) {
            if (c.getNomeCirculo().contains(nomeCirculo)) {
                listPesquisa.add(c);
            }
        }

        return listPesquisa;
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
        Circulo c = pegarCirc(idCirculo);
        List<Usuario> membros = c.getMembros();

        c.getMembros().removeAll(membros);
        circuloRepository.save(c);

        return membros.isEmpty();
    }

    public Boolean adicionarArquivoNoGrupo(UUID idCirculo, UUID idArquivo) {
        Arquivo arquivo = arquivoService.encontrarArq(idArquivo);
        Circulo circulo = pegarCirc(idCirculo);

        circulo.getArquivos().add(arquivo);
        circuloRepository.save(circulo);
        return true;
    }

    public Boolean convidarPessoa(UUID idCirculo, UUID idAnfitriao, String emailDoConvidado) {
        validacao(idCirculo, idAnfitriao);

        usuarioService.encontrarUsuarioPorEmail(emailDoConvidado);

        conviteRepository.save(new Convite(idCirculo, idAnfitriao, emailDoConvidado));
        return true;
    }

    public int setStatusConvite(Integer status, UUID idCirculo){
        return conviteRepository.mudarStatusConvite(status, idCirculo);
    }

    public Boolean decisaoConvite(UUID idCirculo, UUID idUsuario, Integer acaoBotao){
        Usuario usuario = usuarioService.encontrarUsuario(idUsuario);

        if (!circuloRepository.existsById(idCirculo)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Circulo não encontrado");
        }

        if(acaoBotao.equals(1)){
            conviteRepository.mudarStatusConvite(1, idCirculo, usuario.getEmail());
            return addMembro(new NovoMembroDto(idCirculo, idUsuario));
        }

        conviteRepository.mudarStatusConvite(2, idCirculo, usuario.getEmail());
        return false;
    }

    public Boolean sairDoCirculo(UUID idUsuario, UUID idCirculo) {
        Circulo circulo = pegarCirc(idCirculo);

        for (Usuario m : circulo.getMembros()) {
            if (m.getId().equals(idUsuario)) {
                circulo.getMembros().remove(m);
                circuloRepository.save(circulo);
                return true;
            }
        }

        return false;
    }
}