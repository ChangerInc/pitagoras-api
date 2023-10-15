package changer.pitagoras.util;

import changer.pitagoras.model.Usuario;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Teste {
    public static void main(String[] args) {
        ListaObj<Usuario> usuario = new ListaObj<>(10);

        usuario.adiciona(new Usuario("leozinho","Leozinho@hotmail.com", "123456" ));
        Scanner leitor = new Scanner(System.in);

        System.out.println("""
                Escolha uma opção:
                1) Gravar Arquivo
                2) Ler Arquivo
                3) Ordenar por Quantidade de Faltas
                4) Remover Funcionários com 3 ou mais faltas
                """);
        Integer opcaoEscolhida = leitor.nextInt();
        switch (opcaoEscolhida){
            case 1:
                // **Gravar Arquivo**

                System.out.println("Digite o nome do arquivo:");
                String nomeArquivo = leitor.next();

                GerenciadorDeArquivo.gravaArquivoCsv(usuario, nomeArquivo);
                break;
            case 2:

                System.out.println("Digite o nome do arquivo:");
                nomeArquivo = leitor.next();

                GerenciadorDeArquivo.leArquivoCsv(nomeArquivo);
                break;
            case 3:

                usuario.ordenarPorQtdFaltas();
                System.out.println("Funcionários ordenados por quantidade de faltas:");
                usuario.exibe();
                break;
            case 4:

                for (int i = 0; i < funcionarios.getTamanho(); i++) {
                    if (funcionarios.getElemento(i).getQtdFaltas() >= 3) {
                        funcionarios.removeElemento(funcionarios.getElemento(i));
                    }
                }
                System.out.println("Funcionários com 3 ou mais faltas removidos:");
                funcionarios.exibe();
                break;
            default:
                System.out.println("Opção inválida!");
                break;
        }
    }
}
