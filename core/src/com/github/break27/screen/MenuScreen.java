/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author break27
 */
package com.github.break27.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.github.break27.TodoGame;
import com.github.break27.graphics.AboutPage;

public class MenuScreen implements Screen{
    private TodoGame parent;
    public MenuScreen(TodoGame game){
        parent = game;
    }
    // ������̨
    private Stage stage;
    // ���� skin
    private Skin skin_ui;
    // �����ָ����
    private SplitPane split_pane_right;
    private SplitPane split_pane_left;
    // �����������
    private ScrollPane scroll_pane_right;
    private ScrollPane scroll_pane_left;
    // �����б��ң�
    private List list_right;
    private Object[] listEntries_sp = {"Save 01", "Save 02", "Save 03"};
    private Object[] listEntries_mp = {"Server 01", "Server 02", "Server 03"};
    private Object[] listEntries_op = {"Option #1", "Option #2", "Option #3"};
    // ���������ڡ�ҳ�漰��ؼ�
    private AboutPage g_about_page;
    private Table table_image;
    // �����б���
    private List list_left;
    private Object[] listEntries_left = {"Single Player", "Multiplayer", "Options", "About"};
    // �������ֿ�
    private TextField text_field;
    // �������
    private Table table;
    // ������񱳾� Pixmap
    private Pixmap bg_table;
    // �������밴ť
    private Button button_start;
    // �������밴ť����
    private Label label_start;
    // ���� demo ��ť
    private Button button_demo;
    // ���� demo ��ť����
    private Label label_demo;
    // ������ť���
    private Table table_button;
    
    // �����б������ݸı���
    private boolean listOnChange_left = false;
    // �������������ɱ��
    private boolean isScreenLoaded = false;
    // ������Ⱦʱ��
    float stateTime;
    // �����б��ң�����
    int list_right_index;
    // �����б�������
    int list_left_index;
    
    @Override
    public void show() {
        skin_ui = parent.Manager.get("ui/uiskin.json", Skin.class);
        stage = new Stage(parent.Viewport);
        // ע�����
        Gdx.input.setInputProcessor(stage);
        // debug
        table_image = new Table();
        // ʵ�����б�
        createList_left();
        createList_right();
        // ʵ�������������ң�
        scroll_pane_right = new ScrollPane(list_right, skin_ui);
        // ʵ��������������
        scroll_pane_left = new ScrollPane(list_left, skin_ui);
        // ���ù�����Ϊ���ɻص�
        scroll_pane_right.setFlickScroll(false);
        scroll_pane_left.setFlickScroll(false);
        // �������
        createTable();
        // ʵ���������ڡ�ҳ��
        g_about_page = new AboutPage(parent);
        // ʵ�����ָ���壨�ң�
        split_pane_right = new SplitPane(scroll_pane_right, table, true, skin_ui);
        // ʵ�����ָ���壨��
        split_pane_left = new SplitPane(scroll_pane_left, split_pane_right, false, skin_ui);
        // �������ָ�������Ϊ 0.33 ��ԼΪ����֮һ��Ļ��ȣ�
        split_pane_left.setMinSplitAmount(0.33f);
        split_pane_left.setMaxSplitAmount(0.33f);
        // ���������С�ָ�������Ϊ 0.25 ��ԼΪ�ķ�֮һ��Ļ��
        split_pane_right.setMinSplitAmount(0.25f);
        // ��������С
        split_pane_left.setSize(parent.Viewport.getWorldWidth(), parent.Viewport.getWorldHeight());
        /*
         * ���֣�
         * �ָ���壨�󣩣�{ �б��󣩣� �ָ���壨�ң�����ֱ�� }
         * �ָ���壨�ң���{ �б��ң����ϲ����� ����²��� }
         * ���{ ���ֿ��ϲ����� ��ť���²��� }
        */
        // ��ӵ���̨
        stage.addActor(split_pane_left);
        // �������
        isScreenLoaded = true;
    }

    @Override
    public void render(float delta) {
        // ������Դ�������ʱ��ʼ��Ⱦ
        if ( isScreenLoaded ) {
            // ��������Ϊ�ɺ�ɫ����Ϊ��ɫ��1.75�룩
            if (stateTime < 1.75f) stateTime += delta;
            Gdx.gl.glClearColor(stateTime/1.75f,stateTime/1.75f,stateTime/1.75f,stateTime/1.75f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            // ��嶯��������
            split_pane_left.setColor(0, 0, 0, stateTime/1.75f);
            stage.act();
            stage.draw();
        }
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
    
    private void createList_left() {
        list_left = new List(skin_ui);
        list_left.setItems(listEntries_left);
        list_left.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                /*
                 * �б�����Ŀ������
                 * 0��������Ϸ
                 * 1��������Ϸ
                 * 2����Ϸѡ��
                 * 3������
                 */
                list_left_index = list_left.getSelectedIndex();
                listOnChange_left = true;
                // �������Ĭ����Ϊ�б�
                setAboutPage(false);
                // ����б��ң�����
                switch (list_left_index) {
                    case 0:
                        list_right.setItems(listEntries_sp);
                        break;
                    case 1:
                        list_right.setItems(listEntries_mp);
                        break;
                    case 2:
                        list_right.setItems(listEntries_op);
                        break;
                    case 3:
                        // ��ȡ�����
                        setAboutPage(true);
                        break;
                }
                // �����ı�������
                text_field.setText("Demonstration Text");
                listOnChange_left = false;
            }
        });
    }
    
    private void createList_right() {
        list_right = new List(skin_ui);
        list_right.setItems(listEntries_sp);
        // ���б���ΪĬ��δѡ״̬
        list_right.setSelectedIndex(-1);
        // ��Ӽ����� �б��󣩿����б��ң�
        list_right.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                /*
                 * �б��ң��������������
                 * ���б�����Ŀ����Ϊ 0 ʱ��
                 * �б��ң���Ŀ������
                 * 0���浵 01
                 * 1���浵 02
                 * 2���浵 03
                 * 
                 */
                list_right_index = list_right.getSelectedIndex();
                // �ж��б����Ƿ����仯
                if (listOnChange_left) {
                    list_right_index = -1;
                    list_right.setSelectedIndex(list_right_index);
                }
                switch (list_left_index) {
                    case 0:
                        switch (list_right_index) {
                            // ��ѡ����Ŀ��������������ı��ı�������
                            case 0:
                                text_field.setText("Save Slot 01");
                                break;
                            case 1:
                                text_field.setText("Save Slot 02");
                                break;
                            case 2:
                                text_field.setText("Save Slot 03");
                                break;
                        }
                        break;
                }
            }
        });
    }
    
    private void createTable() {
        // ʵ�������
        table = new Table();
        // ʵ�������ֿ�
        text_field = new TextField("Demonstration Text.", skin_ui);
        // �����ֿ���Ϊ����д��
        text_field.setDisabled(true);
        // �����ֿ���ӵ�����ö������
        table.add(text_field).align(Align.top).expand().fill();
        table.row(); // �½���
        // ʵ������ť���
        table_button = new Table();
        // ʵ����demo��ť
        button_demo = new Button(skin_ui);
        // ʵ����demo��ť����
        label_demo = new Label("Demo", skin_ui);
        button_demo.add(label_demo);
        // ��Ӱ�ť������
        button_demo.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int pointer, int button) {
                parent.changeScreen(parent.DEMO);
            }
        });
        // ����ť��ӵ���񣬲�����Ϊ 2
        table_button.add(button_demo).padRight(2f);
        // ʵ�������밴ť
        button_start = new Button(skin_ui);
        // ʵ�������밴ť����
        label_start = new Label("Load", skin_ui);
        button_start.add(label_start);
        // ��Ӱ�ť������
        button_start.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int pointer, int button) {
                parent.changeScreen(parent.GAME);
            }
        });
        // ����ť��ӵ����
        table_button.add(button_start);
        // ����ť����ӵ���񲢶����ڵײ�����
        table.add(table_button).align(Align.bottomRight);
        // ʵ���� Pixmap
        bg_table = new Pixmap(1, 1, Pixmap.Format.RGB565);
        // ��������Ϊ��ɫ
        bg_table.setColor(Color.GRAY);
        bg_table.fill();
        table.setBackground(new TextureRegionDrawable(new Texture(bg_table)));
    }
    
    private void setAboutPage(boolean enable) {
        if ( enable ) {
            // ��ȡ�����
            float w = scroll_pane_right.getWidth();
            // ��Ⱦҳ�棨�������������ȱ�ֵ 21/426��
            g_about_page.setSize((int)(w-0.05f*w), (int)parent.Viewport.getWorldHeight());
            // ��ͼ����ӵ����
            table_image.add(g_about_page.getImage());
            // �������ӵ����
            scroll_pane_right.setActor(table_image);
        } else {
            // ������
            table_image.clear();
            // ��ԭ�������
            scroll_pane_right.setActor(list_right);
        }
    }
    
}
