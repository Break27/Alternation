/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author break27
 */
public class FileUtils {
    
    private static FileInputStream fis;
    private static BufferedReader br;
    private static ByteArrayOutputStream bos;
    
    public static byte[] getbyteStream(File file) {
        byte[] data = new byte[0];
        try {
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1024];
            int len = -1;
            while ( (len = fis.read(b)) != -1 ) {
                bos.write(b, 0 , len);
            }
            data = bos.toByteArray();
            fis.close();
            bos.close();
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        // 返回字节数组
        return data;
    }
    
    public static List<String> fileRead(File file) {
        List<String> lines = null;
        try {
            lines = (List<String>) fileReadLine(file, -1);
        } catch (IOException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lines;
    }
    /**
     * @return String List or String
     */
    public static Object fileReadLine(File file, int linenum) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        List<String> lines = new ArrayList<>();
        String line = null;
        br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
        while ((line = br.readLine()) != null) {
              lines.add(line);
        }
        br.close();
        if ( linenum < 0 ) return lines;
        if ( linenum > lines.size() ) return null;
        return lines.get(linenum);
    }
    
    public static Font loadFont(String filename, int fontSize) {
        File fontFile = new File(filename);
        if ( !fontFile.exists() ) throw new UnsupportedOperationException("Couldn't load font \"" + filename + "\". (File Not Found)");
        Font dynamicFont = null;
        try {
            fis = new FileInputStream(fontFile);
            dynamicFont = Font.createFont(Font.TRUETYPE_FONT, fis).deriveFont(fontSize);
        } catch (IOException | FontFormatException ex) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dynamicFont;
    }
}
