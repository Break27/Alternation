/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.util;

import com.badlogic.gdx.files.FileHandle;
import java.io.File;

/**
 *
 * @author break27
 */
public class FileUtils {
    public static File getTempFile(Class<?> c) {
        return FileHandle.tempFile(c.getName()).file();
    }
}
