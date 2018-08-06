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

package net.smoofyuniverse.dungeon.gen.populator.core;

import net.smoofyuniverse.dungeon.gen.populator.api.info.LayerInfo;
import net.smoofyuniverse.dungeon.gen.populator.api.info.RoomInfo;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public abstract class RoomPopulator extends LayerPopulator {
	private int roomItAttempts = 1, roomItMax = 0;
	private float roomChanceBase = 1, roomChanceAddition = 0;
	private float roomItChanceBase = 1, roomItChanceAddition = 0;

	protected RoomPopulator(String name) {
		super(name);
	}

	protected final void roomIterations(int attempts, int max) {
		validateIterations(attempts, max);

		this.roomItAttempts = attempts;
		this.roomItMax = max;
	}

	protected final void roomChance(float value) {
		validateProbability(value);

		this.roomChanceBase = value;
		this.roomChanceAddition = 0;
	}

	protected final void roomChance(float from, float to) {
		validateProbability(from);
		validateProbability(to);

		this.roomChanceBase = from;
		this.roomChanceAddition = to - from;
	}

	protected final void roomIterationChance(float value) {
		validateProbability(value);

		this.roomItChanceBase = value;
		this.roomItChanceAddition = 0;
	}

	protected final void roomIterationChance(float from, float to) {
		validateProbability(from);
		validateProbability(to);

		this.roomItChanceBase = from;
		this.roomItChanceAddition = to - from;
	}

	@Override
	public final boolean populateLayer(LayerInfo info, World w, Extent c, Random r, float layerFactor) {
		float chance = this.roomChanceBase + this.roomChanceAddition * layerFactor, itChance = this.roomItChanceBase + this.roomItChanceAddition * layerFactor;
		boolean success = false;

		for (int i = 0; i < 4; i++) {
			RoomInfo room = info.getRoom(i);
			if (room.flag)
				continue;

			if (random(r, chance)) {
				int itCount = 0;

				for (int it = 0; it < this.roomItAttempts; it++) {
					if (this.roomItMax != 0 && itCount >= this.roomItMax)
						break;

					if (random(r, itChance) && populateRoom(room, w, c, r))
						itCount++;
				}

				if (itCount != 0)
					success = true;
			}
		}

		return success;
	}

	public abstract boolean populateRoom(RoomInfo info, World w, Extent c, Random r);

/*	public static int getFloorOffset(Extent chunk, int x, int y, int z) {
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
	} */
}
