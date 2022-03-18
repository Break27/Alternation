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

package com.github.break27.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.github.break27.graphics.ui.AlternativeSkin;
import com.github.break27.graphics.ui.LocalizableWidget;
import com.github.break27.graphics.ui.StyleAppliedWidget;
import com.github.break27.graphics.ui.widget.AlterLabel;
import com.github.break27.util.Utils;
import cz.vutbr.web.css.MediaSpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import javax.imageio.ImageIO;
import org.fit.cssbox.awt.GraphicsEngine;
import org.fit.cssbox.css.CSSNorm;
import org.fit.cssbox.css.DOMAnalyzer;
import org.fit.cssbox.io.DOMSource;
import org.fit.cssbox.io.DefaultDOMSource;
import org.fit.cssbox.io.DefaultDocumentSource;
import org.fit.cssbox.io.DocumentSource;
import org.fit.cssbox.layout.Dimension;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author break27
 */
public class Browser {
    private final BrowserTable table;
    private final AlterLabel titleLabel;

    protected BrowserModule Module;
    protected BrowserModule.BrowserParser Parser;
    protected BrowserModule.BrowserRenderer Renderer;
    
    public Browser(int imageWidth, int imageHeight) {
        this(imageWidth, imageHeight, false);
    }
    
    public Browser(int imageWidth, int imageHeight, boolean isClipped) {
        // use screen resolution
        this(imageWidth, imageHeight, Gdx.graphics.getDisplayMode().width, 
                Gdx.graphics.getDisplayMode().height, isClipped);
    }
    
    public Browser(int imageWidth, int imageHeight, int displayWidth, int displayHeight) {
        this(imageWidth, imageHeight, displayWidth, displayHeight, false);
    }
    
    /** Create a Browser with Image size, Display size
     * (or screen resolution) specified.
     * @param width Width of the returned Image.
     * @param height Height of the returned Image.
     * @param displayWidth Width of the Canvas.
     * @param displayHeight Height of the Canvas.
     * @param isClipped If true, the returned Image would be
     * clipped to the expected size.
     */
    public Browser(int width, int height, int displayWidth, int displayHeight, boolean isClipped) {
        Dimension windowSize = new Dimension(displayWidth, displayHeight);
        table = new BrowserTable();
        titleLabel = new AlterLabel();
        titleLabel.setEllipsis(true);
        
        table.warning("NO-CONTENT");
        Module = new BrowserModule();

        Parser = Module.createParser(windowSize);
        Renderer = Module.createRenderer(width, height, isClipped);
    }
    
    public Image getImage() {
        return Renderer.getImage();
    }
    
    public Table getBrowserTable() {
        return table;
    }
    
    public Label getBrowserLabel() {
        return titleLabel;
    }
    
    @Deprecated
    public void render() {
        Renderer.render();
    }

    public void reload() {

    }

    public void setTitleSeparation(String separation) {
        Parser.separation = separation;
    }

    public void destroy() {
        Parser.halt();
        Renderer.halt();
    }
    
    /** To load an html document from URL.
     * Note: the protocol should be announced,
     * otherwise it is considered as following HTTP Protocol.
     * @param url
     */
    public void load(String url) {
        if (!url.startsWith("http:") 
            && !url.startsWith("https:") 
            && !url.startsWith("ftp:") 
            && !url.startsWith("file:"))
            url = "http://" + url;
        Parser.parse(url);
    }
    
    /** To load an html document from URI
     * @param uri
     */
    public void load(URI uri) {
        if(!Module.isLoading) {
            Parser.parse(uri);
        } else {
            Gdx.app.error(getClass().getName(), "Unsupported Operation: "
                    + "Browser could only load once at a time!");
        }
    }

    /** Basic Modules of the Browser.
     * @author break27
     */
    protected class BrowserModule {
        protected volatile ByteArrayOutputStream out;
        protected volatile boolean isLoading = false;

        private Thread ParserThread;
        private Thread RendererThread;

        public BrowserParser createParser(Dimension size) {
            return new BrowserParser(size);
        }

        public BrowserRenderer createRenderer(int width, int height, boolean isClipped) {
            return new BrowserRenderer(width, height, isClipped);
        }

        public class BrowserParser {
            String separation = "";
            Dimension windowSize;
            DocumentSource source;

            BrowserParser(Dimension size) {
                out = new ByteArrayOutputStream();
                this.windowSize = size;
            }

            public void parse(String url) {
                try {
                    source = new DefaultDocumentSource(url);
                    parse(source);
                } catch(IOException e) {
                    Gdx.app.error(getClass().getName(), "Browser Error: Failed to load document: "
                            + source.getURL(), e);
                }
            }

            public void parse(URI uri) {
                try {
                    source = new DefaultDocumentSource(uri.toURL());
                    parse(source);
                } catch(IOException e) {
                    Gdx.app.error(getClass().getName(), "Browser Error: Failed to load document: "
                            + source.getURL(), e);
                }
            }

            private void parse(DocumentSource source) {
                ParserThread = new Thread("Browser$BrowserParser") {
                    @Override
                    public void run() {
                        try {
                            parseHtml(source);
                        } catch(IOException | SAXException e) {
                            Gdx.app.error(getClass().getName(), "Browser Error: Failed to load document: "
                                    + source.getURL(), e);
                        }
                    }
                };
                ParserThread.start();
            }

            public void halt() {
                if(ParserThread.isAlive()) {
                    ParserThread.interrupt();
                }
            }

            /** Get the HTML Document from the given source
             *  then parse and render it into an image.
             */
            private void parseHtml(DocumentSource source) throws IOException, SAXException {
                titleLabel.setText("");
                table.info("CONNECT", source.getURL());
                try {
                    isLoading = true;
                    DOMSource parser = new DefaultDOMSource(source);
                    // utf-8 support
                    parser.setContentType("text/html;charset=utf-8");
                    Document doc = parser.parse();
                    table.info("LOADING", source.getURL());
                    // create the media specification
                    MediaSpec media = new MediaSpec("screen");
                    media.setDimensions(windowSize.width, windowSize.height);
                    media.setDeviceDimensions(windowSize.width, windowSize.height);

                    // abort loading if thread interrupted
                    if(Thread.currentThread().isInterrupted())
                        throw new InterruptedException();

                    // Create the CSS analyzer
                    DOMAnalyzer da = new DOMAnalyzer(doc, source.getURL());
                    da.setDefaultEncoding("UTF-8");
                    // get the title of the webpage
                    // if not available, use URL as title.
                    titleLabel.setText(separation + source.getURL().toString());
                    NodeList titleTag = da.getHead().getElementsByTagName("title");
                    if(titleTag.getLength() > 0) {
                        String title = titleTag.item(0).getTextContent();
                        if(!title.isEmpty()) titleLabel.setText(separation + Utils.stripNPC(title));
                    }

                    da.setMediaSpec(media);
                    da.attributesToStyles(); //convert the HTML presentation attributes to inline styles
                    da.addStyleSheet(null, CSSNorm.stdStyleSheet(), DOMAnalyzer.Origin.AGENT); //use the standard style sheet
                    da.addStyleSheet(null, CSSNorm.userStyleSheet(), DOMAnalyzer.Origin.AGENT); //use the additional style sheet
                    da.addStyleSheet(null, CSSNorm.formsStyleSheet(), DOMAnalyzer.Origin.AGENT); //render form fields using css
                    da.getStyleSheets(); //load the author style sheets

                    if(Thread.currentThread().isInterrupted())
                        throw new InterruptedException();

                    GraphicsEngine contentCanvas = new GraphicsEngine(da.getRoot(), da, source.getURL());
                    contentCanvas.setAutoMediaUpdate(false); //we have a correct media specification, do not update
                    contentCanvas.setAutoSizeUpdate(true);
                    contentCanvas.getConfig().setClipViewport(true);
                    contentCanvas.getConfig().setLoadImages(true);
                    contentCanvas.getConfig().setLoadBackgroundImages(true);

                    if(Thread.currentThread().isInterrupted())
                        throw new InterruptedException();

                    // write to image
                    table.loading("RENDER");
                    contentCanvas.createLayout(windowSize);
                    ImageIO.write(contentCanvas.getImage(), "png", out);

                } catch(IOException | DOMException | SAXException e) {
                    table.error("ERROR", e.getMessage());
                    throw e;
                } catch(InterruptedException ex) {
                    Gdx.app.log(Thread.currentThread().getName(), "Browser Loading Aborted.");
                } finally {
                    source.close();
                }
                table.success("READY");
                isLoading = false;
                /*
                 * post Rendering thread to render thread
                 * this is especially important when it
                 * comes to rendering-related functions.
                 */
                if(!Thread.currentThread().isInterrupted())
                    Gdx.app.postRunnable(RendererThread);
            }
        }

        public class BrowserRenderer {
            volatile TextureRegion region;
            volatile Image image;

            boolean isClipped;
            int Width;
            int Height;

            BrowserRenderer(int width, int height, boolean isClipped) {
                region = new TextureRegion();
                image = new Image();

                this.Width = width;
                this.Height = height;
                this.isClipped = isClipped;
                image.setSize(width, height);

                RendererThread = new Thread("Browser$BrowserRenderer") {
                    @Override
                    public void run() {
                        if(!currentThread().isInterrupted())
                            render();
                    }
                };
            }

            public void halt() {
                if(RendererThread.isAlive())
                    RendererThread.interrupt();
            }

            public Image getImage() {
                // if no image available, create a new blank image.
                if(image.getDrawable() == null) {
                    image.setDrawable(new TextureRegionDrawable(new Texture(new Pixmap(Width, Height, Pixmap.Format.RGBA8888))));
                }
                return image;
            }

            public void render() {
                region.setRegion(new Texture(new Pixmap(out.toByteArray(), 0, out.size())));
                if(isClipped) region.setRegion(0, 0, Width, Height);
                image.setDrawable(new TextureRegionDrawable(region));
            }
        }
    }
}

class BrowserTable extends Table implements StyleAppliedWidget, LocalizableWidget {
    private final Image badge;
    private final AlterLabel label;
    private Drawable info, loading, warning, error, success;
    
    public BrowserTable() {
        this("");
    }
    
    public BrowserTable(String text) {
        badge = new Image();
        label = new AlterLabel(text);
        label.setWrap(true);
        add(badge).center();
        add(label).spaceLeft(5f);
        
        register();
    }

    public void loading(String code, Object... args) {
        badge.setDrawable(loading);
        label.setText(translate("label", code, args));
    }

    public void warning(String code, Object... args) {
        badge.setDrawable(warning);
        label.setText(translate("label", code, args));
    }

    public void error(String code, Object... args) {
        badge.setDrawable(error);
        label.setText(translate("label", code, args));
    }

    public void success(String code, Object... args) {
        badge.setDrawable(success);
        label.setText(translate("label", code, args));
    }

    public void info(String code, Object... args) {
        badge.setDrawable(info);
        label.setText(translate("label", code, args));
    }
    
    @Override
    public void styleApply(AlternativeSkin skin) {
        // image styles
        info = skin.getDrawable("icon-info-circle");
        loading = skin.getDrawable("icon-monitor-go");
        warning = skin.getDrawable("icon-warning");
        error = skin.getDrawable("icon-world-delete");
        success = skin.getDrawable("icon-tick");
    }

    @Override
    public void localeApply() {
    }
    
    @Override
    public void destroy() {
    }
}