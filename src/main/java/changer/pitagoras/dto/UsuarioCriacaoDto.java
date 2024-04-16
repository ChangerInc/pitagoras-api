package changer.pitagoras.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}