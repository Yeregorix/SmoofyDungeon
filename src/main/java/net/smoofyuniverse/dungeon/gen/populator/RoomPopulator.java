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

package net.smoofyuniverse.dungeon.gen.populator;

import com.flowpowered.math.vector.Vector3i;
import net.smoofyuniverse.dungeon.util.ResourceUtil;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public abstract class RoomPopulator extends LayerPopulator {

	protected RoomPopulator() {}

	@Override
	public final void populateLayer(World w, Extent c, Random r, int layer, int y) {
		Vector3i min = c.getBlockMin();
		float itChance = getRoomIterationChance() + (getRoomIterationChanceAdditionPerLayer() * layer);
		int itMax = getRoomIterationMax();
		int room = -1;
		for (int x = 0; x < 16; x += 8)
			for (int z = 0; z < 16; z += 8) {
				room++;
				if (hasFlag(c, layer, room))
					continue;
				if (ResourceUtil.random(getRoomChance(), r)) {
					int itCount = 0;
					for (int it = 0; it < getRoomIterations(); it++) {
						if (itCount >= itMax && itMax >= 0)
							break;
						if (ResourceUtil.random(itChance, r)) {
							itCount++;
							populateRoom(w, c, r, layer, room, min.getX() + x, y, min.getZ() + z);
						}
					}
				}
			}
	}

	public float getRoomIterationChance() {
		return 1.0f;
	}

	public float getRoomIterationChanceAdditionPerLayer() {
		return 0.0f;
	}

	public int getRoomIterationMax() {
		return -1;
	}

	public static boolean hasFlag(Extent chunk, int layer, int room) {
		return chunk.getBlockType(chunk.getBlockMin().add(layer + 1, 0, room + 1)) == BlockTypes.BARRIER;
	}

	public float getRoomChance() {
		return 1.0f;
	}

	public int getRoomIterations() {
		return 1;
	}

	public abstract void populateRoom(World w, Extent c, Random r, int layer, int room, int x, int y, int z);

	public static int getFloorOffset(Extent chunk, int x, int y, int z) {
		BlockType type = chunk.getBlockType(x + 3, y, z + 3);
		if (type == BlockTypes.COBBLESTONE || type == BlockTypes.MOSSY_COBBLESTONE || type == BlockTypes.NETHERRACK || type == BlockTypes.SOUL_SAND)
			return 0;
		return 1;
	}

	public static int getCeilingOffset(Extent chunk, int x, int y, int z) {
		BlockType type = chunk.getBlockType(x + 3, y + 6, z + 3);
		if (type == BlockTypes.COBBLESTONE || type == BlockTypes.MOSSY_COBBLESTONE || type == BlockTypes.NETHERRACK || type == BlockTypes.SOUL_SAND)
			return 0;
		return 1;
	}

	public static void setFlag(Extent chunk, int layer, int room, boolean v, Cause c) {
		chunk.setBlockType(chunk.getBlockMin().add(layer + 1, 0, room + 1), v ? BlockTypes.BARRIER : BlockTypes.AIR, c);
	}
}
