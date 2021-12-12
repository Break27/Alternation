/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui.button;

/**
 *
 * @author break27
 */
public class CloseButton extends TitleButton {
    
    public CloseButton() {
        provider.setStyle("icon_close", "alter::icon-close");
    }
    
    @Override
    public void styleApply() {
        setImage(provider.getStyle("icon_close"));
    }
}
