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
    
    // ���� 3D ����
    Environment env;
    // ����͸�����
    PerspectiveCamera cam;
    CameraInputController camController;
    // ģ��ʵ��
    ModelInstance modelInstance_block;
    // ģ�ͻ���
    ModelBatch batch;
    // ģ��
    Model model_block;
    // ģ�͹�����
    ModelBuilder modelBuilder;
    
    @Override
    public void show() {
        // ʵ�������
        createCamera();
        // ʵ���� 3D ����
        env = new Environment();
        // ���û�����
        env.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        // ����ֱ�߹�Դ
        env.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        // ʵ����ģ�ͻ���
        batch = new ModelBatch();
        // ����ģ��
        //createModel_block();
        createModel_block_dirt();
    }

    @Override
    public void render(float delta) {
        // �����Ӿ�
        Gdx.gl.glViewport(0, 0, (int) parent.Viewport.getWorldWidth(), (int) parent.Viewport.getWorldHeight());
        // ��������Ϊ��ɫ
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        // �������״̬
       camController.update();
        // ����ģ��
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
        // ʵ������һ�˳��ӽ������Y������67�ȣ�
        cam = new PerspectiveCamera(67, parent.Viewport.getWorldWidth(), parent.Viewport.getWorldHeight());
        // �趨���λ��
        cam.position.set(10f, 10f, 10f);
        // �趨������۵�
        cam.lookAt(0, 0, 0);
        // ���ü��渳ֵ
        cam.near = 1f;
        // Զ�ü��渳ֵ
        cam.far = 300f;
        // ���������׶
        cam.update();
        // ע���������������
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);
    }
    
    private void createModel_block() {
        // ʵ����ģ�͹�����
        modelBuilder = new ModelBuilder();
        // ����ģ�ͣ����飩X:5 Y:5 Z:5 ��ɫ����
        model_block = modelBuilder.createBox(5f, 5f, 5f, new Material(ColorAttribute.createDiffuse(Color.WHITE)), Usage.Position | Usage.Normal);
        modelInstance_block = new ModelInstance(model_block);
    }
    
    private void createModel_block_dirt() {
        Model model_block_dirt = parent.Manager.get("model/dirt.obj", Model.class);
        modelInstance_block = new ModelInstance(model_block_dirt);
    }
    
}
