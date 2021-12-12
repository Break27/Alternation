/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.break27.graphics.g3d.voxel;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 *
 * @author break27
 */
public class VoxelModel extends ModelInstance {
    VoxelWorld world;
    
    public VoxelModel(VoxelWorld world) {
        super(new Model());
        this.world = world;
    }
    
    @Override
    public void getRenderables (Array<Renderable> renderables, Pool<Renderable> pool) {
        for (int i = 0; i < world.chunks.length; i++) {
            VoxelChunk chunk = world.chunks[i];
            Mesh mesh = world.meshes[i];
            if (world.dirty[i]) {
                int numVerts = chunk.calculateVertices(world.vertices);
                world.numVertices[i] = numVerts / 4 * 6;
                mesh.setVertices(world.vertices, 0, numVerts * VoxelChunk.VERTEX_SIZE);
                world.dirty[i] = false;
            }
            if (world.numVertices[i] == 0) continue;
            Renderable renderable = pool.obtain();
            renderable.material = world.materials[i];
            renderable.meshPart.mesh = mesh;
            renderable.meshPart.offset = 0;
            renderable.meshPart.size = world.numVertices[i];
            renderable.meshPart.primitiveType = GL20.GL_TRIANGLES;
            renderables.add(renderable);
        }
    }
}
