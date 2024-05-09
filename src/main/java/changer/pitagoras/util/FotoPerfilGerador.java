package changer.pitagoras.util;

import org.springframework.web.multipart.MultipartFile;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Random;

public class FotoPerfilGerador {

    public static BufferedImage gerarFotoPerfil(String iniciais) {
        int tamanho = 200; //px
        BufferedImage image = new BufferedImage(tamanho, tamanho, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Define o fundo da imagem com uma cor aleatória
        Random random = new Random();
        Color backgroundColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, tamanho, tamanho);

        // Define a cor e o estilo da fonte
        g2d.setColor(Color.WHITE); // Cor do texto
        g2d.setFont(new Font("Arial", Font.BOLD, tamanho / 2)); // Estilo da fonte

        // Desenha as iniciais do nome no centro da imagem
        FontMetrics fm = g2d.getFontMetrics();
        int x = (tamanho - fm.stringWidth(iniciais)) / 2;
        int y = (fm.getAscent() + (tamanho - (fm.getAscent() + fm.getDescent())) / 2);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawString(iniciais, x, y);

        g2d.dispose();
        return image;
    }

    public static void saveImage(BufferedImage image, String filename) {
        File file = new File(filename);
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            System.out.println("Erro ao salvar a imagem: " + e.getMessage());
        }
    }

    public static String gerarLetras(String nome) {
        String[] partes = nome.split(" ");
        if (partes.length == 1) {
            // Se houver apenas uma parte (somente primeiro nome), retorne as duas primeiras letras
            if (nome.length() >= 2) {
                return nome.substring(0, 2).toUpperCase();
            } else {
                // Se o nome tiver menos de duas letras, retorne o nome completo
                return nome.toUpperCase();
            }
        } else {
            // Se houver mais de uma parte (nome e sobrenome), retorne as iniciais
            String primeiraInicial = partes[0].substring(0, 1);

            String segundaInicial = partes[1].substring(0, 1);
            return (primeiraInicial + segundaInicial).toUpperCase();
        }
    }

    public static MultipartFile convertBufferedImageToMultipartFile(BufferedImage image, String filename) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(baos.toByteArray());
        return new MultipartFileMock(inputStream, filename);
    }
}