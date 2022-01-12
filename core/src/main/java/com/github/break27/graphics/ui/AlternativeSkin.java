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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.XmlReader.Element;

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

    public @Null BitmapFont getDefaultFont() {
        try {
            return getFont("default");
        } catch (GdxRuntimeException ignored) { }
        return null;
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
            super.getJsonLoader(xmlFile).fromJson(Skin.class, jsonString);
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
    protected Json getJsonLoader(FileHandle skinFile) {
        AlternativeSkin skin = this;
        Json json = super.getJsonLoader(skinFile);
        // use an alternative serializer
        json.setSerializer(BitmapFont.class, new Json.ReadOnlySerializer<BitmapFont>() {
            public BitmapFont read (Json json, JsonValue jsonData, Class type) {
                String path = json.readValue("file", String.class, jsonData);
                int scaledSize = json.readValue("scaledSize", int.class, -1, jsonData);
                Boolean flip = json.readValue("flip", Boolean.class, false, jsonData);
                Boolean markupEnabled = json.readValue("markupEnabled", Boolean.class, false, jsonData);

                FileHandle fontFile = skinFile.parent().child(path);
                if (!fontFile.exists()) fontFile = Gdx.files.internal(path);
                if (!fontFile.exists()) throw new SerializationException("Font file not found: " + fontFile);

                // Use a region with the same name as the font, else use a PNG file in the same directory as the FNT file.
                String regionName = fontFile.nameWithoutExtension();
                try {
                    BitmapFont font = null;
                    // replace "default" with a TrueType font loaded beforehand
                    // if it is available.
                    if(regionName.equals("default")) font = skin.getDefaultFont();
                    if(font == null) {
                        Array<TextureRegion> regions = skin.getRegions(regionName);
                        if (regions != null)
                            font = new BitmapFont(new BitmapFont.BitmapFontData(fontFile, flip), regions, true);
                        else {
                            TextureRegion region = skin.optional(regionName, TextureRegion.class);
                            if (region != null)
                                font = new BitmapFont(fontFile, region, flip);
                            else {
                                FileHandle imageFile = fontFile.parent().child(regionName + ".png");
                                if (imageFile.exists())
                                    font = new BitmapFont(fontFile, imageFile, flip);
                                else
                                    font = new BitmapFont(fontFile, flip);
                            }
                        }
                        font.getData().markupEnabled = markupEnabled;
                        // Scaled size is the desired cap height to scale the font to.
                        if (scaledSize != -1) font.getData().setScale(scaledSize / font.getCapHeight());
                    }
                    return font;
                } catch (RuntimeException ex) {
                    throw new SerializationException("Error loading bitmap font: " + fontFile, ex);
                }
            }
        });
        return json;
    }

    @Override
    public void dispose() {
        super.dispose();
        // dispose all atlases
        for(int i=0; i<AtlasSet.size; i++) {
            AtlasSet.pop().dispose();
        }
    }

    private void loadFontXml(FileHandle xmlFile, XmlReader reader, final Array<String> list) {
        /* Load XML */
        Element parent = reader.parse(xmlFile);
        parent.getChildrenByNameRecursively("resource").forEach(font -> {
            // get attributes
            String name = font.getAttribute("name");
            String location = font.getAttribute("location");
            FileHandle sibling = xmlFile.sibling(location);

            // prevent from duplicated loading
            if(list.contains(sibling.file().getAbsolutePath(), false))
                throw new SerializationException(xmlFile + ": Resource has already loaded: " + sibling);
            else
                list.add(sibling.file().getAbsolutePath());

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
        SkinBuilder builder = new SkinBuilder();
        parseXml(xmlFile, reader, builder, new Array<>());
        return json.toJson(builder);
    }
    
    private void parseXml(FileHandle xmlFile, XmlReader reader, final SkinBuilder builder,
                                 final Array<String> list) {
        Element root = reader.parse(xmlFile);
        /* TextureAtlas */
        FileHandle defaultFile = xmlFile.sibling(xmlFile.nameWithoutExtension() + ".atlas");
        if(root.hasChild("atlas")) {
            Element atlas = root.getChildByName("atlas");
            FileHandle sibling = xmlFile.sibling(atlas.getAttribute("location"));

            // load atlas
            if(sibling.exists()) {
                // prevent from duplicated loading
                if(list.contains(sibling.file().getAbsolutePath(), false)) {
                    throw new SerializationException(xmlFile + ": Resource has already loaded: " + sibling);
                } else {
                    addRegions(new TextureAtlas(sibling));
                    list.add(sibling.file().getAbsolutePath());
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
                if(list.contains(sibling.file().getAbsolutePath(), false)) {
                    throw new SerializationException(xmlFile + ": Resource has already loaded: " + sibling);
                } else {
                    loadFontXml(sibling, reader, list);
                    list.add(sibling.file().getAbsolutePath());
                }
            }
            else Gdx.app.error(getClass().getName(), xmlFile + ": File not found. Ignored.");
        }
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
                        Gdx.app.error(getClass().getName(), xmlFile + ": Unsupported Operation: " +
                                "File is including itself! Ignored.");
                    else if(list.contains(file.file().getAbsolutePath(), false))
                        throw new SerializationException(file + ": Target file has already loaded: " + xmlFile);
                    else {
                        list.add(file.file().getAbsolutePath());
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
    }
}

class SkinBuilder implements Json.Serializable {
    private OrderedMap<String, SkinStyle> Classes;
    private SkinStyle Styles;
    private SkinAttribute Attributes;

    public SkinBuilder() {
        Classes = new OrderedMap<>();
    }

    public void startStyle() {
        Styles = new SkinStyle();
    }

    public void endStyle(String name) {
        Classes.put(name, Styles);
    }

    public void startAttribute() {
        Attributes = new SkinAttribute();
    }

    public void setAttribute(String name, Object value) {
        Attributes.put(name, value);
    }

    public void endAttribute(String name) {
        Styles.put(name, Attributes);
    }

    @Override
    public void write(Json json) {
        Classes.forEach(entry -> {
            json.writeValue(entry.key, entry.value);
        });
    }

    @Override
    public void read(Json json, JsonValue jv) {
    }

    class SkinStyle implements Json.Serializable {
        private OrderedMap<String, SkinAttribute> Styles = new OrderedMap<>();

        public void put(String name, SkinAttribute attri) {
            Styles.put(name, attri);
        }

        @Override
        public void write(Json json) {
            Styles.forEach(entry -> {
                json.writeValue(entry.key, entry.value);
            });
        }

        @Override
        public void read(Json json, JsonValue jv) {
        }
    }

    class SkinAttribute implements Json.Serializable {
        private OrderedMap<String, Object> Attributes = new OrderedMap<>();

        public void put(String name, Object value) {
            Attributes.put(name, value);
        }

        @Override
        public void write(Json json) {
            Attributes.forEach(entry -> {
                json.writeValue(entry.key, entry.value);
            });
        }

        @Override
        public void read(Json json, JsonValue jv) {
        }
    }
}