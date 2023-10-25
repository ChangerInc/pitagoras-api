package changer.pitagoras.dto;

import changer.pitagoras.util.Criptograma;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UsuarioEmailSenhaDto {
    private String email;
    private String senha;

    public UsuarioEmailSenhaDto(String email, String senha) {
        this.email = email;
        this.senha = Criptograma.encrypt(senha);
    }

}
