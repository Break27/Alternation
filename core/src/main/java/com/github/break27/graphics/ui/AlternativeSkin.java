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

package com.github.break27.graphics.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.github.break27.system.misc.SkinBuilder;

/**
 *
 * @author break27
 */
public class AlternativeSkin extends Skin {
    
    Array<TextureAtlas> AtlasSet = new Array<>();
    
    public AlternativeSkin() {
    }
    
    public AlternativeSkin(FileHandle file) {
        load(file);
    }

    public BitmapFont getDefaultFont() {
        return getFont("default-font");
    }

    @Override
    public Drawable getDrawable(String name) {
        try {
            return super.getDrawable(name);
        } catch(GdxRuntimeException ignored) {
        }
        // fallback
        Gdx.app.log(getClass().getName(), "No Resource Found: " + name + ". Using fallback.");
        return super.getDrawable("icon20-question");
    }
    
    @Override
    public void load (FileHandle xmlFile) {
        String jsonString = jsonBuild(xmlFile);
        try {
            getJsonLoader(xmlFile).fromJson(Skin.class, jsonString);
        } catch (SerializationException ex) {
            throw new SerializationException("Error reading file: " + xmlFile, ex);
        }
    }
    
    @Override
    public void addRegions(TextureAtlas atlas) {
        super.addRegions(atlas);
        AtlasSet.add(atlas);
    }
    
    @Override
    public void dispose() {
        super.dispose();
        // dispose all atlases
        for(int i=0; i<AtlasSet.size; i++) {
            AtlasSet.pop().dispose();
        }
    }

    private void loadFontXml(FileHandle xmlFile, XmlReader reader, final Array<FileHandle> list) {
        /* Load XML */
        Element parent = reader.parse(xmlFile);
        parent.getChildrenByNameRecursively("resource").forEach(font -> {
            // get attributes
            String name = font.getAttribute("name");
            String location = font.getAttribute("location");
            FileHandle sibling = xmlFile.sibling(location);

            // prevent from duplicated loading
            if(list.contains(sibling, false))
                throw new SerializationException(xmlFile + ": Resource has already loaded: " + sibling);
            else
                list.add(sibling);

            /* Load Params */
            FreeTypeFontParameter parameter = new FreeTypeFontParameter();
            // Font size
            int size = 12;
            if(font.hasChild("size"))
                size = Integer.parseInt(font.getChildByName("size").getText());
            parameter.size = size;
            // load Inherited and Extended Characters
            if(parent.hasChild("inherit"))
                parameter.characters = parent.getChildByName("inherit").getText();
            if(font.hasChild("extended"))
                parameter.characters += font.getChildByName("extended").getText();

            /* Load Font */
            loadFont(name, sibling, parameter);
        });
    }

    private void loadFont(String name, FileHandle fontFile, FreeTypeFontParameter parameter) {
        if(!fontFile.exists()) {
            throw new GdxRuntimeException(fontFile + ": TrueTypeFont resource unavailable: File not found.");
        } else {
            /* Generate Font */
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
            // Load default characters
            parameter.characters += FreeTypeFontGenerator.DEFAULT_CHARS;
            // generate
            BitmapFont font = generator.generateFont(parameter);
            generator.dispose();
            /* load font to skin */
            add(name, font, BitmapFont.class);
        }
    }

    private void loadJson(FileHandle file) {
        // load atlas
        FileHandle atlasFile = file.sibling(file.nameWithoutExtension() + ".atlas");
        if (atlasFile.exists()) addRegions(new TextureAtlas(atlasFile));
        // load json
        super.load(file);
    }

    private String jsonBuild(FileHandle xmlFile) {
        XmlReader reader = new XmlReader();
        Json json = new Json();
        // build json string
        SkinBuilder builder = parseXml(xmlFile, reader, new SkinBuilder(), new Array<>());
        return json.toJson(builder);
    }
    
    private SkinBuilder parseXml(FileHandle xmlFile, XmlReader reader, final SkinBuilder builder,
                                 final Array<FileHandle> list) {
        Element root = reader.parse(xmlFile);
        /* Includes */
        if(root.hasChild("include")) {
            Array<Element> includes = root.getChildrenByNameRecursively("include");
            includes.forEach(element -> {
                FileHandle file = null;
                if(element.hasAttribute("internal"))
                    file = Gdx.files.internal(element.getAttribute("internal"));
                else if(element.hasAttribute("external"))
                    file = Gdx.files.external(element.getAttribute("external"));
                else if(element.hasAttribute("classpath"))
                    file = Gdx.files.classpath(element.getAttribute("classpath"));
                
                if(file != null && file.exists()) {
                    // prevent from infinite recursive loop
                    if(file.equals(xmlFile)) 
                        Gdx.app.error(getClass().getName(), "Unsupported Operation: File is including itself! Ignored.");
                    else if(list.contains(file, false)) 
                        throw new SerializationException(file + ": Target file has already loaded: " + xmlFile);
                    else {
                        list.add(file);
                        if(file.extension().equals("xml"))
                            parseXml(file, reader, builder, list);
                        else if(file.extension().equals("json"))
                            // load json (compatible with the original)
                            loadJson(file);
                        else Gdx.app.error(getClass().getName(), xmlFile + ": Invalid Include File: "
                                    + file + ". Ignored.");
                    }
                } else {
                    Gdx.app.error(getClass().getName(), xmlFile + ": Include file not found: " + file + ". Ignored.");
                }
            });
        }
        /* TextureAtlas */
        FileHandle defaultFile = xmlFile.sibling(xmlFile.nameWithoutExtension() + ".atlas");
        if(root.hasChild("atlas")) {
            Element atlas = root.getChildByName("atlas");
            FileHandle sibling = xmlFile.sibling(atlas.getAttribute("location"));

            // load atlas
            if(sibling.exists()) {
                // prevent from duplicated loading
                if(list.contains(sibling, false)) {
                    throw new SerializationException(xmlFile + ": Resource has already loaded: " + sibling);
                } else {
                    addRegions(new TextureAtlas(sibling));
                    list.add(sibling);
                }
            }
            else throw new SerializationException("Atlas file not found: " + sibling);

        } else if(defaultFile.exists()) {
            addRegions(new TextureAtlas(defaultFile));
        } else {
            throw new SerializationException("Invalid skin file: No .atlas file found.");
        }
        /* Freetype Font */
        if(root.hasChild("font")) {
            Element font = root.getChildByName("font");
            FileHandle sibling = xmlFile.sibling(font.getAttribute("location"));
            // load font file
            if(sibling.exists()) {
                // prevent from duplicated loading
                if(list.contains(sibling, false)) {
                    throw new SerializationException(xmlFile + ": Resource has already loaded: " + sibling);
                } else {
                    loadFontXml(sibling, reader, list);
                    list.add(sibling);
                }
            }
            else Gdx.app.error(getClass().getName(), xmlFile + ": File not found. Ignored.");
        }
        /* Parse <Class> element */
        if(root.hasChild("class")) {
            Array<Element> classes = root.getChildrenByNameRecursively("class");
            classes.forEach(element -> {
                builder.startStyle();

                for(int i=0; i < element.getChildCount(); i++) {
                    builder.startAttribute();
                    Element child = element.getChild(i);

                    for(int j=0; j < child.getChildCount(); j++) {
                        Element grandchild = child.getChild(j);
                        builder.setAttribute(grandchild.getName(), grandchild.getText());
                    }

                    builder.endAttribute(child.getName());
                }

                builder.endStyle(element.getAttribute("name"));
            });
        }
        return builder;
    }
}