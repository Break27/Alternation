/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.g3d.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Cubemap;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.shaders.DepthShader;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.MathUtils;
import com.github.break27.graphics.g3d.voxel.PerlinNoiseGenerator;
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
 *
 * @author break27
 */
public class MapDisplay {
    VoxelModel model;
    
    int ChunkNumX;
    int ChunkNumY;
    int ChunkNumZ;
    
    public MapDisplay(int visibleChunkX, int visibleChunkY, int visibleChunkZ) {
        model = new VoxelModel(new VoxelWorld(
                ChunkNumX = visibleChunkX, 
                ChunkNumY = visibleChunkY, 
                ChunkNumZ = visibleChunkZ));
        create();
    }
    
    ModelBatch modelBatch;
    PerspectiveCamera camera;
    Environment lights;
    FirstPersonCameraController controller;
    VoxelWorld voxelWorld;
    
    private SceneManager sceneManager;
    private Cubemap diffuseCubemap;
    private Cubemap environmentCubemap;
    private Cubemap specularCubemap;
    private Texture brdfLUT;
    private SceneSkybox skybox;
    private DirectionalLightEx light;
    
    private void create() {
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
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.near = 0.5f;
        camera.far = 1000;
        controller = new FirstPersonCameraController(camera);
        Gdx.input.setInputProcessor(controller);
        sceneManager.setCamera(camera);

        // setup light
        light = new DirectionalLightEx();
        light.direction.set(1, -3, 1).nor();
        light.color.set(Color.WHITE);
        sceneManager.environment.add(light);

        // setup quick IBL (image based lighting)
        IBLBuilder iblBuilder = IBLBuilder.createOutdoor(light);
        environmentCubemap = iblBuilder.buildEnvMap(1024);
        diffuseCubemap = iblBuilder.buildIrradianceMap(256);
        specularCubemap = iblBuilder.buildRadianceMap(10);
        iblBuilder.dispose();
        brdfLUT = new Texture(Gdx.files.classpath("net/mgsx/gltf/shaders/brdfLUT.png"));

        sceneManager.setAmbientLight(1f);
        sceneManager.environment.set(new PBRTextureAttribute(PBRTextureAttribute.BRDFLUTTexture, brdfLUT));
        sceneManager.environment.set(PBRCubemapAttribute.createSpecularEnv(specularCubemap));
        sceneManager.environment.set(PBRCubemapAttribute.createDiffuseEnv(diffuseCubemap));

        // setup skybox
        skybox = new SceneSkybox(environmentCubemap);
        sceneManager.setSkyBox(skybox);
        
        MathUtils.random.setSeed(0);
        voxelWorld = new VoxelWorld(10, 2, 10);
        PerlinNoiseGenerator.generateVoxels(voxelWorld, 0, 63, 10);
        Scene scene = new Scene(new VoxelModel(voxelWorld));
        sceneManager.addScene(scene);
        float camX = voxelWorld.voxelsX / 2f;
        float camZ = voxelWorld.voxelsZ / 2f;
        float camY = voxelWorld.getHighest(camX, camZ) + 1.5f;
        camera.position.set(camX, camY, camZ);
    }
    
    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        controller.update();

        // render
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        sceneManager.update(deltaTime);
        sceneManager.render();
    }
    
    public void resize(int width, int height) {
        sceneManager.updateViewport(width, height);
    }
}
