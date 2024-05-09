package changer.pitagoras.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class ConviteDto {

    private String fotoAnfitriao;
    private String anfitriao;
    private String nomeCirculo;
    private UUID idCirculo;
    private LocalDateTime horario;

}
