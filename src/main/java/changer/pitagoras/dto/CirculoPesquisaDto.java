package changer.pitagoras.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class CirculoPesquisaDto {
    private UUID idCirc;
    private String nome;
}
