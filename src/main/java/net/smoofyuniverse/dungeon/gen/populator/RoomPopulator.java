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
import net.smoofyuniverse.dungeon.gen.populator.FlagManager.ChunkInfo;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public abstract class RoomPopulator extends LayerPopulator {
	private float roomChance = 1f, roomChanceAPL = 0f;
	private int roomItAttempts = 1, roomItMax = 0;
	private float roomItChance = 1, roomItChanceAPL = 0f;

	protected RoomPopulator(String name) {
		super(name);
	}

	protected final void roomChance(float chance, float APL) {
		validateProbability(chance, APL);
		this.roomChance = chance;
		this.roomChanceAPL = APL;
	}

	protected final void roomIterations(int attempts, int max) {
		validateIterations(attempts, max);
		this.roomItAttempts = attempts;
		this.roomItMax = max;
	}

	protected final void roomIterationChance(float chance, float APL) {
		validateProbability(chance, APL);
		this.roomItChance = chance;
		this.roomItChanceAPL = APL;
	}

	@Override
	public final boolean populateLayer(ChunkInfo info, World w, Extent c, Random r, int layer, int y) {
		Vector3i min = c.getBlockMin();
		float chance = this.roomChance + layer * this.roomChanceAPL, itChance = this.roomItChance + layer * this.roomItChanceAPL;
		boolean success = false;

		int room = -1;
		for (int x = 0; x < 16; x += 8) {
			for (int z = 0; z < 16; z += 8) {
				room++;
				if (info.getFlag(layer, room))
					continue;

				if (random(r, chance)) {
					int itCount = 0;

					for (int it = 0; it < this.roomItAttempts; it++) {
						if (this.roomItMax != 0 && itCount >= this.roomItMax)
							break;

						if (random(r, itChance) && populateRoom(info, w, c, r, layer, room, min.getX() + x, y, min.getZ() + z))
							itCount++;
					}

					if (itCount != 0)
						success = true;
				}
			}
		}

		return success;
	}

	public boolean populateRoom(ChunkInfo info, World w, Extent c, Random r, int layer, int room, int x, int y, int z) {
		return populateRoom(w, c, r, layer, room, x, y, z);
	}

	public boolean populateRoom(World w, Extent c, Random r, int layer, int room, int x, int y, int z) {
		throw new UnsupportedOperationException();
	}

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
}
