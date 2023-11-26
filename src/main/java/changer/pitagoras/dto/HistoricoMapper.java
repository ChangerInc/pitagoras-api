package changer.pitagoras.dto;

import changer.pitagoras.model.HistoricoConversao;
import changer.pitagoras.model.Usuario;

import java.util.UUID;

public class HistoricoMapper {

    public static HistoricoConversao toEntity(HistoricoDto historicoDto) {
        HistoricoConversao historicoConversao = new HistoricoConversao();

        historicoConversao.setDataConversao(historicoDto.getDataConversao());
        historicoConversao.setExtensaoInicial(historicoDto.getExtensaoAnterior());
        historicoConversao.setExtensaoAtual(historicoDto.getExtensaoAtual());
        historicoConversao.setTamanho(historicoDto.getTamanho());
        if (historicoDto.getFkUsuario() != null) {
            Usuario usuario = new Usuario();
            usuario.setId(UUID.fromString(historicoDto.getFkUsuario())); // Supondo que o ID seja uma String que precisa ser convertida para UUID
            historicoConversao.setUsuario(usuario);
        }

        return historicoConversao;
    }
}