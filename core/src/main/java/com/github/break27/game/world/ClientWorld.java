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

package com.github.break27.game.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.shaders.DepthShader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Predicate;
import com.github.break27.game.entity.AlterEntity;
import com.github.break27.game.entity.Player;
import com.github.break27.game.entity.component.PositionComponent;
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
    public SceneManager SceneManager;
    public VoxelWorld VoxelWorld;

    double visibleRadius = 10.0d;
    VoxelModel model;
    Player currentPlayer;

    public ClientWorld(Save save) {
        super(save);
        // resolve save file.

        // register player(s)
        //PlayerList.addAll(players);
        //PlayerList.add(currentPlayer = player);
    }

    public void initialize() {

    }

    @Override
    public void create() {
        MathUtils.random.setSeed(0);
        VoxelWorld = new VoxelWorld(20, 20 ,20);
        model = new VoxelModel(VoxelWorld);

        // configure shader
        PBRShaderConfig config = PBREmissiveShaderProvider.createDefaultConfig();
        config.numBones = 60;
        config.numDirectionalLights = 1;
        config.numPointLights = 0;
        config.defaultCullFace = GL20.GL_FRONT;

        DepthShader.Config depthConfig = PBREmissiveShaderProvider.createDefaultDepthConfig();
        depthConfig.numBones = 60;
        SceneManager = new SceneManager(new PBREmissiveShaderProvider(config), new PBRDepthShaderProvider(depthConfig));

        // setup camera
        SceneManager.setCamera(currentPlayer.Camera);
        Gdx.input.setInputProcessor(currentPlayer.Controller);

        // setup light
        DirectionalLightEx light = new DirectionalLightEx();
        light.direction.set(1, -3, 1).nor();
        light.color.set(Color.WHITE);
        SceneManager.environment.add(light);

        // setup quick IBL (image based lighting)
        IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
        Cubemap environmentCubemap = iblBuilder.buildEnvMap(1024);
        Cubemap diffuseCubemap = iblBuilder.buildIrradianceMap(256);
        Cubemap specularCubemap = iblBuilder.buildRadianceMap(10);
        iblBuilder.dispose();
        Texture brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));

        SceneManager.setAmbientLight(1f);
        SceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        SceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
        SceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));

        // setup skybox
        SceneSkybox skybox = new SceneSkybox(environmentCubemap);
        SceneManager.setSkyBox(skybox);

        currentPlayer.Camera.position.set(VoxelWorld.getVisibleChunkRadiusXZ(),
                VoxelWorld.getVisibleChunkRadiusY(),
                VoxelWorld.getVisibleChunkRadiusXZ());
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        Vector3 playerPos = currentPlayer.getComponent(PositionComponent.class).Position;

        Entities.select(entity -> {
            Vector3 pos = entity.getComponent(PositionComponent.class).Position;

            return visibilityCheck(playerPos.x, pos.x)
                    && visibilityCheck(playerPos.y, pos.y)
                    && visibilityCheck(playerPos.z, pos.z);
        });
    }

    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        SceneManager.update(delta);
        SceneManager.render();
    }

    @Override
    public void dispose() {
        SceneManager.dispose();
    }

    private boolean visibilityCheck(float pos1, float pos2) {
        return (pos1 + visibleRadius) >= pos2 || (pos1 - visibleRadius) <= pos2;
    }
}