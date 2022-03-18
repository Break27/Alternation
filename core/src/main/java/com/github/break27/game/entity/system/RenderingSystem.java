package com.github.break27.game.entity.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.github.break27.game.entity.component.ModelComponent;
import com.github.break27.game.entity.component.PositionComponent;
import com.github.break27.game.entity.component.VisibleComponent;

import java.util.Comparator;

public class RenderingSystem extends SortedIteratingSystem {
    private float visibleRadius = 10.0f;
    private final ComponentMapper<VisibleComponent> vm;

    public RenderingSystem() {
        super(Family.all(VisibleComponent.class, ModelComponent.class, PositionComponent.class).get(), new VisibilityComparator());
        vm = ComponentMapper.getFor(VisibleComponent.class);
    }

    public void setVisibleRadius(float radius) {
        visibleRadius = radius;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

    private static class VisibilityComparator implements Comparator<Entity> {
        private final ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);

        @Override
        public int compare(Entity e1, Entity e2) {

            return 0;
        }
    }
}
