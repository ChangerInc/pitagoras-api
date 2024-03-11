package changer.pitagoras.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
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
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] bytesArquivo;

    public Arquivo(String nome, BigDecimal tamanho, String extensao) {
        idArquivo = UUID.randomUUID();
        this.nome = nome;
        this.criacao = LocalDateTime.now();
        this.tamanho = tamanho;
        this.extensao = extensao;
    }

    public Arquivo(String nome, String extensao) {

    }
}
