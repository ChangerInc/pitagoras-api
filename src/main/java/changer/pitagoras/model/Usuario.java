package changer.pitagoras.model;

//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Entity;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;

import changer.pitagoras.util.Criptograma;

import java.util.UUID;

//@Entity(name = "Usuario")
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
public class Usuario {
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String nome;
    private String email;
    private String senha;

    public void atualizarSenha(String novaSenha) {
        this.senha = new Criptograma().encrypt(novaSenha);
    }

    public Usuario(String nome, String email, String senha) {
        this.id = UUID.randomUUID();
        this.nome = nome;
        this.email = email;
        this.senha = new Criptograma().encrypt(senha);
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }
}
