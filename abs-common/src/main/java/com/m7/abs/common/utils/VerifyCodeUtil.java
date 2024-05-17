package com.m7.abs.common.utils;/**
 * @author PKJ
 * @date 2018/6/12 0012 下午 1:36
 * @content
 **/

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * 生成验证码
 *
 * @author pkj
 * @create 2018-06-12 下午 1:36
 **/
public class VerifyCodeUtil {
    private int w = 50;
    private int h = 25;
    private Random r = new Random();
    private String[] fontNames = {"Times New Roman", "宋体", "仿宋_GB2312"};
    private String codes = "0123456789abcdefghjkmnpqrstuvwxyzABCEFGHIJKLMNPQRSTUVWXYZ";
    private Color bgColor = new Color(255, 255, 255);
    private String text;

    private Color randomColor() {
        int red = r.nextInt(200);
        int green = r.nextInt(200);
        int blue = r.nextInt(200);
        return new Color(red, green, blue);
    }

    private Font randomFont() {
        int index = r.nextInt(fontNames.length);
        String fontName = fontNames[index];
        int style = r.nextInt(4);
        int size = r.nextInt(6) + 15;
        return new Font(fontName, style, size);
    }

    private void drawLine(BufferedImage image) {
        int num = 3;
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        for (int i = 0; i < num; i++) {
            int x1 = r.nextInt();
            int y1 = r.nextInt();
            int x2 = r.nextInt();
            int y2 = r.nextInt();
            g2.setStroke(new BasicStroke(1.5F));
            g2.setColor(Color.BLUE);
            g2.drawLine(x1, y1, x2, y2);
        }
    }

    private char randomChar() {
        int index = r.nextInt(codes.length());
        return codes.charAt(index);
    }

    private BufferedImage createImage() {
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setColor(this.bgColor);
        g2.fillRect(0, 0, w, h);
        return image;
    }

    public BufferedImage getImage() {
        BufferedImage image = createImage();
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            String s = randomChar() + "";
            sb.append(s);
            float x = i * 1.0F * w / 4;
            g2.setFont(randomFont());
            g2.setColor(randomColor());
            g2.drawString(s, x, h - 5);
        }
        this.text = sb.toString();
        drawLine(image);
        return image;
    }

    public String getText() {
        return text;
    }

    public static void output(BufferedImage image, OutputStream out) throws IOException {
        ImageIO.write(image, "JPEG", out);
    }
}
