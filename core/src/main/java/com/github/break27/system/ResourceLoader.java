/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.github.break27.graphics.ui.AlternativeFont;
import com.github.break27.graphics.ui.AlternativeSkin;
import com.github.break27.launcher.LauncherAdapter;
import com.kotcrab.vis.ui.VisUI;

/**
 *
 * @author break27
 */
public class ResourceLoader {
    
    static XmlReader reader = new XmlReader();
    static boolean read = false;
    
    public static void loadDefault() {
        loadManifest(Gdx.files.internal("manifest.xml"));
        loadVisUI();
    }
    
    public static void loadManifest(FileHandle manifest) {
        findResources(manifest).forEach(element -> {
            String name = element.getAttribute("name", null);
            String type = element.getAttribute("type");
            FileHandle sibling = manifest.sibling(element.getAttribute("location"));
            if(type.equals("manifest")) {
                loadManifest(sibling);
            } else if(name != null) {
                /* Font */
                if(type.equals("font")) {
                    FreeTypeFontParameter parameter = new FreeTypeFontParameter();
                    // Font size
                    parameter.size = Integer.valueOf(element.getAttribute("size", "12"));
                    // Inherited and Extended Characters
                    if(element.getParent().hasChild("inherit")) 
                        parameter.characters = element.getParent().getChildByName("inherit").getText();
                    if(element.hasChild("extended")) 
                        parameter.characters += element.getChildByName("extended").getText();
                    
                    loadFont(name, sibling, parameter);
                }
                /* Skin */
                if(type.equals("skin")) loadSkin(name, sibling);
            }
        });
    }
    
    public static void dispose() {
        VisUI.dispose();
        Resource.dispose();
    }
    
    private static void loadSkin(String name, FileHandle skin) {
        if(skin.exists())
            Resource.loadSkin(name, new AlternativeSkin(skin));
        else
            Gdx.app.log(ResourceLoader.class.getName(), "Skin file \"" + skin + 
                    "\" does not exist! Skin \"" + name + "\" is not loaded.");
    }
    
    private static void loadFont(String name, FileHandle ttf, FreeTypeFontParameter parameter) {
        if(ttf.exists()) {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(ttf);
            // Load in default characters
            parameter.characters += FreeTypeFontGenerator.DEFAULT_CHARS;
            // Generate Font
            BitmapFont font = generator.generateFont(parameter);
            generator.dispose();
            Resource.loadFont(name, new AlternativeFont(font.getData(), font.getRegions(), true));
        } else {
            Gdx.app.log(ResourceLoader.class.getName(), "Font file \"" + ttf + 
                    "\" does not exist! Skin \"" + name + "\" is not loaded.");
        }
    }
    
    private static void loadVisUI() {
        if(Resource.has(Resource.DefaultType.SKIN, "visui"))
            VisUI.load(Resource.getSkin("visui"));
        else
            VisUI.load();
    }
    
    private static Array<Element> findResources(FileHandle file) {
        Element element = reader.parse(file);
        loadMetaInfo(element.getChildByName("meta"));
        return element.getChildByName("list").getChildrenByNameRecursively("resource");
    }
    
    private static void loadMetaInfo(Element metainfo) {
        if(!read) {
            /* Name */
            Element Ename = metainfo.getChildByName("name");
            String name = Ename == null ? "Resource Set" : Ename.getText();
            /* Version */
            Element Eversion = metainfo.getChildByName("version");
            if(Eversion != null) {
                String val = Eversion.getText().replace(".", "");
                // Only numbers are accepted.
                if(val.matches("^[0-9]{1,}$")) {
                    int version = Integer.parseInt(val);
                    int gamever = Integer.parseInt(LauncherAdapter.VERSION.replace(".", ""));
                    if(version < gamever) Gdx.app.log(ResourceLoader.class.getName(), "Currently loading a resource set "
                            + "from an older version of the game. Some texture might be unable to display.");
                    if(version > gamever) Gdx.app.log(ResourceLoader.class.getName(), "Currently loading a resource set "
                            + "from a newer version of the game.");
                } else {
                    Gdx.app.log(ResourceLoader.class.getName(), "Version element of the resource set is unreadable, ignored.");
                }
            }
            /* Authors */
            Element Eauthors = metainfo.getChildByName("authors");
            if(Eauthors != null) {
                Array<String> authors = new Array();
                Eauthors.getChildrenByNameRecursively("author").forEach(author -> {
                    authors.add(author.getText());
                });
            }
            read = true;
        }
    }
}