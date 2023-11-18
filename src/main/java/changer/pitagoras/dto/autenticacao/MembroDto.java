package changer.pitagoras.dto.autenticacao;

import changer.pitagoras.dto.UsuarioNomeEmailDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class MembroDto {
    private UsuarioNomeEmailDto membro;
    private LocalDateTime dataInclusao;
}
