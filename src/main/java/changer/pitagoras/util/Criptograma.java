package changer.pitagoras.util;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Criptograma {

    // Método para calcular o hash SHA-256 de uma string
    public String encrypt(String input) {
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
//    public static void main(String[] args) {
//        String input = "ueummmmmmm";
//        String outraInput = "2548964896896";
//        String encrypted = encrypt(input);
//        String encrypted2 = encrypt(outraInput);
//        System.out.println("Texto Original: " + input);
//        System.out.println("Texto Original: " + outraInput);
//        System.out.println("\nCaracteres: " + encrypted.length());
//        System.out.println("Caracteres: " + encrypted2.length());
//        System.out.println("\nSHA-256 Hash: " + encrypt(input));
//        System.out.println("SHA-256 Hash: " + encrypt(outraInput));
//
//        if (encrypted.equals(encrypted2)) {
//            System.out.println("são exatamente iguais!");
//        } else {
//            System.out.println("são diferentes");
//        }
//    }
}