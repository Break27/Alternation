/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.break27.TodoGame3;

/**
 *
 * @author break27
 */
public class BannerScreen extends AbstractScreen {
    
    public BannerScreen(TodoGame3 game) {
        super(game);
    }

    Texture libgdx_img_tex;
    Texture cdpt_img_tex;
    Sprite image_spr;
    Sprite cdpt_img_spr;
    // ��Ļ��С
    float height;
    float width;
    
    @Override
    public void show() {
        batch = new SpriteBatch();
        // ʵ���� LibGDX ����
        libgdx_img_tex = new Texture(Gdx.files.internal("banner/libgdx.png"));
        // ʵ���� LibGDX ����
        image_spr = new Sprite(libgdx_img_tex);
        // ʵ���� CDPT ����
        cdpt_img_tex = new Texture(Gdx.files.internal("banner/cdpt_bk337.png"));
        // ʵ���� CDPT ����
        cdpt_img_spr = new Sprite(cdpt_img_tex);
        // ��ȡ��Ϸ�����С
        this.height = parent.Height;
        this.width = parent.Width;
        /*
        * ������λ����Ϊ��Ļ����
        * �㷨�� {X����Ļ���һ�룭������һ�룬Y����Ļ�߶�һ�룭����߶�һ��}
        */
        image_spr.setPosition(width/2-image_spr.getWidth()/2, height/2-image_spr.getHeight()/2);
        cdpt_img_spr.setPosition(width/2-cdpt_img_spr.getWidth()/2, height/2-cdpt_img_spr.getHeight()/2);
    }

    @Override
    public void render(float delta) {
        // ��������Ϊ��ɫ
        Gdx.gl.glClearColor(1,1,1,1);
        // ����
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        state += Gdx.graphics.getDeltaTime();
        // 6��������ػ���
        if (state >= 6) super.changeTo(new LoadingScreen(parent));
        batch.begin();
        // �Ȼ��� LibGDX �ٻ��� CDPT���ȴ�3�룩
        if (state >= 3) {
            image_spr.set(cdpt_img_spr);
        }
        image_spr.draw(batch);
        batch.end();
    }
    
    @Override
    public int getId() {
        return BANNER;
    }
    
    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}
