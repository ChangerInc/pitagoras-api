package changer.pitagoras.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "Convite")
@NoArgsConstructor
@Data
public class Convite {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idRegistro;

    private UUID idCirculo;

    private UUID idAnfitriao;

    private String emailConvidado;

    private Integer statusConvite;
    /* 0 = nao lido / 1 = aceito / 2 = rejeitado / 3 = grupo excluido */

    private LocalDateTime dataRegistro;

    public Convite(UUID idCirculo, UUID idAnfitriao, String emailConvidado) {
        this.idRegistro = UUID.randomUUID();
        this.idCirculo = idCirculo;
        this.idAnfitriao = idAnfitriao;
        this.emailConvidado = emailConvidado;
        this.statusConvite = 0;
        this.dataRegistro = LocalDateTime.now();
    }
}
