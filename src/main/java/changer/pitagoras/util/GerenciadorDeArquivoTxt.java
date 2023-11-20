package changer.pitagoras.util;

import changer.pitagoras.dto.UsuarioTxtDto;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorDeArquivoTxt {
    public static void gravaRegistro(String registro, String nomeArq) {
        BufferedWriter saida = null;

// Abre o arquivo
        try {
            saida = new BufferedWriter(new FileWriter(nomeArq, true));
        } catch (IOException erro) {
            System.out.println("Erro na abertura do arquivo");
        }

// Grava o registro e fecha o arquivo
        try {
            saida.append(registro + "\n");
            saida.close();
        } catch (IOException erro) {
            System.out.println("Erro ao gravar o arquivo");
            erro.printStackTrace();
        }
    }

    public static void gravaArquivoTxt(List<UsuarioTxtDto> lista, String nomeArq) {
        int contaRegDados = 0;

        // Monta o registro de header
        String header = "00USERS"; //Verificar documento de layout
        LocalDateTime agora = LocalDateTime.now();
        int ano = agora.getYear();
        int mes = agora.getMonthValue();
        int semestre = mes > 6 ? 2 : 1;
        header += String.format("%04d%d", ano, semestre);;
        header += LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        header += "02";

        // Grava o registro de header
        gravaRegistro(header, nomeArq);

        // Grava os registros de dados (ou registros de corpo)
        for (UsuarioTxtDto usuario : lista) {
            //Usuario
            String corpo = "02";
            corpo += String.format("%-37s", usuario.getIdUsuario()); //Completar de acordo com documento
            corpo += String.format("%-25s", usuario.getNome());
            corpo += String.format("%-40s", usuario.getEmail());
            corpo += String.format("%-64s", usuario.getSenha());
            corpo += String.format("%-6s", usuario.getPlano());
            corpo += String.format("%-18s ", usuario.getDataCriacaoConta());

            //Gravando corpo no arquivo:
            gravaRegistro(corpo, nomeArq);
            // Incrementa o contador de registros de dados gravados
            contaRegDados++;
        }

        // Monta e grava o registro de trailer
        String trailer = "01";
        trailer += String.format("%010d", contaRegDados);

        gravaRegistro(trailer, nomeArq);
    }

    public static void leArquivoTxt(String nomeArq) {

        BufferedReader entrada = null;
        String registro, tipoRegistro;
        int contaRegDadosLidos = 0;
        int qtdRegDadosGravados;


        // Cria uma lista para armazenar os objetos criados com
        // os dados lidos do arquivo txt
        List<UsuarioTxtDto> listaLida = new ArrayList<>();
        // Abre o arquivo
        try {
            entrada = new BufferedReader(new FileReader(nomeArq));
        } catch (IOException erro) {
            System.out.println("Erro na abertura do arquivo");
        }
        // Leitura do arquivo
        try {
            registro = entrada.readLine();
            while (registro != null) {
                // obtem os 2 primeiros caracteres do registro lido
                // 1o argumento do substring é o indice do que se quer obter, iniciando de zero
                // 2o argumento do substring é o indice final do que se deseja, MAIS UM
                // 012345
                // 00NOTA
                tipoRegistro = registro.substring(0, 2);

                if (tipoRegistro.equals("00")) {
                    System.out.println("É um registro de header");
                    //Exibir informações do header
                    System.out.printf("Tipo do arquivo: %s\n",registro.substring(2,7));
                    System.out.printf("Ano/semestre: %s\n",registro.substring(7,12));
                    System.out.printf("Data e Hora: %s\n",registro.substring(12,31));
                    System.out.printf("Versão do layout: %s\n",registro.substring(31,33));

                } else if (tipoRegistro.equals("01")) {
                    System.out.println("É um registro de trailer");
                    //Exibir quantidade de registros
                    System.out.println("Quantidade de registros de dados baixados pós conversão: " + contaRegDadosLidos);

                } else if (tipoRegistro.equals("02")) {
                    System.out.println("É um registro de corpo");
                    String id = registro.substring(0,39).trim();
                    String nome = registro.substring(39,56).trim();
                    String email = registro.substring(56,97).trim();
                    String senha = registro.substring(97,162).trim();
                    String plano = registro.substring(165,174).trim();
                    String dataConta = registro.substring(174,204).trim();

                    System.out.println("ID: " + id);
                    System.out.println("Nome: " + nome);
                    System.out.println("Email: " + email);
                    System.out.println("Senha.: " + senha);
                    System.out.println("Plano: " + plano);
                    System.out.println("Data de criação de conta.: " + dataConta);

                    //Guardar dados do corpo em variáveis
                    // Incrementa o contador de reg de dados lidos
                    contaRegDadosLidos++;

                    // Cria um objeto com os dados lidos do registro
                    // Se estivesse conectado a um banco de dados,
                    // como não estamos conectados a um BD, vamos adicionar na lista
                } else {
                    System.out.println("Registro inválido");

                }
                // Le o proximo registro
                registro = entrada.readLine();
            }  // fim do while
            // Fecha o arquivo
            entrada.close();
        } // fim do try
        catch (IOException erro) {
            System.out.println("Erro ao ler o arquivo");
            erro.printStackTrace();
        }
        // Exibe a lista lida
        System.out.println("\nLista lida do arquivo:");

        for (UsuarioTxtDto e : listaLida) {
            System.out.println(e);
        }
        // Aqui tb seria possível salvar a lista no BD
        // repository.saveAll(lista);
    }
}
