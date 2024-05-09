package changer.pitagoras.dto;

import changer.pitagoras.model.Arquivo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class CirculoMembrosDto {
    private UUID id;
    private String nomeCirculo;
    private UUID dono;
    private List<UsuarioFotoDto> membros;
    private List<Arquivo> arquivos;
}
