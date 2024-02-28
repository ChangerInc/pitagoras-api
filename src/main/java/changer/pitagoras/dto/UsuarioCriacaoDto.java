package changer.pitagoras.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioCriacaoDto {

    @Size(min = 3, max = 40)
    @NotBlank
    @Schema(description = "Nome do usuário", example = "Rafael Reis")
    private String nome;

    @Email
    @Column(unique = true)
    @Schema(description = "Email do usuário", example = "rafael.reis@sptech.school")
    private String email;

    @Size(min = 6, max = 20)
    @Schema(description = "Senha do usuário", example = "AdminAdmin")
    private String senha;

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
}