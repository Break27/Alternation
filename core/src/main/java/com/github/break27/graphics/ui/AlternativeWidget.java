/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui;

import com.github.break27.system.Resource;
import java.util.HashMap;

/**
 *
 * @author break27
 */
public interface AlternativeWidget {
    static final HashMap<Class, AlternativeWidget> WidgetsMap = new HashMap();
    
    /**
     * 
     */
    public default void enableStyle() {
        WidgetsMap.put(this.getClass(), this);
        /* Apply to itself immediately */
        styleApply();
        localeApply();
    }
    
    /**
     * @return AlternativeSkin
     */
    public default AlternativeSkin getAlterSkin() {
        return Resource.getSkin("alter");
    }
    
    public void styleApply();
    
    public void localeApply();
}