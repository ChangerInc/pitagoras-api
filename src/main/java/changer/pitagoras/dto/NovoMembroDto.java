package changer.pitagoras.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;
@AllArgsConstructor
@Getter
public class NovoMembroDto {
    private UUID idCirculo;
    private UUID idUsuario;
}
