package changer.pitagoras.util;

import changer.pitagoras.model.Usuario;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GerenciadorDeArquivo {
    public static void gravaArquivoCsv(ListaObj<Usuario> lista, String nomeArq){
        FileWriter arq = null;
        Formatter saida = null;
        Boolean incorreto = false;

        nomeArq += ".csv";

        try{
            arq = new FileWriter(nomeArq);
            saida = new Formatter(arq);
        } catch (IOException erro){
            System.out.println("Erro ao abrir o arquivo");
            System.exit(1);
        }

        try{
            for (int i = 0; i < lista.getTamanho(); i++) {
                Object objeto = lista.getElemento(i);

                saida.format("%s\n", objeto.toString());
            }
        }catch (FormatterClosedException erro){
            System.out.println("Erro ao gravar o arquivo");
            incorreto = true;
        } finally{
            saida.close();
            try{
                arq.close();
            }catch (IOException erro){
                System.out.println("Erro ao fechar o arquivo");
                incorreto = true;
            }
            if (incorreto){
                System.exit(1);
            }
        }
    }

    public static void leArquivoCsv(String nomeArq) {
        FileReader arq = null;
        Scanner entrada = null;
        Boolean deuRuim = false;

        nomeArq += ".csv";

        // Bloco try-catch para abrir o arquivo
        try {
            arq = new FileReader(nomeArq);
            entrada = new Scanner(arq).useDelimiter(";|\\n");
        } catch (FileNotFoundException erro) {
            System.out.println("Arquivo nao encontrado");
            System.exit(1);
        }

        // Bloco try-catch para ler o arquivo
        try {
            // Imprime o cabeçalho
            System.out.println(entrada.next());

            // Imprime o corpo do arquivo
            while (entrada.hasNext()) {
                System.out.println(entrada.next());
            }
        } catch (NoSuchElementException erro) {
            System.out.println("Arquivo com problemas");
            deuRuim = true;
        } catch (IllegalStateException erro) {
            System.out.println("Erro na leitura do arquivo");
            deuRuim = true;
        } finally {
            entrada.close();
            try {
                arq.close();
            } catch (IOException erro) {
                System.out.println("Erro ao fechar o arquivo");
                deuRuim = true;
            }
            if (deuRuim) {
                System.exit(1);
            }
        }
    }



}
