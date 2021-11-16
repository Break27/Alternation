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
    // 屏幕大小
    float height;
    float width;
    
    @Override
    public void show() {
        batch = new SpriteBatch();
        // 实例化 LibGDX 材质
        libgdx_img_tex = new Texture(Gdx.files.internal("banner/libgdx.png"));
        // 实例化 LibGDX 精灵
        image_spr = new Sprite(libgdx_img_tex);
        // 实例化 CDPT 材质
        cdpt_img_tex = new Texture(Gdx.files.internal("banner/cdpt_bk337.png"));
        // 实例化 CDPT 精灵
        cdpt_img_spr = new Sprite(cdpt_img_tex);
        // 获取游戏界面大小
        this.height = parent.Height;
        this.width = parent.Width;
        /*
        * 将精灵位置设为屏幕中心
        * 算法： {X：屏幕宽度一半－精灵宽度一半，Y：屏幕高度一半－精灵高度一半}
        */
        image_spr.setPosition(width/2-image_spr.getWidth()/2, height/2-image_spr.getHeight()/2);
        cdpt_img_spr.setPosition(width/2-cdpt_img_spr.getWidth()/2, height/2-cdpt_img_spr.getHeight()/2);
    }

    @Override
    public void render(float delta) {
        // 将背景设为白色
        Gdx.gl.glClearColor(1,1,1,1);
        // 清屏
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        state += Gdx.graphics.getDeltaTime();
        // 6秒后进入加载画面
        if (state >= 6) super.changeTo(new LoadingScreen(parent));
        batch.begin();
        // 先绘制 LibGDX 再绘制 CDPT（等待3秒）
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
