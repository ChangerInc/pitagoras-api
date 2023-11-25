package changer.pitagoras.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ArquivoApenasBytesDto {
    private UUID idConversao;
    private byte[] bytesArquivo;
}
