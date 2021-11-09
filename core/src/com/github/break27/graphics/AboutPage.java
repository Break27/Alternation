/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.github.break27.TodoGame;
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
    // ���������ļ�
    private File RATFile;
    // ����������Ⱦ����������
    private boolean rendered = false;
    private TodoGame parent;
    
    public AboutPage(TodoGame game) {
        parent = game;
        render();
    }
    
    public AboutPage(int width, int height, TodoGame game) {
        parent = game;
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
    
    private boolean render() {
        if ( parent == null ) {
            Gdx.app.error(this.getClass().getName(), "Parent is Unset!");
            return false;
        }
        // ����Ⱦһ��
        if ( !rendered ) {
            htmlFile = Gdx.files.internal("misc/html/about.html").file();
            // ��ȡ html �ļ�������Ⱦ����λͼ
            if ( htmlLine == null ) htmlLine = readFile(htmlFile);
            imagebyte = renderHtml(htmlLine, Width, Height);
            pixmap_ab = new Pixmap(imagebyte, 0, imagebyte.length);
            // ʵ��������
            texture_ab = new Texture(pixmap_ab);
            // ������Ⱦ�����
            rendered = true;
        }
        return true;
    }
    
    private byte[] renderHtml(String line, int width, int height) {
        // ��Ⱦ����ȡͼ���ֽ�����
        RATFile = parent.Assets.getRAT_File();
        byte[] data = HtmlUtils.getImagebyte(line, RATFile, width, height);
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
