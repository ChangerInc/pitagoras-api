package changer.pitagoras.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "MembroCirculo")
@NoArgsConstructor
@Getter
public class MembroCirculo {
    @Id
    private UUID idMembro;
    @Id
    private UUID idCirculo;
    private LocalDateTime dataInclusao;

    public MembroCirculo(UUID idMembro, UUID idCirculo) {
        this.idMembro = idMembro;
        this.idCirculo = idCirculo;
        this.dataInclusao = LocalDateTime.now();
    }
}
