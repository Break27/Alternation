/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.io.File;

import net.mgsx.gltf.loaders.glb.GLBAssetLoader;
import net.mgsx.gltf.loaders.gltf.GLTFAssetLoader;
import net.mgsx.gltf.scene3d.scene.SceneAsset;

/**
 *
 * @author break27
 */
public class GameAssets {
    private static AssetManager Manager;
    
    public static FileHandle GameDocRoot;
    
    public static void setManager(AssetManager manager) {
        Manager = manager;
    }
    
    public static void setDefaultPath(String extRoot) {
        GameDocRoot = new FileHandle(extRoot + "Documents/Todo Game");
    }
    
    public static File getTmpFileRoot(String filename) {
        return new File(FileHandle.tempDirectory("CDPT").file().getAbsolutePath() + "/" + filename);
    }
    
    /**
     * @return Random Access Temporary File
     */
    public static File getRAT_File() {
        return FileHandle.tempFile("CDPT").file();
    }
    
    public static File getGameDocFileRoot(String filename) {
        return new File(GameDocRoot.file().getAbsolutePath() + "/" + filename);
    }
    
    public static boolean load() {
        /** MenuScreen.class **/
        Manager.load("ui/uiskin.json", Skin.class);
        /** InGameScreen.class **/
        Manager.load("model/block_dirt.gltf", SceneAsset.class);
        Manager.load("model/block_grass.gltf", SceneAsset.class);
        Manager.finishLoading();
        return true;
    }
    
    public static boolean diagnose() {
        /** MenuScreen.class **/
        if ( !Manager.isLoaded("ui/uiskin.json") ) return false;
        return true;
    }
    
    public static void unload() {
        if( Manager != null ) Manager.dispose();
    }
    
    public static void initGLTFLoader() {
        // 设定 GLTF 模型加载器
        Manager.setLoader(SceneAsset.class, ".gltf", new GLTFAssetLoader());
        Manager.setLoader(SceneAsset.class, ".glb", new GLBAssetLoader());
    }
}
