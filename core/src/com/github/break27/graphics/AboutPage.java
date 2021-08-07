/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.break27.utils.FileUtils;
import com.github.break27.utils.HtmlUtils;
import java.io.File;
import java.util.List;

/**
 * 
 * @author break27
 */
public final class AboutPage {    
    
    // 声明位图图像
    private Pixmap pixmap_ab;
    // 声明材质
    private Texture texture_ab;
    // 声明字节数组
    private byte[] imagebyte;
    // 声明图像
    private Image image;
    // 声明 html 字符串
    private String htmlLine;
    // 声明图像大小整型（默认为 100x100）
    private int Width = 100, Height = 100;
    // 声明 html 文件
    private File htmlFile;
    // 声明“已渲染”布尔类型
    private boolean rendered = false;
    
    public AboutPage() {
        render();
    }
    
    public AboutPage(int width, int height) {
        setSize(width, height);
    }

    public Image getImage() {
        // 生成图像
        if ( image == null ) image = new Image(texture_ab);
        return image;
    }
    
    public void setSize(int width, int height) {
        // 若大小数据不一致，重新渲染
        if ( width != Width || height != Height ) rendered = false;
        // 设置大小数据
        Width = width;
        Height = height;
        render();
    }
    
    private void render() {
        // 仅渲染一次
        if ( !rendered ) {
            // 文件 -- debug
            if ( htmlFile == null ) htmlFile = new File("misc/html/about.html");
            // 读取 html 文件，经渲染生成位图
            if ( htmlLine == null ) htmlLine = readFile(htmlFile);
            imagebyte = renderHtml(htmlLine, Width, Height);
            pixmap_ab = new Pixmap(imagebyte, 0, imagebyte.length);
            // 实例化材质
            texture_ab = new Texture(pixmap_ab);
            // “已渲染”标记
            rendered = true;
        }
    }
    
    private byte[] renderHtml(String line, int width, int height) {
        // 渲染，获取图像字节数据
        byte[] data = new byte[0];
        data = HtmlUtils.getImagebyte(line, width, height);
        return data;
    }
    
    private String readFile(File htmlFile) {
        // 读取文件，返回字符串
        List<String> lines = FileUtils.fileRead(htmlFile);
        String line = new String();
        for (int i=0; i < lines.size(); i++) line = line + lines.get(i);
        return line;
    }
}
