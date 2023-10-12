package changer.pitagoras.dto;

import changer.pitagoras.util.Criptograma;
import lombok.Getter;

@Getter
public class UsuarioSimplesDto {
    private String email;
    private String senha;

    public UsuarioSimplesDto(String email, String senha) {
        this.email = email;
        this.senha = Criptograma.encrypt(senha);
    }
}
