package changer.pitagoras.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "Circulo")
@NoArgsConstructor
@Data
public class Circulo {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Size(min = 3, max = 13)
    private String nomeCirculo;
    @ManyToOne
    @JoinColumn(name = "Usuario_id")
    private Usuario dono;
    private LocalDateTime dataCriacao;
    @ManyToMany
    @JoinTable(
            name = "arquivos_grupo",
            joinColumns = @JoinColumn(name = "circulo_fk"),
            inverseJoinColumns = @JoinColumn(name = "arquivo_fk")
    )
    private List<Arquivo> arquivos;

    public Circulo(String nomeCirculo, Usuario dono) {
        this.id = UUID.randomUUID();
        this.nomeCirculo = nomeCirculo;
        this.dono = dono;
        this.dataCriacao = LocalDateTime.now();
        this.arquivos = new ArrayList<>();
    }
}