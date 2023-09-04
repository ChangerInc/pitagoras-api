package com.changer.projeto.api;

import java.util.concurrent.ThreadLocalRandom;

public class Usuario {
    private Integer id;
    private String nome;
    private String email;
    private String senha;

    public Usuario() {
        id = ThreadLocalRandom.current().nextInt(1, 101);
    }

    public Usuario(String nome, String email, String senha) {
        this();
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    public void atualizarSenha(String novaSenha) {
        this.senha = novaSenha;
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
