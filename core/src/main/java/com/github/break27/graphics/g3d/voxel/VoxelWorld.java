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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;

import java.util.Arrays;

/**
 *
 *
 */
public class VoxelWorld {
	public static final int CHUNK_SIZE_X = 8;
	public static final int CHUNK_SIZE_Y = 8;
	public static final int CHUNK_SIZE_Z = 8;

	public final Array<VoxelChunk> chunks;
	public final Mesh[] meshes;
	public final Material[] materials;
	public final boolean[] dirty;
	public final int[] numVertices;
	public float[] vertices;
        
	public final int chunksX;
	public final int chunksY;
	public final int chunksZ;

	public final int voxelsX;
	public final int voxelsY;
	public final int voxelsZ;

	int visibleChunkRadiusXZ;
	int visibleChunkRadiusY;
	int numChunks;

	public VoxelWorld (int chunksX, int chunksY, int chunksZ) {
		this(chunksX, chunksY, chunksZ, 5, 5);
	}

	public VoxelWorld (int chunksX, int chunksY, int chunksZ, int visibleChunkRadiusXZ, int visibleChunkRadiusY) {
		this.chunks = new Array<>();
		this.visibleChunkRadiusXZ = visibleChunkRadiusXZ;
		this.visibleChunkRadiusY = visibleChunkRadiusY;
		this.chunksX = chunksX;
		this.chunksY = chunksY;
		this.chunksZ = chunksZ;
		this.numChunks = chunksX * chunksY * chunksZ;
		this.voxelsX = chunksX * CHUNK_SIZE_X;
		this.voxelsY = chunksY * CHUNK_SIZE_Y;
		this.voxelsZ = chunksZ * CHUNK_SIZE_Z;
		for (int y = 0; y < chunksY; y++) {
			for (int z = 0; z < chunksZ; z++) {
				for (int x = 0; x < chunksX; x++) {
					VoxelChunk chunk = new VoxelChunk(CHUNK_SIZE_X, CHUNK_SIZE_Y, CHUNK_SIZE_Z);
					chunk.offset.set(x * CHUNK_SIZE_X, y * CHUNK_SIZE_Y, z * CHUNK_SIZE_Z);
					chunks.add(chunk);
				}
			}
		}
		int len = CHUNK_SIZE_X * CHUNK_SIZE_Y * CHUNK_SIZE_Z * 6 * 6 / 3;
		short[] indices = new short[len];
		short j = 0;
		for (int i = 0; i < len; i += 6, j += 4) {
			indices[i + 0] = (short)(j + 0);
			indices[i + 1] = (short)(j + 1);
			indices[i + 2] = (short)(j + 2);
			indices[i + 3] = (short)(j + 2);
			indices[i + 4] = (short)(j + 3);
			indices[i + 5] = (short)(j + 0);
		}
		this.meshes = new Mesh[chunksX * chunksY * chunksZ];
		for (int i = 0; i < meshes.length; i++) {
			meshes[i] = new Mesh(true, CHUNK_SIZE_X * CHUNK_SIZE_Y * CHUNK_SIZE_Z * 6 * 4,
				CHUNK_SIZE_X * CHUNK_SIZE_Y * CHUNK_SIZE_Z * 36 / 3, VertexAttribute.Position(), VertexAttribute.Normal());
			meshes[i].setIndices(indices);
		}
		this.dirty = new boolean[chunksX * chunksY * chunksZ];
		Arrays.fill(dirty, true);

		this.numVertices = new int[chunksX * chunksY * chunksZ];
		Arrays.fill(numVertices, 0);

		this.vertices = new float[VoxelChunk.VERTEX_SIZE * 6 * CHUNK_SIZE_X * CHUNK_SIZE_Y * CHUNK_SIZE_Z];
		this.materials = new Material[chunksX * chunksY * chunksZ];
		for (int i = 0; i < materials.length; i++) {
			// compatible with PBR Shader
			materials[i] = new Material(PBRColorAttribute.createBaseColorFactor(new Color(MathUtils.random(0.25f, 1f),
				MathUtils.random(0.25f, 1f), MathUtils.random(0.25f, 1f), 1f)));
		}
	}

	public void set (float x, float y, float z, byte voxel) {
		int ix = (int)x;
		int iy = (int)y;
		int iz = (int)z;
		int chunkX = ix / CHUNK_SIZE_X;
		if (chunkX < 0 || chunkX >= chunksX) return;
		int chunkY = iy / CHUNK_SIZE_Y;
		if (chunkY < 0 || chunkY >= chunksY) return;
		int chunkZ = iz / CHUNK_SIZE_Z;
		if (chunkZ < 0 || chunkZ >= chunksZ) return;
		chunks.get(chunkX + chunkZ * chunksX + chunkY * chunksX * chunksZ).set(ix % CHUNK_SIZE_X, iy % CHUNK_SIZE_Y, iz % CHUNK_SIZE_Z,
			voxel);
	}

	public byte get (float x, float y, float z) {
		VoxelChunk chunk = getChunk(x, y, z);
		if(chunk == null) return 0;
		return chunk.get((int)x % CHUNK_SIZE_X, (int)y % CHUNK_SIZE_Y,(int)z % CHUNK_SIZE_Z);
	}

	private int getChunkIndex(float x, float y, float z) {
		int ix = (int)x;
		int iy = (int)y;
		int iz = (int)z;
		int chunkX = ix / CHUNK_SIZE_X;
		if (chunkX < 0 || chunkX >= chunksX) return -1;
		int chunkY = iy / CHUNK_SIZE_Y;
		if (chunkY < 0 || chunkY >= chunksY) return -1;
		int chunkZ = iz / CHUNK_SIZE_Z;
		if (chunkZ < 0 || chunkZ >= chunksZ) return -1;
		return chunkX + chunkZ * chunksX + chunkY * chunksX * chunksZ;
	}

	public VoxelChunk getChunk(float x, float y, float z) {
		int index = getChunkIndex(x, y, z);
		if(index == -1) return null;
		return chunks.get(index);
	}

	public int[] getVisibleChunkIndices(float x, float y, float z) {
		int chunkColumns = visibleChunkRadiusXZ * 2 - 1;
		int visibleChunkXZ = (int)Math.pow(chunkColumns, 2);
		int visibleChunkY = visibleChunkRadiusY * 2 - 1;
		int[] chunkIndices = new int[visibleChunkXZ * visibleChunkY];

		for(int index=0, layer=1, diffY=visibleChunkRadiusY; layer < visibleChunkY; layer++) {
			float offsetY = (visibleChunkY - diffY) * CHUNK_SIZE_Y;
			diffY++;

			for(int column=1, diffX=visibleChunkRadiusXZ; column < chunkColumns; column++) {
				float offsetX = -(visibleChunkXZ - diffX) * CHUNK_SIZE_X;
				diffX++;

				for(int chunk=1, diffZ=visibleChunkRadiusXZ; chunk < chunkColumns; chunk++) {
					float offsetZ = (visibleChunkXZ - diffZ) * CHUNK_SIZE_Z;
					diffZ++;

					chunkIndices[index++] = getChunkIndex(offsetX + x, offsetY + y, offsetZ + z);
				}
			}
		}
		return chunkIndices;
	}

	public float getHighest (float x, float z) {
		int ix = (int)x;
		int iz = (int)z;
		if (ix < 0 || ix >= voxelsX) return 0;
		if (iz < 0 || iz >= voxelsZ) return 0;
		// FIXME optimize
		for (int y = voxelsY - 1; y > 0; y--) {
			if (get(ix, y, iz) > 0) return y + 1;
		}
		return 0;
	}

	public int getVisibleChunkRadiusXZ() {
		return visibleChunkRadiusXZ;
	}

	public int getVisibleChunkRadiusY() {
		return visibleChunkRadiusY;
	}

	public void setVisibleChunkRadius(int visibleChunkRadiusXZ, int visibleChunkRadiusY) {
		this.visibleChunkRadiusXZ = visibleChunkRadiusXZ;
		this.visibleChunkRadiusY = visibleChunkRadiusY;
	}

	public void setColumn (float x, float y, float z, byte voxel) {
		int ix = (int)x;
		int iy = (int)y;
		int iz = (int)z;
		if (ix < 0 || ix >= voxelsX) return;
		if (iy < 0 || iy >= voxelsY) return;
		if (iz < 0 || iz >= voxelsZ) return;
		// FIXME optimize
		for (; iy > 0; iy--) {
			set(ix, iy, iz, voxel);
		}
	}

	public void setCube (float x, float y, float z, float width, float height, float depth, byte voxel) {
		int ix = (int)x;
		int iy = (int)y;
		int iz = (int)z;
		int iwidth = (int)width;
		int iheight = (int)height;
		int idepth = (int)depth;
		int startX = Math.max(ix, 0);
		int endX = Math.min(voxelsX, ix + iwidth);
		int startY = Math.max(iy, 0);
		int endY = Math.min(voxelsY, iy + iheight);
		int startZ = Math.max(iz, 0);
		int endZ = Math.min(voxelsZ, iz + idepth);
		// FIXME optimize
		for (iy = startY; iy < endY; iy++) {
			for (iz = startZ; iz < endZ; iz++) {
				for (ix = startX; ix < endX; ix++) {
					set(ix, iy, iz, voxel);
				}
			}
		}
	}

}
