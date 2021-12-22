/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.github.break27.graphics.ui.AlternativeWidget;
import com.github.break27.graphics.ui.widget.AlterLabel;
import cz.vutbr.web.css.MediaSpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
 *
 * @author break27
 */
public class Browser {
    
    static BrowserTable table;
    static AlterLabel titleLabel;
    
    ByteArrayOutputStream out;
    Dimension windowSize;
    
    BrowserModule.BrowserParser Parser;
    BrowserModule.BrowserRenderer Renderer;
    
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
     *  (or screen resolution) specified.
     *  @param width Width of the returned Image.
     *  @param height Height of the returned Image.
     *  @param displayWidth Width of the Canvas.
     *  @param displayHeight Height of the Canvas.
     *  @param isClipped If true, the returned Image would be
     *  clipped to the expected size.
     */
    public Browser(int width, int height, int displayWidth, int displayHeight, boolean isClipped) {
        table = new BrowserTable();
        titleLabel = new AlterLabel();
        titleLabel.setEllipsis(true);
        titleLabel.setWidth(100f);
        
        table.warning("No content loaded.");
        windowSize = new Dimension(displayWidth, displayHeight);
        Parser = new BrowserModule.BrowserParser(windowSize);
        Renderer = new BrowserModule.BrowserRenderer(width, height, isClipped);
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
    
    public void destroy() {
        Parser.halt();
        Renderer.halt();
    }
    
    /** To load a html document from URL.
     *  Note: the protocol should be announced.
     *  Otherwise it is considered as following HTTP Protocol.
     *  @param url
     */
    public void load(String url) {
        if (!url.startsWith("http:") 
            && !url.startsWith("https:") 
            && !url.startsWith("ftp:") 
            && !url.startsWith("file:"))
            url = "http://" + url;
        try {
            load(new URI(url));
        } catch (URISyntaxException ex) {
            Gdx.app.error(getClass().getName(), "Error loading: " + url);
        }
    }
    
    /** To load a html document from URI
     *  @param uri
     */
    public void load(URI uri) {
        if(!BrowserModule.isLoading) {
            Parser.parse(uri);
        } else {
            Gdx.app.error(getClass().getName(), "Unsupported Operation: "
                    + "Browser could only load once at a time!");
        }
    }
    
    /** Basic Modules of the Browser.
     *  @author break27
     */
    protected static class BrowserModule {
    
        static volatile ByteArrayOutputStream out;
        static volatile boolean isLoading = false;

        static Thread ParserThread;
        static Thread RendererThread;
        
        public static class BrowserParser {

            Dimension windowSize;
            DocumentSource source;

            BrowserParser(Dimension size) {
                out = new ByteArrayOutputStream();
                this.windowSize = size;
            }

            public void parse(String url) {
                ParserThread = new Thread("Browser$BrowserParser") {
                    @Override
                    public void run() {
                        try {
                            source = new DefaultDocumentSource(url);
                            parseHtml(source);
                        } catch(IOException | SAXException e) {
                            Gdx.app.error(getClass().getName(), "Browser Error: Failed to load document: " 
                                    + source.getURL(), e);
                        }
                    }
                };
                ParserThread.start();
            }

            public void parse(URI uri) {
                ParserThread = new Thread("Browser$BrowserParser") {
                    @Override
                    public void run() {
                        try {
                            source = new DefaultDocumentSource(uri.toURL());
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
                    ParserThread.stop();
                }
            }
            
            /** Get the HTML Document from the given source
             *  then parse and render it into an image.
             */
            private void parseHtml(DocumentSource source) throws IOException, SAXException {
                titleLabel.setText("");
                table.info("Connecting to " + source.getURL() + "...");
                try {
                    isLoading = true;
                    DOMSource parser = new DefaultDOMSource(source);
                    // utf-8 support
                    parser.setContentType("text/html;charset=utf-8");
                    Document doc = parser.parse();
                    table.info("Loading " + source.getURL() + "...");
                    // create the media specification
                    MediaSpec media = new MediaSpec("screen");
                    media.setDimensions(windowSize.width, windowSize.height);
                    media.setDeviceDimensions(windowSize.width, windowSize.height);

                    // Create the CSS analyzer
                    DOMAnalyzer da = new DOMAnalyzer(doc, source.getURL());
                    da.setDefaultEncoding("UTF-8");
                    // get the title of the webpage
                    // if not available, use URL as title.
                    titleLabel.setText(" - " + source.getURL().toString());
                    NodeList titleTag = da.getHead().getElementsByTagName("title");
                    if(titleTag.getLength() > 0) {
                        String title = titleTag.item(0).getTextContent();
                        if(!title.isEmpty()) titleLabel.setText(" - " + stripNPC(title));
                    }
                    
                    da.setMediaSpec(media);
                    da.attributesToStyles(); //convert the HTML presentation attributes to inline styles
                    da.addStyleSheet(null, CSSNorm.stdStyleSheet(), DOMAnalyzer.Origin.AGENT); //use the standard style sheet
                    da.addStyleSheet(null, CSSNorm.userStyleSheet(), DOMAnalyzer.Origin.AGENT); //use the additional style sheet
                    da.addStyleSheet(null, CSSNorm.formsStyleSheet(), DOMAnalyzer.Origin.AGENT); //render form fields using css
                    da.getStyleSheets(); //load the author style sheets

                    GraphicsEngine contentCanvas = new GraphicsEngine(da.getRoot(), da, source.getURL());
                    contentCanvas.setAutoMediaUpdate(false); //we have a correct media specification, do not update
                    contentCanvas.setAutoSizeUpdate(true);
                    contentCanvas.getConfig().setClipViewport(true);
                    contentCanvas.getConfig().setLoadImages(true);
                    contentCanvas.getConfig().setLoadBackgroundImages(true);

                    table.loading("Rendering...");
                    contentCanvas.createLayout(windowSize);
                    ImageIO.write(contentCanvas.getImage(), "png", out);
                    
                } catch(IOException | DOMException | SAXException ex) {
                    table.error("Error: " + ex.getMessage());
                    throw ex;
                } finally {
                    source.close();
                }
                table.success("Browser Ready.");
                isLoading = false;
                /*
                * post Rendering thread to render thread
                * this is especially important when it
                * comes to rendering-related functions.
                */
                Gdx.app.postRunnable(RendererThread);
            }
            
            /** Strip all Non-Printing Characters of a String Object.
             *  Any character with an ASCII Code below 32 or of 127
             *  will be deleted.
             */
            private String stripNPC(String string) {
                char[] chars = string.toCharArray();
                if(chars.length > 0) {
                    for(int i=0; i<chars.length; i++) {
                        if(chars[i] < 0x20 || chars[i] == 0x7F) 
                            chars[i] = 0x0;
                    }
                }
                return new String(chars);
            }
        }

        public static class BrowserRenderer {

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
                        render();
                    }
                };
            }
            
            public void halt() {
                if(RendererThread.isAlive())
                    RendererThread.stop();
            }
            
            public Image getImage() {
                // if no image available, create a new blank image.
                if(image.getDrawable() == null) {
                    image.setDrawable(new TextureRegionDrawable(new Texture(new Pixmap(Width, Height, Format.RGBA8888))));
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

class BrowserTable extends Table implements AlternativeWidget {
    
    private Image badge;
    private AlterLabel label;
    private Drawable info, loading, warning, error, success;
    
    public BrowserTable() {
        this("");
    }
    
    public BrowserTable(String text) {
        badge = new Image();
        label = new AlterLabel(text);
        add(badge).center();
        add(label).spaceLeft(5f);
        
        label.setEllipsis(true);
        label.setWidth(125f);
        
        setStyleEnabled();
    }
    
    public void loading(String text) {
        badge.setDrawable(loading);
        label.setText(text);
    }
    
    public void warning(String text) {
        badge.setDrawable(warning);
        label.setText(text);
    }
    
    public void error(String text) {
        badge.setDrawable(error);
        label.setText(text);
    }
    
    public void success(String text) {
        badge.setDrawable(success);
        label.setText(text);
    }
    
    public void info(String text) {
        badge.setDrawable(info);
        label.setText(text);
    }
    
    @Override
    public void styleApply() {
        info = getAlterSkin().getDrawable("icon-info-circle");
        loading = getAlterSkin().getDrawable("icon-monitor-go");
        warning = getAlterSkin().getDrawable("icon-warning");
        error = getAlterSkin().getDrawable("icon-world-delete");
        success = getAlterSkin().getDrawable("icon-tick");
    }

    @Override
    public void localeApply() {
    }
    
    @Override
    public void destroy() {
    }
}