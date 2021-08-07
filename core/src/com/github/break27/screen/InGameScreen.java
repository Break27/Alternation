/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.github.break27.TodoGame;

/**
 *
 * @author break27
 */
public class InGameScreen implements Screen {
    private TodoGame parent;
    public InGameScreen(TodoGame game) {
        parent = game;
    }
    
    // 声明 3D 环境
    Environment env;
    // 声明透视相机
    PerspectiveCamera cam;
    CameraInputController camController;
    // 模型实例
    ModelInstance modelInstance_block;
    // 模型画笔
    ModelBatch batch;
    // 模型
    Model model_block;
    // 模型构建器
    ModelBuilder modelBuilder;
    
    @Override
    public void show() {
        // 实例化相机
        createCamera();
        // 实例化 3D 环境
        env = new Environment();
        // 设置环境光
        env.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        // 设置直线光源
        env.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        // 实例化模型画笔
        batch = new ModelBatch();
        // 创建模型
        //createModel_block();
        createModel_block_dirt();
    }

    @Override
    public void render(float delta) {
        // 设置视距
        Gdx.gl.glViewport(0, 0, (int) parent.Viewport.getWorldWidth(), (int) parent.Viewport.getWorldHeight());
        // 将背景设为黑色
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        // 更新相机状态
       camController.update();
        // 绘制模型
        batch.begin(cam);
        batch.render(modelInstance_block, env);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        parent.Viewport.update(width, height);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() { }
    
    private void createCamera() {
        // 实例化第一人称视角相机（Y轴视域67度）
        cam = new PerspectiveCamera(67, parent.Viewport.getWorldWidth(), parent.Viewport.getWorldHeight());
        // 设定相机位置
        cam.position.set(10f, 10f, 10f);
        // 设定相机着眼点
        cam.lookAt(0, 0, 0);
        // 近裁剪面赋值
        cam.near = 1f;
        // 远裁剪面赋值
        cam.far = 300f;
        // 更新相机视锥
        cam.update();
        // 注册相机控制器监听
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
    }
    
    private void createModel_block() {
        // 实例化模型构建器
        modelBuilder = new ModelBuilder();
        // 创建模型（方块）X:5 Y:5 Z:5 白色材质
        model_block = modelBuilder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.WHITE)), Usage.Position | Usage.Normal);
        modelInstance_block = new ModelInstance(model_block);
    }
    
    private void createModel_block_dirt() {
        Model model_block_dirt = parent.Manager.get("model/dirt.obj", Model.class);
        modelInstance_block = new ModelInstance(model_block_dirt);
    }
    
}
