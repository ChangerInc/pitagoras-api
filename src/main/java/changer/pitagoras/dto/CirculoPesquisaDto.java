package changer.pitagoras.dto;

import changer.pitagoras.model.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class CirculoPesquisaDto {
    private UUID idCirc;
    private String nome;
}
