package changer.pitagoras.dto;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioNomeEmailDto {
    private String nome;
    private String email;

    public UsuarioNomeEmailDto(String nome, String email) {
        this.nome = nome;
        this.email = email;
    }

}
