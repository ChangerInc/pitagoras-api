package changer.pitagoras.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "HistoricoConversao")
@NoArgsConstructor
@Data
@Getter
public class HistoricoConversao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idConversao;
    private String nome;
    private BigDecimal tamanho;
    private String extensaoAnterior;
    private String extensaoAtual;
    private LocalDateTime dataConversao;
    private String linkDownload;
    @ManyToOne
    @JoinColumn(name = "Usuario_id")
    private Usuario usuario;

    public HistoricoConversao(String nome, BigDecimal tamanho, String extensaoAnterior, String extensaoAtual, String linkDownload, Usuario usuario) {
        this.idConversao = UUID.randomUUID();
        this.nome = nome;
        this.tamanho = tamanho;
        this.extensaoAnterior = extensaoAnterior;
        this.extensaoAtual = extensaoAtual;
        this.dataConversao = LocalDateTime.now();
        this.linkDownload = linkDownload;
        this.usuario = usuario;
    }
}