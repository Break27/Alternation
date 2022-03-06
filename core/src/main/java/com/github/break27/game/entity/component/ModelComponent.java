package com.github.break27.game.entity.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

public class ModelComponent implements Component {

    public ModelInstance Model;

    public ModelComponent() {
        this(new ModelInstance(new Model()));
    }

    public ModelComponent(ModelInstance model) {
        Model = model;
    }
}
