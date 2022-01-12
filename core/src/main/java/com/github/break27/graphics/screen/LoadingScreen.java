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
import com.github.break27.Game3;
import com.github.break27.system.Resource;
import net.mgsx.gltf.loaders.glb.GLBAssetLoader;
import net.mgsx.gltf.loaders.gltf.GLTFAssetLoader;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

/**
 *
 * @author break27
 */
public class LoadingScreen extends AbstractScreen {
    
    public LoadingScreen(Game3 game) {
        super(game);
    }
    
    @Override
    public void show() {
        initGltfLoader();
        Resource.loadDefault(parent.Asset);
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