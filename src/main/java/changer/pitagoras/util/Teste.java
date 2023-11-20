package changer.pitagoras.util;

import changer.pitagoras.dto.UsuarioTxtDto;
import changer.pitagoras.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class Teste {
    public static void main(String[] args) {
        List<UsuarioTxtDto> usuarioTxtDtos = new ArrayList<>();

        Usuario usuario = new Usuario();

      //  usuarioTxtDtos.add(new UsuarioTxtDto("lele", "lele@gmail.com", "dasçkndflajdaf", true));
      //  usuarioTxtDtos.add(new UsuarioTxtDto("lala", "lala@gmail.com", "dsad23r435", false));
      //  usuarioTxtDtos.add(new UsuarioTxtDto("laele", "laele@gmail.com", "r43tooh48H89G7GF", true));

        GerenciadorDeArquivoTxt.gravaArquivoTxt(usuarioTxtDtos, "Usuarios");
        GerenciadorDeArquivoTxt.leArquivoTxt("Usuarios");
    }
}
