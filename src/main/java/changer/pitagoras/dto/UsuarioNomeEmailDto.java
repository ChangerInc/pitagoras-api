package changer.pitagoras.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UsuarioNomeEmailDto {
    private UUID id;
    private String nome;
    private String email;
}