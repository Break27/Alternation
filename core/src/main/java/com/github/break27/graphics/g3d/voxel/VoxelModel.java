/**************************************************************************
 * Copyright (c) 2021 Breakerbear
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *************************************************************************/

package com.github.break27.graphics.g3d.voxel;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import net.dermetfan.utils.ArrayUtils;
import sun.security.util.ArrayUtil;

/**
 *
 * @author break27
 */
public class VoxelModel extends ModelInstance {
    VoxelWorld world;

    float posX, posY, posZ;
    //int[] visibleChunkIndices = null;
    //boolean dataUpdated = true;

    public VoxelModel(VoxelWorld world) {
        super(new Model());
        this.world = world;
    }

    public void updatePosition(float x, float y, float z) {
        posX = x;
        posY = y;
        posZ = z;
        //dataUpdated = false;
    }

    /* Experimental
    @Override
    public void getRenderables (Array<Renderable> renderables, Pool<Renderable> pool) {
        if(visibleChunkIndices == null || !dataUpdated) {
            visibleChunkIndices = world.getVisibleChunkIndices(posX, posY, posX);
            dataUpdated = true;
        }
        for(int i : visibleChunkIndices) {
            VoxelChunk chunk = world.chunks.get(i);
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
     */
    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        for (int i = 0; i < world.chunks.size; i++) {
            VoxelChunk chunk = world.chunks.get(i);
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