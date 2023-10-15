package changer.pitagoras.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class CirculoSimplesDto {
    private UUID idCirc;
    private String nome;
    private UUID idDono;
}
