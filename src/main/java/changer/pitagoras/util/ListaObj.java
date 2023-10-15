package changer.pitagoras.util;

import java.util.ArrayList;
import java.util.List;

public class ListaObj <T>{

    // Atributos
    private T[] vetor;       // Vetor onde serÃ¡ armazenada a lista
    private int nroElem;     // tem dupla funcao: representa qtos elementos foram adicionados
    // e tb o indice de onde sera adicionado o proximo elemento

    // Construtor
    // Recebe como argumento o tamanho maximo do vetor
    public ListaObj(int tamanho) {
        vetor = (T[]) new Object[tamanho];   // Cria o vetor
        nroElem = 0;                         // Zera nroElem
    }

    // Metodos

    /* Metodo adiciona - recebe o elemento a ser adicionado na lista
       Se a lista estiver cheia, exibe uma mensagem
     */
    public void adiciona(T elemento) {
        if (nroElem >= vetor.length) {
            System.out.println("Lista estÃ¡ cheia");
        }
        else {
            vetor[nroElem++] = elemento;
        }
    }

    /* Metodo exibe - exibe os elementos da lista */
    public void exibe() {
        if (nroElem == 0) {
            System.out.println("\nA lista estÃ¡ vazia.");
        }
        else {
            System.out.println("\nElementos da lista:");
            for (int i = 0; i < nroElem; i++) {
                System.out.println(vetor[i]);
            }
        }
    }

    /* Metodo busca - recebe o elemento a ser procurado na lista
       Retorna o indice do elemento, se for encontrado
       Retorna -1 se nao encontrou
     */
    public int busca(T elementoBuscado) {
        for (int i = 0; i < nroElem; i++) {
            if (vetor[i].equals(elementoBuscado)) {   // se encontrou
                return i;                        // retorna seu indice
            }
        }
        return -1;                               // nao encontrou, retorna -1
    }

    /* Metodo removePeloIndice - recebe o indice do elemento a ser removida
       Se o indice for invalido, retorna false
       Se removeu, retorna true
     */
    public boolean removePeloIndice (int indice) {
        if (indice < 0 || indice >= nroElem) {
            System.out.println("\nIndice invalido!");
            return false;
        }
        // Loop para "deslocar para a esquerda" os elementos do vetor
        // sobrescrevendo o elemento removido
        for (int i = indice; i < nroElem-1; i++) {
            vetor[i] = vetor[i+1];
        }

        nroElem--;          // decrementa nroElem
        return true;
    }

    /* Metodo removeElemento - recebe um elemento a ser removido
       Utiliza os metodos busca e removePeloIndice
       Retorna false, se nao encontrou o elemento
       Retorna true, se encontrou e removeu o elemento
     */
    public boolean removeElemento(T elementoARemover) {
        return removePeloIndice(busca(elementoARemover));
    }

    /* getTamanho()  - retorna o tamanho da lista */
    public int getTamanho() {
        return nroElem;
    }

    /* getElemento() - recebe um indice e retorna o elemento desse indice */
    public T getElemento(int indice) {
        if (indice < 0 || indice >= nroElem) {   // se indice invalido
            return null;                        // entao retorna null
        }
        else {
            return vetor[indice];
        }
    }

    /* limpa() - limpa a lista */
    public void limpa() {
        nroElem = 0;
    }

    public void ordenaPorNome() {
        // Crie uma nova lista para armazenar os elementos ordenados.
        List<T> elementosOrdenados = new ArrayList<>();

        // Adicione todos os elementos da lista atual à lista ordenada, em ordem alfabética.
        for (int i = 0; i < nroElem; i++) {
            elementosOrdenados.add(vetor[i]);
        }

        // Ordene a lista de elementos.
        elementosOrdenados.sort((o1, o2) -> o1.toString().compareTo(o2.toString()));

        // Substitua a lista atual pela lista ordenada.
        vetor = elementosOrdenados.toArray(vetor);
    }

}
