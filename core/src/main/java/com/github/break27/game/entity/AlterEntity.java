package com.github.break27.game.entity;

import com.badlogic.ashley.core.Entity;
import com.github.break27.game.entity.component.ModelComponent;
import com.github.break27.game.entity.component.PositionComponent;
import com.github.break27.game.entity.component.VelocityComponent;

public class AlterEntity extends Entity {

    public AlterEntity() {
        // initialize with default components
        add(new PositionComponent());
        add(new VelocityComponent());
    }

    public void update() {

    }
}
