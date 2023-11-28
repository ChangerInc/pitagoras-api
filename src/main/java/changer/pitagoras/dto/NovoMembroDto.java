package changer.pitagoras.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;
@AllArgsConstructor
@Getter
public class NovoMembroDto {
    private UUID idCirculo;
    @Email
    private String email;
    private UUID idDono;
    private UUID idUsuario;
}
