
package changer.pitagoras.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import changer.pitagoras.util.Criptograma;

import java.time.Instant;
import java.time.LocalDateTime;
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
    public Usuario(String nome, String email, String senha) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.fotoPerfil = null;
        this.plano = false;
        this.dataCriacaoConta = LocalDateTime.now();

    }
}