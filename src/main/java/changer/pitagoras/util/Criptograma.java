package changer.pitagoras.util;
import changer.pitagoras.model.Usuario;

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

    public static void main(String[] args) {
        Usuario teste = new Usuario("Nacchan", "nata@teste.com", "1615");

        System.out.println(teste);
    }
}