package changer.pitagoras.util;//Classe pronta, NÃO é necessário alterar nada

public class PilhaObj<T> {
    private T[] pilha;
    private int topo;
    public PilhaObj(int capacidade) {
        pilha = (T[]) new Object[capacidade];
        topo = -1;
    }
    public Boolean isEmpty() {
        return topo == -1;
    }
    public Boolean isFull() {
        return topo == pilha.length-1;
    }
    public void push(T info) {
        if (isFull()) {
            throw new IllegalStateException();
        }
        else {
            pilha[++topo] = info;
        }
    }
    public T pop() {
        if (isEmpty()) {
            return null;
        }
        return pilha[topo--];
    }
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return pilha[topo];
    }
    public void exibe() {
        if (isEmpty()) {
            System.out.println("Pilha vazia");
        }
        else {
            for (int i = topo; i >= 0; i--) {
                System.out.println(pilha[i]);
            }
        }
    }
    public int getTopo() {
        return topo;
    }
}