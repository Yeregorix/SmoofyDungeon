/*
 * Copyright (c) 2017 Hugo Dupanloup (Yeregorix)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.smoofyuniverse.dungeon.gen.populator.api.info;

import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.extent.Extent;

public final class ChunkInfo {
	public final int minX, minZ, layersCount, topY;
	public boolean flag;
	private LayerInfo[] layers;

	public ChunkInfo(int minX, int minZ, int layersCount) {
		if (layersCount < 0)
			throw new IllegalArgumentException("layersCount");

		this.minX = minX;
		this.minZ = minZ;
		this.layersCount = layersCount;
		this.topY = 4 + layersCount * 6;

		this.layers = new LayerInfo[layersCount];
		for (int i = 0; i < layersCount; i++)
			this.layers[i] = new LayerInfo(this, i);
	}

	public LayerInfo getLayer(int index) {
		return this.layers[index];
	}

	public void configureOffsets(Extent volume) {
		LayerInfo prev = null;
		for (LayerInfo current : this.layers) {
			for (int i = 0; i < 4; i++) {
				RoomInfo room = current.getRoom(i);
				if (volume.getBlockType(room.minX + 3, room.minY + 1, room.minZ + 3) == BlockTypes.COBBLESTONE) {
					room.floorOffset = 1;
					if (prev != null)
						prev.getRoom(i).ceilingOffset = 1;
				}
			}

			prev = current;
		}
	}
}
