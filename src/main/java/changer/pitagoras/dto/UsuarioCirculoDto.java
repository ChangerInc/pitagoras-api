package changer.pitagoras.dto;

import changer.pitagoras.model.Usuario;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class UsuarioCirculoDto {

    //Usuario
    private UUID idUsuario;
    private String nome;
    private String email;
    private String senha;
    private Boolean plano;
    private LocalDateTime dataCriacaoConta;

    //Circulo
    private UUID idCirculo;
    private String nomeCirculo;
    private Usuario dono;
    private LocalDateTime dataCriacaoCirculo;

    public UsuarioCirculoDto(String nome, String email, String senha, String nomeCirculo, Usuario dono) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.nomeCirculo = nomeCirculo;
        this.dono = dono;
    }

    public UUID getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(UUID idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean getPlano() {
        return plano;
    }

    public void setPlano(Boolean plano) {
        this.plano = plano;
    }

    public LocalDateTime getDataCriacaoConta() {
        return dataCriacaoConta;
    }

    public void setDataCriacaoConta(LocalDateTime dataCriacaoConta) {
        this.dataCriacaoConta = dataCriacaoConta;
    }

    public UUID getIdCirculo() {
        return idCirculo;
    }

    public void setIdCirculo(UUID idCirculo) {
        this.idCirculo = idCirculo;
    }

    public String getNomeCirculo() {
        return nomeCirculo;
    }

    public void setNomeCirculo(String nomeCirculo) {
        this.nomeCirculo = nomeCirculo;
    }

    public Usuario getDono() {
        return dono;
    }

    public void setDono(Usuario dono) {
        this.dono = dono;
    }

    public LocalDateTime getDataCriacaoCirculo() {
        return dataCriacaoCirculo;
    }

    public void setDataCriacaoCirculo(LocalDateTime dataCriacaoCirculo) {
        this.dataCriacaoCirculo = dataCriacaoCirculo;
    }
}
