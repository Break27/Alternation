/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.desktop;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.github.break27.lwjgl.LwjglAdapter;
import java.nio.ByteBuffer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.eclipse.fx.drift.DriftFXSurface;
import org.eclipse.fx.drift.GLRenderer;
import org.eclipse.fx.drift.PresentationMode;
import org.eclipse.fx.drift.RenderTarget;
import org.eclipse.fx.drift.Renderer;
import org.eclipse.fx.drift.StandardTransferTypes;
import org.eclipse.fx.drift.Swapchain;
import org.eclipse.fx.drift.SwapchainConfig;
import org.eclipse.fx.drift.TransferType;
import org.eclipse.fx.drift.Vec2i;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_POINTS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glVertex2i;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;
import static org.lwjgl.opengl.GL30.GL_DEPTH_COMPONENT32F;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glCheckFramebufferStatus;
import static org.lwjgl.opengl.GL30.glDeleteFramebuffers;
import static org.lwjgl.opengl.GL30.glGenFramebuffers;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

/**
 *
 * @author break27
 */
public class fxApp extends Application {
    
    private TransferType txType = StandardTransferTypes.MainMemory;
    private LwjglAdapter adapter;
    private Renderer renderer;
    private Swapchain swapchain;
    
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane root = new BorderPane();
        root.setBackground(null);
        root.setPadding(new Insets(40));
        root.setPrefSize(width, height);
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        DriftFXSurface surface = new DriftFXSurface();

        renderer = GLRenderer.getRenderer(surface);
        adapter = new LwjglAdapter(width, height);

        root.setCenter(surface);
        
        ComboBox<TransferType> txMode = new ComboBox<>();
        txMode.getItems().addAll(StandardTransferTypes.MainMemory, StandardTransferTypes.NVDXInterop, StandardTransferTypes.IOSurface);
        txMode.setValue(txType);
        txMode.valueProperty().addListener((obs, ov, nv) -> txType = nv);
        
        stage.setScene(scene);
        stage.setTitle("JFX Application");
        stage.show();
    }
    
    int width = 1024;
    int height = 768;
    private TransferType curTxType;
    private RenderTarget target;
    public Thread renderThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while ( !DesktopLauncher.isAppOnClose() ) {
                System.out.println("thread is running.");
                if (renderer == null) continue;
                Vec2i size = renderer.getSize();
                if (swapchain == null || size.x != width || size.y != height || curTxType != txType) {
                    System.err.println("(re)create swapchain");
                    if (swapchain != null) {
                        swapchain.dispose();
                    }

                    swapchain = renderer.createSwapchain(new SwapchainConfig(size, 2, PresentationMode.MAILBOX, txType));
                    width = size.x;
                    height = size.y;
                    curTxType = txType;
                }
                
                try {
	            target = swapchain.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
	    	/*
	            int tex = GLRenderer.getGLTextureId(target);
	    		int depthTex = glGenTextures();
	    		glBindTexture(GL_TEXTURE_2D, depthTex);
	    		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32F, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer)null);
	            glBindTexture(GL_TEXTURE_2D, 0);
	            
	    		int fb = glGenFramebuffers();
	    		
	    		
	    		glBindFramebuffer(GL_FRAMEBUFFER, fb);
	    		glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, tex, 0);
	    		glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, depthTex, 0);
	    		
	    		int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
	    		switch (status) {
	    			case GL_FRAMEBUFFER_COMPLETE: break;
	    			case GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT: System.err.println("INCOMPLETE_ATTACHMENT!"); break;
	    		}
	    		
	    		glViewport(0, 0, width, height);
	    		
	            
	    		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	    		glDeleteFramebuffers(fb);
	    		glDeleteTextures(depthTex);
                        */
	            adapter.start();
	            swapchain.present(target);
	            //Thread.sleep(16);
        }
        
        swapchain.dispose();
		swapchain = null;
            }
    });
    public void loopRun() {
        renderThread.start();
    }
    
    private void render() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glBegin(GL_POINTS);
        glColor3f(1,1,1);
        glVertex2i(100,100);
        glEnd();
    }
    
    private Texture texture;
    private Pixmap pixmap;
    private Batch batch;
    private void createImage() {
        if(pixmap !=null && texture != null) {
            pixmap.dispose();
            texture.dispose();

            pixmap = new Pixmap(pixelData, 0, pixelData.length);
            texture = new Texture(pixmap);
            texture.bind();
            batch.begin();
            batch.draw(texture, 100, 100);
            batch.end();
        }
    }
    
    private static byte[] pixelData;
    public static void passFrameData(byte[] data) {
        pixelData = data;
    }
}
