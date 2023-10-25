package changer.pitagoras.service;

import changer.pitagoras.dto.UsuarioNomeEmailDto;
import changer.pitagoras.dto.UsuarioEmailSenhaDto;
import changer.pitagoras.model.Usuario;
import changer.pitagoras.repository.UsuarioRepository;
import changer.pitagoras.util.Criptograma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import changer.pitagoras.model.Usuario;
import changer.pitagoras.util.ListaObj;
import java.util.List;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

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

    public UsuarioNomeEmailDto encontrarUsuarioPorEmail(UsuarioEmailSenhaDto dto) {
        Optional<UsuarioNomeEmailDto> usuario =
                usuarioRepository.buscarUsuarioEmailSenhaDto(dto.getEmail(), dto.getSenha());
        System.out.println(usuario);
        return usuario.orElse(null);
    }

    public void deletarUsuario(Usuario user) {
        usuarioRepository.delete(user);
    }


    public int update(Map<String, String> senhas, UUID id) {
        String senhaAtual = senhas.get("senhaAtual");
        String senhaNova = senhas.get("senhaNova");

        if (encontrarUsuario(id) == null) {
            return 404;
        }

        if (usuarioRepository.existsBySenhaAndId(Criptograma.encrypt(senhaNova), id)) {
            return 409;
        }

        if (usuarioRepository.existsBySenhaAndId(Criptograma.encrypt(senhaAtual), id)) {
            usuarioRepository.updateSenha(Criptograma.encrypt(senhaNova), id);
            return 200;
        }

        return 400;
    }

    public UsuarioEmailSenhaDto converterParaUsuarioLoginDTO(String email, String senha) {
        return new UsuarioEmailSenhaDto(email, senha);
    }

    public UsuarioNomeEmailDto converterParaUsuarioSemSenhaDTO(Usuario usuario) {
        return new UsuarioNomeEmailDto(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }

    public ListaObj<Usuario> ordenaPorNome(List<Usuario> lista) {
        ListaObj<Usuario> listaObj = new ListaObj<>(lista.size());

        for (Usuario usuario : lista) {
            listaObj.adiciona(usuario);
        }

        for (int i = 0; i < listaObj.getTamanho() - 1; i++) {
            int indMenor = i;
            for (int j = i + 1; j < listaObj.getTamanho(); j++) {
                if (listaObj.getElemento(j).getEmail().compareToIgnoreCase(listaObj.getElemento(indMenor).getEmail()) < 0) {
                    indMenor = j;
                }
            }
            Usuario aux = listaObj.getElemento(i);
            listaObj.adiciona(i, listaObj.getElemento(indMenor));
            listaObj.adiciona(indMenor, aux);
        }

        return listaObj;
    }

    public Usuario pesquisaBinaria(ListaObj<Usuario> listaObj, String email) {
        int inicio = 0;
        int fim = listaObj.getTamanho() - 1;

        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;
            int comp = listaObj.getElemento(meio).getEmail().compareToIgnoreCase(email);

            if (comp == 0) {
                return listaObj.getElemento(meio);
            } else if (comp < 0) {
                inicio = meio + 1;
            } else {
                fim = meio - 1;
            }
        }

        return null;
    }

}