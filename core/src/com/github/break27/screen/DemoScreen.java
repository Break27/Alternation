/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.break27.TodoGame;
import com.github.break27.utils.FileUtils;
import com.github.break27.utils.HtmlUtils;
import java.io.File;
import java.util.List;

/**
 *
 * @author break27
 */
public class DemoScreen implements Screen {
    private TodoGame parent;
    public DemoScreen(TodoGame game) {
        parent = game;
    }
    
    // ��������
    private SpriteBatch batch;
    // ����λͼͼ��
    private Pixmap pix;
    // ��������
    private Texture texture;
    // ����ͼ����
    private Sprite sprite;
    // ���� html �ļ�
    private File htmlFile;
    // ����ͼ���ֽ�����
    private byte[] byte_array;
    
    @Override
    public void show() {
        batch = new SpriteBatch();
        htmlFile = new File("misc/html/about.html");
        List<String> lines = FileUtils.fileRead(htmlFile);
        String line = new String();
        for (int i=0; i < lines.size(); i++) {
            line = line + lines.get(i);
        }
        byte_array = HtmlUtils.getImagebyte(line, (int) parent.Viewport.getWorldWidth(), (int) parent.Viewport.getWorldHeight());
        pix = new Pixmap(byte_array, 0, byte_array.length);
        texture = new Texture(pix);
        sprite = new Sprite(texture);
    }

    @Override
    public void render(float delta) {
        // ��������Ϊ��ɫ
        Gdx.gl.glClearColor(1,1,1,1);
        // ����
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        sprite.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() { }
    
}
