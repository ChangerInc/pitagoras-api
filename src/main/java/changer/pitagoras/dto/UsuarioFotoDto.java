package changer.pitagoras.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class UsuarioFotoDto {
    private UUID id;
    private String nome;
    private String fotoPerfil;
}