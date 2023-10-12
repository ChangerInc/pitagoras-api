package changer.pitagoras.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "Circulo")
@NoArgsConstructor
@Getter
public class Circulo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String nomeCirculo;
    @OneToOne
    private Usuario dono;
    private LocalDateTime dataCriacao;

    public Circulo(String nomeCirculo, Usuario dono) {
        this.id = UUID.randomUUID();
        this.nomeCirculo = nomeCirculo;
        this.dono = dono;
        this.dataCriacao = LocalDateTime.now();
    }
}
