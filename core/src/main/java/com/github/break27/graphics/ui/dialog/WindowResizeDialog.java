/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.ui.dialog;

/**
 *
 * @author break27
 */
public class WindowResizeDialog extends AlternativeDialog {
    
    public WindowResizeDialog(String name) {
        super(name);
        addCloseButton();
        text("This is a test.");
    }

    @Override
    public void styleApply() {
        setTitleImage(getAlterSkin().getDrawable("icon20-application"));
    }
    
    @Override
    public void localeApply() {
    }
}
