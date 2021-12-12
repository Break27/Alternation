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
public class CollapseButton extends TitleButton {
    
    /** Compatible with VisImageButton.
     */
    public CollapseButton() {
        provider.setStyle("icon_collapse", "alter::icon-collapse");
    }

    @Override
    public void styleApply() {
        setImage(provider.getStyle("icon_collapse"));
    }
}