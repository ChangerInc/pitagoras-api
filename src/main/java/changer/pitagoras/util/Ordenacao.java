package changer.pitagoras.util;

import changer.pitagoras.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class Ordenacao {
//    public static void insertionSort(ListaObj<Usuario> lista) {
//        int listaTamanho = lista.getTamanho();
//        for (int i = 1; i < listaTamanho; i++) {
//            Usuario key = lista.getElemento(i);
//            int j = i - 1;
//
//            while (j >= 0 && lista.getElemento(j).getQtdFaltas() > key.getQtdFaltas()) {
//                lista.setElemento(j + 1, lista.getElemento(j));
//                j--;
//            }
//
//            lista.setElemento(j + 1, key);
//        }
//    }
//
//    public static void bubbleSort(ListaObj<Usuario> lista) {
//        int listaTamanho = lista.getTamanho();
//        for (int i = 0; i < listaTamanho - 1; i++) {
//            for (int j = 0; j < listaTamanho - i - 1; j++) {
//                if (lista.getElemento(j).getQtdFaltas() > lista.getElemento(j + 1).getQtdFaltas()) {
//                    Usuario temp = lista.getElemento(j);
//                    lista.setElemento(j, lista.getElemento(j + 1));
//                    lista.setElemento(j + 1, temp);
//                }
//            }
//        }
//    }
//
//    public static void selectionSort(ListaObj<Usuario> lista) {
//        int listaTamanho = lista.getTamanho();
//        for (int i = 0; i < listaTamanho - 1; i++) {
//            int minIndex = i;
//            for (int j = i + 1; j < listaTamanho; j++) {
//                if (lista.getElemento(j).getQtdFaltas() < lista.getElemento(minIndex).getQtdFaltas()) {
//                    minIndex = j;
//                }
//            }
//            Usuario usuarioDaVez = lista.getElemento(i);
//            lista.setElemento(i, lista.getElemento(minIndex));
//            lista.setElemento(minIndex, usuarioDaVez);
//        }
//    }


}
