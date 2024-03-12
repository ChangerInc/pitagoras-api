
package changer.pitagoras.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import changer.pitagoras.util.Criptograma;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "Usuario")
@NoArgsConstructor
@Data
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String nome;
    private String email;
    private String senha;
    @Lob
    @Column(length = 16 * 1024 * 1024) // 16 MB
    private byte[] fotoPerfil;
    private Boolean plano;
    private LocalDateTime dataCriacaoConta;
    @ManyToMany
    @JoinTable(
            name = "arquivos_usuario",
            joinColumns = @JoinColumn(name = "circulo_fk"),
            inverseJoinColumns = @JoinColumn(name = "arquivo_fk")
    )
    private List<Arquivo> arquivos;

    public Usuario(String nome, String email, String senha) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.fotoPerfil = null;
        this.plano = false;
        this.dataCriacaoConta = LocalDateTime.now();
        this.arquivos = new ArrayList<>();
    }


}