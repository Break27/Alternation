/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.utils;

import com.github.break27.GameAssets;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JEditorPane;

/**
 *
 * @author break27
 */
public class HtmlUtils {
    
    // ��������ͼƬ
    private static BufferedImage image;
    // ����ͼ��
    private static Graphics graphics;
    // �����༭��
    private static JEditorPane jep;
    
    public static byte[] getImagebyte(String html, File file, int width, int height) {
        return getImageByHtml(html, file, width, height);
    }
    
    private static byte[] getImageByHtml(String html, File file, int width, int height){
        // ʵ����ͼƬ
        image = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration().createCompatibleImage(width, height);
        // ��ȾͼƬ
        renderHtml(image, file, html, width, height);
        // ���ļ�Ϊnull�򷵻ؿ�����
        if ( file == null ) return new byte[0];
        // ������ɼ�ɾ���ļ�
        byte[] data = FileUtils.getbyteStream(file);
        file.delete();
        // ʵ���ļ�-�ֽ�����ת��
        return data;
    }
    
    private static void renderHtml(BufferedImage image, File imageFile, String html, int width, int height) {
        // ʵ����ͼ��
        graphics = image.createGraphics();
        // ʵ�����༭��
        jep = new JEditorPane();
        // ���ñ༭����С
        jep.setSize(width, height);
        // utf8 ֧��
        jep.getDocument().putProperty("Ignore-charset", "true");
        jep.setContentType("text/html;charset=utf-8");
        jep.putClientProperty("charset", "utf-8");
        // ���ñ༭������
        jep.setFont(FileUtils.loadFont("font/Inter-Regular.ttf", 18));
        // �趨�ı�
        jep.setText(html);
        jep.print(graphics);
        // д��ͼƬ
        try {
            ImageIO.write(image, "png", imageFile);
        } catch (IOException ex) {
            Logger.getLogger(HtmlUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
