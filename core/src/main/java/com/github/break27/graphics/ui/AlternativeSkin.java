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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.SerializationException;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.github.break27.system.Resource.SerializableResource;
import com.github.break27.system.misc.SkinBuilder;
import com.kotcrab.vis.ui.VisUI;

/**
 *
 * @author break27
 */
public class AlternativeSkin extends Skin implements SerializableResource {
    
    Array<TextureAtlas> AtlasSet = new Array<>();
    
    public AlternativeSkin() {
    }
    
    public AlternativeSkin(FileHandle file) {
        load(file);
    }
    
    @Override
    public <T> T get(String name, Class<T> type) {
        try {
            return super.get(name, type);
        } catch(GdxRuntimeException ignored) {
        }
        // turn to VisUI skin if no resource available
        Gdx.app.debug(getClass().getName(), "No Resource of " + name+"@"+type.getName() 
                + " Available, turning to VisUI.");
        return VisUI.getSkin().get(name, type);
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
    
    private String jsonBuild(FileHandle xmlFile) {
        XmlReader reader = new XmlReader();
        Json json = new Json();
        // build json string
        SkinBuilder builder = parseXml(xmlFile, reader, new SkinBuilder(), new Array<>());
        return json.toJson(builder);
    }
    
    private SkinBuilder parseXml(FileHandle xmlFile, XmlReader reader, final SkinBuilder builder, final Array<FileHandle> list) {
        Element root = reader.parse(xmlFile);
        /* Includes */
        if(root.hasChild("include")) {
            Array<Element> includes = root.getChildrenByNameRecursively("include");
            includes.forEach(element -> {
                FileHandle file = null;
                if(element.hasAttribute("internal")) {
                    file = Gdx.files.internal(element.getAttribute("internal"));
                } else if(element.hasAttribute("external")) {
                    file = Gdx.files.external(element.getAttribute("external"));
                }
                
                if(file != null && file.exists()) {
                    // preventing from infinite recursive loop
                    if(file.equals(xmlFile)) 
                        Gdx.app.error(getClass().getName(), "Unsupported Operation: File is including itself! Ignored.");
                    else if(list.contains(file, false)) 
                        throw new SerializationException(file + ": Target file has already loaded: " + xmlFile);
                    else {
                        list.add(file);
                        parseXml(file, reader, builder, list);
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
            
            if(sibling.exists()) addRegions(new TextureAtlas(sibling));
            else throw new SerializationException("Atlas file not found: " + sibling);
            
        } else if(defaultFile.exists()) {
            addRegions(new TextureAtlas(defaultFile));
        } else {
            throw new SerializationException("Invalid skin file: No .atlas file found.");
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