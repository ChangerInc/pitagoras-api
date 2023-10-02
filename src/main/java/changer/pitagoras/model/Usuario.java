package changer.pitagoras.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import changer.pitagoras.util.Criptograma;

import java.util.UUID;

@Entity(name = "Usuario")
@NoArgsConstructor
@Getter
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
        setSenha(senha);
    }

    public void setSenha(String senha) {
        this.senha = new Criptograma().encrypt(senha);
    }

    public void atualizarSenha(String novaSenha) {
        setSenha(senha);
    }
}
