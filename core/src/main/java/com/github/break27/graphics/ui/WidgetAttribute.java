/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui;

/**
 *
 * @author break27
 */
public interface WidgetAttribute {
    
    public static class StyleAttribute implements WidgetAttribute {
        
        public StyleAttribute() {
            
        }

        @Override
        public void set() {
            
        }
    }
    
    public static class LocaleAttribute implements WidgetAttribute {
        
        public LocaleAttribute() {
            
        }

        @Override
        public void set() {
            
        }
    }
    
    public void set();
}
