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
    // 声明舞台
    private Stage stage;
    // 声明 skin
    private Skin skin_ui;
    // 声明分割面板
    private SplitPane split_pane_right;
    private SplitPane split_pane_left;
    // 声明滚动面板
    private ScrollPane scroll_pane_right;
    private ScrollPane scroll_pane_left;
    // 声明列表（右）
    private List list_right;
    private Object[] listEntries_sp = {"Save 01", "Save 02", "Save 03"};
    private Object[] listEntries_mp = {"Server 01", "Server 02", "Server 03"};
    private Object[] listEntries_op = {"Option #1", "Option #2", "Option #3"};
    // 声明“关于”页面及其控件
    private AboutPage g_about_page;
    private Table table_image;
    // 声明列表（左）
    private List list_left;
    private Object[] listEntries_left = {"Single Player", "Multiplayer", "Options", "About"};
    // 声明文字框
    private TextField text_field;
    // 声明表格
    private Table table;
    // 声明表格背景 Pixmap
    private Pixmap bg_table;
    // 声明载入按钮
    private Button button_start;
    // 声明载入按钮文字
    private Label label_start;
    // 声明 demo 按钮
    private Button button_demo;
    // 声明 demo 按钮文字
    private Label label_demo;
    // 声明按钮表格
    private Table table_button;
    
    // 声明列表（左）内容改变标记
    private boolean listOnChange_left = false;
    // 声明界面加载完成标记
    private boolean isScreenLoaded = false;
    // 声明渲染时间
    float stateTime;
    // 声明列表（右）索引
    int list_right_index;
    // 声明列表（左）索引
    int list_left_index;
    
    @Override
    public void show() {
        skin_ui = parent.Manager.get("ui/uiskin.json", Skin.class);
        stage = new Stage(parent.Viewport);
        // 注册监听
        Gdx.input.setInputProcessor(stage);
        // debug
        table_image = new Table();
        // 实例化列表
        createList_left();
        createList_right();
        // 实例化滚动条（右）
        scroll_pane_right = new ScrollPane(list_right, skin_ui);
        // 实例化滚动条（左）
        scroll_pane_left = new ScrollPane(list_left, skin_ui);
        // 设置滚动条为不可回弹
        scroll_pane_right.setFlickScroll(false);
        scroll_pane_left.setFlickScroll(false);
        // 创建表格
        createTable();
        // 实例化“关于”页面
        g_about_page = new AboutPage(parent);
        // 实例化分割面板（右）
        split_pane_right = new SplitPane(scroll_pane_right, table, true, skin_ui);
        // 实例化分割面板（左）
        split_pane_left = new SplitPane(scroll_pane_left, split_pane_right, false, skin_ui);
        // 将左面板分割量锁定为 0.33 （约为三分之一屏幕宽度）
        split_pane_left.setMinSplitAmount(0.33f);
        split_pane_left.setMaxSplitAmount(0.33f);
        // 将右面板最小分割量锁定为 0.25 （约为四分之一屏幕）
        split_pane_right.setMinSplitAmount(0.25f);
        // 设置面板大小
        split_pane_left.setSize(parent.Viewport.getWorldWidth(), parent.Viewport.getWorldHeight());
        /*
         * 布局：
         * 分割面板（左）：{ 列表（左）， 分割面板（右）【垂直】 }
         * 分割面板（右）：{ 列表（右）【上部】， 表格【下部】 }
         * 表格：{ 文字框【上部】， 按钮【下部】 }
        */
        // 添加到舞台
        stage.addActor(split_pane_left);
        // 加载完毕
        isScreenLoaded = true;
    }

    @Override
    public void render(float delta) {
        // 仅当资源加载完毕时开始渲染
        if ( isScreenLoaded ) {
            // 将背景设为由黑色渐变为白色（1.75秒）
            if (stateTime < 1.75f) stateTime += delta;
            Gdx.gl.glClearColor(stateTime/1.75f,stateTime/1.75f,stateTime/1.75f,stateTime/1.75f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            // 面板动画：淡入
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
                 * 列表（左）项目索引：
                 * 0：单人游戏
                 * 1：多人游戏
                 * 2：游戏选项
                 * 3：关于
                 */
                list_left_index = list_left.getSelectedIndex();
                listOnChange_left = true;
                // 将右面板默认设为列表
                setAboutPage(false);
                // 变更列表（右）内容
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
                        // 获取面板宽度
                        setAboutPage(true);
                        break;
                }
                // 重置文本框内容
                text_field.setText("Demonstration Text");
                listOnChange_left = false;
            }
        });
    }
    
    private void createList_right() {
        list_right = new List(skin_ui);
        list_right.setItems(listEntries_sp);
        // 将列表设为默认未选状态
        list_right.setSelectedIndex(-1);
        // 添加监听器 列表（左）控制列表（右）
        list_right.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                /*
                 * 列表（右）控制右面板内容
                 * 当列表（左）项目索引为 0 时：
                 * 列表（右）项目索引：
                 * 0：存档 01
                 * 1：存档 02
                 * 2：存档 03
                 * 
                 */
                list_right_index = list_right.getSelectedIndex();
                // 判断列表（左）是否发生变化
                if (listOnChange_left) {
                    list_right_index = -1;
                    list_right.setSelectedIndex(list_right_index);
                }
                switch (list_left_index) {
                    case 0:
                        switch (list_right_index) {
                            // 当选中项目索引发生变更，改变文本框内容
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
        // 实例化表格
        table = new Table();
        // 实例化文字框
        text_field = new TextField("Demonstration Text.", skin_ui);
        // 将文字框设为不可写入
        text_field.setDisabled(true);
        // 将文字框添加到表格，置顶并填充
        table.add(text_field).align(Align.top).expand().fill();
        table.row(); // 新建行
        // 实例化按钮表格
        table_button = new Table();
        // 实例化demo按钮
        button_demo = new Button(skin_ui);
        // 实例化demo按钮文字
        label_demo = new Label("Demo", skin_ui);
        button_demo.add(label_demo);
        // 添加按钮监听器
        button_demo.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int pointer, int button) {
                parent.changeScreen(parent.DEMO);
            }
        });
        // 将按钮添加到表格，并设间距为 2
        table_button.add(button_demo).padRight(2f);
        // 实例化载入按钮
        button_start = new Button(skin_ui);
        // 实例化载入按钮文字
        label_start = new Label("Load", skin_ui);
        button_start.add(label_start);
        // 添加按钮监听器
        button_start.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent e, float x, float y, int pointer, int button) {
                parent.changeScreen(parent.GAME);
            }
        });
        // 将按钮添加到表格
        table_button.add(button_start);
        // 将按钮组添加到表格并对齐于底部右下
        table.add(table_button).align(Align.bottomRight);
        // 实例化 Pixmap
        bg_table = new Pixmap(1, 1, Pixmap.Format.RGB565);
        // 将背景设为灰色
        bg_table.setColor(Color.GRAY);
        bg_table.fill();
        table.setBackground(new TextureRegionDrawable(new Texture(bg_table)));
    }
    
    private void setAboutPage(boolean enable) {
        if ( enable ) {
            // 获取面板宽度
            float w = scroll_pane_right.getWidth();
            // 渲染页面（画面与滚动条宽度比值 21/426）
            g_about_page.setSize((int)(w-0.05f*w), (int)parent.Viewport.getWorldHeight());
            // 将图像添加到表格
            table_image.add(g_about_page.getImage());
            // 将表格添加到面板
            scroll_pane_right.setActor(table_image);
        } else {
            // 清除表格
            table_image.clear();
            // 还原面板内容
            scroll_pane_right.setActor(list_right);
        }
    }
    
}
