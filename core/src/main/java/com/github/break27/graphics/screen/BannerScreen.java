/**************************************************************************
 * Copyright (c) 2021 Breakerbear
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *************************************************************************/

package com.github.break27.graphics.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.break27.Game3;

/**
 *
 * @author break27
 */
public class BannerScreen extends AbstractScreen {
    
    public BannerScreen(Game3 game) {
        super(game);
    }
    
    BannerActor banner;
    
    @Override
    public void show() {
        stage = new Stage(defaultViewport);
        banner = new BannerActor(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(banner);
    }

    @Override
    public void render(float delta) {
        // 将背景设为白色
        Gdx.gl.glClearColor(1,1,1,1);
        // 清屏
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        state += Gdx.graphics.getDeltaTime();
        // 6秒后进入加载画面
        if (state >= 6) change(new LoadingScreen(parent));
        // 先绘制 LibGDX 再绘制 CDPT（等待3秒）
        if (state >= 3) {
            banner.switchBanner();
        }
        stage.draw();
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
        banner.dispose();
    }
}

class BannerActor extends Actor {
    Texture texture;
    float x;
    float y;
    
    public BannerActor(float width, float height) {
        this.texture = new Texture(Gdx.files.internal("banner/libgdx.png"));
        // centers the actor itself
        this.x = width/2 - texture.getWidth()/2f;
        this.y = height/2 - texture.getHeight()/2f;
    }
    
    public void switchBanner() {
        this.texture = new Texture(Gdx.files.internal("banner/cdpt_bk337.png"));
    }

    public void dispose() {
        this.texture.dispose();
    }
    
    @Override
    public void draw(Batch batch, float alpha) {
        batch.draw(texture, x, y);
    }
}