package com.changer.projeto.api.util;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Criptograma {

    // Método para calcular o hash SHA-256 de uma string
    public static String encrypt(String input) {
        try {
            // Obtém uma instância do MessageDigest com o algoritmo SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Converte a string de entrada em bytes
            byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Converte o resultado do hash em uma representação hexadecimal
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Trate exceções adequadamente
            e.printStackTrace();
            return null;
        }
    }

    // Para teste:
    public static void main(String[] args) {
        String input = "12345678";
        String encrypted = encrypt(input);
        System.out.println("Texto Original: " + input);
        System.out.println("Caracteres: " + encrypted.length());
        System.out.println("SHA-256 Hash: " + encrypted);
    }
}