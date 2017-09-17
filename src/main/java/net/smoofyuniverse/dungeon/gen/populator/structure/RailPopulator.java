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

package net.smoofyuniverse.dungeon.gen.populator.structure;

import com.flowpowered.math.vector.Vector3i;
import net.smoofyuniverse.dungeon.gen.populator.RoomPopulator;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RailPopulator extends RoomPopulator {
	private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

	@Override
	public int getMinimumLayer() {
		return 2;
	}

	@Override
	public float getRoomIterationChance() {
		return 0.06f;
	}

	@Override
	public float getRoomIterationChanceAdditionPerLayer() {
		return -0.004f;
	}

	@Override
	public int getRoomIterationMax() {
		return 2;
	}

	@Override
	public int getRoomIterations() {
		return 3;
	}

	@Override
	public void populateRoom(World w, Extent c, Random r, int layer, int room, int x, int y, int z) {
		y += getFloorOffset(c, x, y, z) + 1;

		int ox = r.nextInt(6) + 1;
		int oz = r.nextInt(6) + 1;
		int[] dir1 = DIRECTIONS[r.nextInt(4)], dir2 = DIRECTIONS[r.nextInt(4)];

		List<Entity> entities = new ArrayList<>();

		int dx = ox;
		int dz = oz;
		while (dx >= 0 && dx < 8 && dz >= 0 && dz < 8) {
			if (r.nextFloat() > 0.2f) {
				c.setBlockType(x + dx, y, z + dz, BlockTypes.RAIL);
				if (r.nextFloat() < 0.01f)
					entities.add(c.createEntity(EntityTypes.RIDEABLE_MINECART, new Vector3i(x + dx, y, z + dz)));
			}

			dx += dir1[0];
			dz += dir1[1];
		}

		if (dir1 != dir2) {
			dx = ox;
			dz = oz;
			while (dx >= 0 && dx < 8 && dz >= 0 && dz < 8) {
				if (r.nextFloat() > 0.2f) {
					c.setBlockType(x + dx, y, z + dz, BlockTypes.RAIL);
					if (r.nextFloat() < 0.01f)
						entities.add(c.createEntity(EntityTypes.RIDEABLE_MINECART, new Vector3i(x + dx, y, z + dz)));
				}

				dx += dir2[0];
				dz += dir2[1];
			}
		}

		if (!entities.isEmpty())
			c.spawnEntities(entities);
	}
}
