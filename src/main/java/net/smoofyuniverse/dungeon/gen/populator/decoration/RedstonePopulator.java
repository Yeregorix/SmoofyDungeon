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

package net.smoofyuniverse.dungeon.gen.populator.decoration;

import net.smoofyuniverse.dungeon.gen.populator.core.RoomPopulator;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class RedstonePopulator extends RoomPopulator {
	private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

	public RedstonePopulator() {
		super("redstone");
		layers(1, 5);
		roomIterations(3, 2);
		roomIterationChance(0.06f, -0.004f);
	}

	@Override
	public boolean populateRoom(World w, Extent c, Random r, int layer, int room, int x, int y, int z) {
		y += getFloorOffset(c, x, y, z) + 1;

		int ox = r.nextInt(4) + 2;
		int oz = r.nextInt(4) + 2;
		int[] dir1 = DIRECTIONS[r.nextInt(4)], dir2 = DIRECTIONS[r.nextInt(4)];

		int dx = ox;
		int dz = oz;
		while (dx >= 1 && dx < 7 && dz >= 1 && dz < 7) {
			if (r.nextFloat() < 0.8f)
				c.setBlockType(x + dx, y, z + dz, BlockTypes.REDSTONE_WIRE);

			dx += dir1[0];
			dz += dir1[1];
		}

		if (r.nextFloat() < 0.3f) {
			dx -= dir1[0];
			dz -= dir1[1];
			c.setBlockType(x + dx, y, z + dz, BlockTypes.STONE_PRESSURE_PLATE);
		}

		if (dir1 != dir2) {
			dx = ox;
			dz = oz;
			while (dx >= 1 && dx < 7 && dz >= 1 && dz < 7) {
				if (r.nextFloat() < 0.8f)
					c.setBlockType(x + dx, y, z + dz, BlockTypes.REDSTONE_WIRE);

				dx += dir2[0];
				dz += dir2[1];
			}

			if (r.nextFloat() < 0.3f) {
				dx -= dir2[0];
				dz -= dir2[1];
				c.setBlockType(x + dx, y, z + dz, BlockTypes.TNT);
			}
		}

		return true;
	}
}
