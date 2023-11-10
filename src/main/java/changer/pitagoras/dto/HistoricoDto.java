package changer.pitagoras.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class HistoricoDto {
    private String nome;
    private BigDecimal tamanho;
    private String extensaoAnterior;
    private String extensaoAtual;
    private LocalDateTime dataConversao;
    private String linkDownload;
    private String fkUsuario;


}