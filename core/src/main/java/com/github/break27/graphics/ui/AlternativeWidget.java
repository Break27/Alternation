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
public interface AlternativeWidget{
    
    public void styleApply();
    
    public default StyleProvider createStyleProvider() {
        return new StyleProvider(this);
    }
}