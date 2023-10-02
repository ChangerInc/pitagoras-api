package changer.pitagoras.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "Circulo")
@NoArgsConstructor
@Getter
public class Circulo {
    @Id
    private UUID id;
    private String nomeCirculo;
    private UUID idDono;
    private LocalDateTime dataCriacao;

    public Circulo(UUID id, String nomeCirculo, UUID idDono, LocalDateTime dataCriacao) {
        this.id = id;
        this.nomeCirculo = nomeCirculo;
        this.idDono = idDono;
        this.dataCriacao = dataCriacao;
    }
}
