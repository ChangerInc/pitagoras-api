package changer.pitagoras.util;

import changer.pitagoras.dto.UsuarioCirculoDto;
import changer.pitagoras.model.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Teste {
    public static void main(String[] args) {
        List<UsuarioCirculoDto> usuarioCirculo = new ArrayList<>();

        Usuario usuario = new Usuario();

        usuarioCirculo.add(new UsuarioCirculoDto("Sara Souza", "sara@email.com","123456S",
                "Fleury.SA", usuario));

        usuarioCirculo.add(new UsuarioCirculoDto("Leone Silva", "leone@email.com","654321L",
                "Dock", usuario));

        usuarioCirculo.add(new UsuarioCirculoDto("Matheus Joaquim", "joaquim@email.com","159753J",
                "Stefanini", usuario));

        usuarioCirculo.add(new UsuarioCirculoDto("Leonardo Lopes", "leo@email.com","357159L",
                "JazzTech", usuario));

        usuarioCirculo.add(new UsuarioCirculoDto("Natã Santos", "nata@email.com","8522258N",
                "Dock", usuario));


        GerenciadorDeArquivoTxt.gravaArquivoTxt(usuarioCirculo, "Usuarios");
        GerenciadorDeArquivoTxt.leArquivoTxt("Usuarios");
    }
}
