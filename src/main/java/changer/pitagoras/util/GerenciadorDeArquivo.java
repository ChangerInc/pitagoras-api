package changer.pitagoras.util;

import changer.pitagoras.model.Usuario;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class GerenciadorDeArquivo {
    public static void gravaArquivoCsv(ListaObj<Usuario> lista, String nomeArq) {
        FileWriter arq = null;
        Formatter saida = null;
        Boolean deuRuim = false;

        nomeArq += "Usuarios.csv";

        try {
            arq = new FileWriter(nomeArq);
            saida = new Formatter(arq);
        } catch (IOException erro) {
            System.out.println("Erro ao abrir o arquivo");
            System.exit(1);
        }

        try {
            for (int i = 0; i < lista.getTamanho(); i++) {

                Usuario usuario = lista.getElemento(i);
                saida.format("%d;%s;%s;%.2f\n",
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getSenha());
            }
        } catch (FormatterClosedException erro) {
            System.out.println("Erro ao gravar o arquivo");
            deuRuim = true;
        } finally {
            saida.close();
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

    public static void leArquivoCsv(String nomeArq) {
        FileReader arq = null;
        Scanner entrada = null;
        Boolean deuRuim = false;

        nomeArq += "Usuarios.csv";

        try {
            arq = new FileReader(nomeArq);
            entrada = new Scanner(arq).useDelimiter(";|\\n");
        } catch (FileNotFoundException erro) {
            System.out.println("Arquivo nao encontrado");
            System.exit(1);
        }

        try {

            System.out.printf("%4S %-15S %-9S %6S\n",
                    "id", "nome","email", "senha" );
            while (entrada.hasNext()) {

                UUID id = UUID.fromString(entrada.next());
                String nome = entrada.next();
                String email = entrada.next();
                String senha = entrada.next();

                System.out.printf("%4d %-15s %-9s %6.2f\n",
                        id, nome, email, senha);

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
