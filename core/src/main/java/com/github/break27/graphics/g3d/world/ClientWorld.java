/**************************************************************************
 * Copyright (c) 2022 Breakerbear
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

package com.github.break27.graphics.g3d.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.shaders.DepthShader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.github.break27.game.entity.Player;
import com.github.break27.graphics.g3d.voxel.VoxelModel;
import com.github.break27.graphics.g3d.voxel.VoxelWorld;
import net.mgsx.gltf.scene3d.attributes.PBRCubemapAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.lights.DirectionalLightEx;
import net.mgsx.gltf.scene3d.scene.Scene;
import net.mgsx.gltf.scene3d.scene.SceneManager;
import net.mgsx.gltf.scene3d.scene.SceneSkybox;
import net.mgsx.gltf.scene3d.shaders.PBRDepthShaderProvider;
import net.mgsx.gltf.scene3d.shaders.PBREmissiveShaderProvider;
import net.mgsx.gltf.scene3d.shaders.PBRShaderConfig;
import net.mgsx.gltf.scene3d.utils.IBLBuilder;

/**
 * @author break27
 */
public class ClientWorld extends World {

    public SceneManager sceneManager;

    VoxelDisplay voxelDisplay;
    Player currentPlayer;

    public ClientWorld(Save save) {
        super(save);
        // resolve save file.

        // register player(s)
        PlayerList = new Array<>();
        //PlayerList.addAll(players);
        //PlayerList.add(currentPlayer = player);
    }

    public void initialize() {

    }

    @Override
    public void create() {
        voxelDisplay = new VoxelDisplay(20, 20, 20);

        // configure shader
        PBRShaderConfig config = PBREmissiveShaderProvider.createDefaultConfig();
        config.numBones = 60;
        config.numDirectionalLights = 1;
        config.numPointLights = 0;
        config.defaultCullFace = GL20.GL_FRONT;

        DepthShader.Config depthConfig = PBREmissiveShaderProvider.createDefaultDepthConfig();
        depthConfig.numBones = 60;
        sceneManager = new SceneManager(new PBREmissiveShaderProvider(config), new PBRDepthShaderProvider(depthConfig));

        // setup camera
        sceneManager.setCamera(currentPlayer.Camera);
        Gdx.input.setInputProcessor(currentPlayer.Controller);

        // setup light
        DirectionalLightEx light = new DirectionalLightEx();
        light.direction.set(1, -3, 1).nor();
        light.color.set(Color.WHITE);
        sceneManager.environment.add(light);

        // setup quick IBL (image based lighting)
        IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
        Cubemap environmentCubemap = iblBuilder.buildEnvMap(1024);
        Cubemap diffuseCubemap = iblBuilder.buildIrradianceMap(256);
        Cubemap specularCubemap = iblBuilder.buildRadianceMap(10);
        iblBuilder.dispose();
        Texture brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));

        sceneManager.setAmbientLight(1f);
        sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
        sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));

        // setup skybox
        SceneSkybox skybox = new SceneSkybox(environmentCubemap);
        sceneManager.setSkyBox(skybox);

        // create scene with model
        Scene scene = new Scene(voxelDisplay.model);
        sceneManager.addScene(scene);

        currentPlayer.Camera.position.set(voxelDisplay.camX, voxelDisplay.camY, voxelDisplay.camZ);
    }

    public void render() {

    }

    @Override
    public void update(float delta) {
        super.update(delta);
        currentPlayer.update();

        // update display
        voxelDisplay.update(currentPlayer.getX(), currentPlayer.getY());

        // render
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        sceneManager.update(delta);
        sceneManager.render();
    }
}

class VoxelDisplay {

    Scene scene;
    VoxelWorld world;
    VoxelModel model;

    int ChunkNumX, ChunkNumY, ChunkNumZ;
    float camX, camY, camZ;

    public VoxelDisplay(int visibleChunkX, int visibleChunkY, int visibleChunkZ) {
        MathUtils.random.setSeed(0);
        world = new VoxelWorld(
                ChunkNumX = visibleChunkX,
                ChunkNumY = visibleChunkY,
                ChunkNumZ = visibleChunkZ
        );

        model = new VoxelModel(world);
        camX = world.voxelsX / 2f;
        camZ = world.voxelsZ / 2f;
        camY = world.getHighest(camX, camZ) + 1.5f;

        scene = new Scene(model);
    }

    public void update(float positionX, float positionY) {

    }
}