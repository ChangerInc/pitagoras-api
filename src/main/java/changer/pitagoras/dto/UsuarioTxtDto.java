package changer.pitagoras.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
@Data
@AllArgsConstructor
public class UsuarioTxtDto {
    //Usuario
    private UUID idUsuario;
    private String nome;
    private String email;
    private String senha;
    private Boolean plano;
    private LocalDateTime dataCriacaoConta;
}
