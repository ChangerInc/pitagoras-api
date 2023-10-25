
package changer.pitagoras.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import changer.pitagoras.util.Criptograma;

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

    public Usuario(String nome, String email, String senha) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }
}