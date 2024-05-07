package changer.pitagoras.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "Arquivo")
@NoArgsConstructor
@Data
public class Arquivo {
    @Id
    private UUID idArquivo;
    private String nome;
    private LocalDateTime criacao;
    private BigDecimal tamanho;
    private String extensao;
    private String urlArquivo;

    public Arquivo(String nome, BigDecimal tamanho, String extensao, String urlArquivo) {
        idArquivo = UUID.randomUUID();
        this.nome = nome;
        this.criacao = LocalDateTime.now();
        this.tamanho = tamanho;
        this.extensao = extensao;
        this.urlArquivo = urlArquivo;
    }
}
