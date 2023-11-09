package changer.pitagoras.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "membro")
@NoArgsConstructor
@Getter
public class Membro {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "Circulo_id")
    private Circulo circulo;
    @ManyToOne
    @JoinColumn(name = "Usuario_id")
    private Usuario membro;
    private LocalDateTime dataInclusao;

    public Membro(Usuario idMembro, Circulo idCirculo) {
        this.id = UUID.randomUUID();
        this.circulo = idCirculo;
        this.membro = idMembro;
        this.dataInclusao = LocalDateTime.now();
    }
}