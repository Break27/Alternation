/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.github.break27.TodoGame3;
import com.github.break27.system.ResourceLoader;
import net.mgsx.gltf.loaders.glb.GLBAssetLoader;
import net.mgsx.gltf.loaders.gltf.GLTFAssetLoader;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

/**
 *
 * @author break27
 */
public class LoadingScreen extends AbstractScreen {
    
    public LoadingScreen(TodoGame3 game) {
        super(game);
    }
    
    @Override
    public int getId() {
        return ScreenType.LOADING;
    }
    
    @Override
    public void show() {
        initGltfLoader();
        ResourceLoader.loadDefault();
    }

    @Override
    public void render(float delta) {
        parent.Asset.update();
        // 将背景设为黑色
        Gdx.gl.glClearColor(0,0,0,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        state += Gdx.graphics.getDeltaTime();
        // To be removed
        if (state > 1) super.change(new MainScreen(parent));
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
    
    private void initGltfLoader() {
        // 设定 GLTF 模型加载器
        parent.Asset.setLoader(SceneAsset.class, ".gltf", new GLTFAssetLoader());
        parent.Asset.setLoader(SceneAsset.class, ".glb", new GLBAssetLoader());
    }
}
