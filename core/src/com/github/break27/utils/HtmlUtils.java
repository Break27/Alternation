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
    
    // 声明缓冲图片
    private static BufferedImage image;
    // 声明图像
    private static Graphics graphics;
    // 声明编辑器
    private static JEditorPane jep;
    
    public static byte[] getImagebyte(String html, File file, int width, int height) {
        return getImageByHtml(html, file, width, height);
    }
    
    private static byte[] getImageByHtml(String html, File file, int width, int height){
        // 实例化图片
        image = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDefaultConfiguration().createCompatibleImage(width, height);
        // 渲染图片
        renderHtml(image, file, html, width, height);
        // 若文件为null则返回空数组
        if ( file == null ) return new byte[0];
        // 方法完成即删除文件
        byte[] data = FileUtils.getbyteStream(file);
        file.delete();
        // 实现文件-字节数组转化
        return data;
    }
    
    private static void renderHtml(BufferedImage image, File imageFile, String html, int width, int height) {
        // 实例化图像
        graphics = image.createGraphics();
        // 实例化编辑器
        jep = new JEditorPane();
        // 设置编辑器大小
        jep.setSize(width, height);
        // utf8 支持
        jep.getDocument().putProperty("Ignore-charset", "true");
        jep.setContentType("text/html;charset=utf-8");
        jep.putClientProperty("charset", "utf-8");
        // 设置编辑器字体
        jep.setFont(FileUtils.loadFont("font/Inter-Regular.ttf", 18));
        // 设定文本
        jep.setText(html);
        jep.print(graphics);
        // 写入图片
        try {
            ImageIO.write(image, "png", imageFile);
        } catch (IOException ex) {
            Logger.getLogger(HtmlUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
