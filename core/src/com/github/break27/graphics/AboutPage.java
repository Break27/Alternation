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
    
    // ����λͼͼ��
    private Pixmap pixmap_ab;
    // ��������
    private Texture texture_ab;
    // �����ֽ�����
    private byte[] imagebyte;
    // ����ͼ��
    private Image image;
    // ���� html �ַ���
    private String htmlLine;
    // ����ͼ���С���ͣ�Ĭ��Ϊ 100x100��
    private int Width = 100, Height = 100;
    // ���� html �ļ�
    private File htmlFile;
    // ����������Ⱦ����������
    private boolean rendered = false;
    
    public AboutPage() {
        render();
    }
    
    public AboutPage(int width, int height) {
        setSize(width, height);
    }

    public Image getImage() {
        // ����ͼ��
        if ( image == null ) image = new Image(texture_ab);
        return image;
    }
    
    public void setSize(int width, int height) {
        // ����С���ݲ�һ�£�������Ⱦ
        if ( width != Width || height != Height ) rendered = false;
        // ���ô�С����
        Width = width;
        Height = height;
        render();
    }
    
    private void render() {
        // ����Ⱦһ��
        if ( !rendered ) {
            // �ļ� -- debug
            if ( htmlFile == null ) htmlFile = new File("misc/html/about.html");
            // ��ȡ html �ļ�������Ⱦ����λͼ
            if ( htmlLine == null ) htmlLine = readFile(htmlFile);
            imagebyte = renderHtml(htmlLine, Width, Height);
            pixmap_ab = new Pixmap(imagebyte, 0, imagebyte.length);
            // ʵ��������
            texture_ab = new Texture(pixmap_ab);
            // ������Ⱦ�����
            rendered = true;
        }
    }
    
    private byte[] renderHtml(String line, int width, int height) {
        // ��Ⱦ����ȡͼ���ֽ�����
        byte[] data = new byte[0];
        data = HtmlUtils.getImagebyte(line, width, height);
        return data;
    }
    
    private String readFile(File htmlFile) {
        // ��ȡ�ļ��������ַ���
        List<String> lines = FileUtils.fileRead(htmlFile);
        String line = new String();
        for (int i=0; i < lines.size(); i++) line = line + lines.get(i);
        return line;
    }
}
