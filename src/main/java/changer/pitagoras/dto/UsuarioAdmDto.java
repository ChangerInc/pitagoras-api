package changer.pitagoras.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UsuarioAdmDto {
    private UUID id;
    private String nome;
    private String email;
    private String senha;
}
