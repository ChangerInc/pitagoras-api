package changer.pitagoras.util.enums;

import lombok.Getter;

@Getter
public enum StatusConviteEnum {
    NAO_LIDO(0),
    ACEITO(1),
    REJEITADO(2),
    GRUPO_EXCLUIDO(3),
    CONVITE_DUPLICADO(4);

    private Integer status;

    StatusConviteEnum(Integer status) {
        this.status = status;
    }
}