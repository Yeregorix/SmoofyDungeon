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

import net.smoofyuniverse.dungeon.gen.populator.RoomPopulator;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class WaterWellPopulator extends RoomPopulator {
	public static final BlockState NORTH_STAIRS = BlockTypes.OAK_STAIRS.getDefaultState().with(Keys.DIRECTION, Direction.NORTH).get(),
			SOUTH_STAIRS = BlockTypes.OAK_STAIRS.getDefaultState().with(Keys.DIRECTION, Direction.SOUTH).get(),
			EAST_STAIRS = BlockTypes.OAK_STAIRS.getDefaultState().with(Keys.DIRECTION, Direction.EAST).get(),
			WEST_STAIRS = BlockTypes.OAK_STAIRS.getDefaultState().with(Keys.DIRECTION, Direction.WEST).get();

	public WaterWellPopulator() {
		super("water_well");
	}

	@Override
	public int getMinimumLayer() {
		return 2;
	}

	@Override
	public float getRoomChance() {
		return 0.002f;
	}

	@Override
	public void populateRoom(World w, Extent c, Random r, int layer, int room, int x, int y, int z) {
		y += getFloorOffset(c, x, y, z) + 1;

		for (int dx = 2; dx < 5; dx++) {
			for (int dz = 2; dz < 5; dz++) {
				c.setBlockType(x + dx, y, z + dz, BlockTypes.STONEBRICK);
				c.setBlockType(x + dx, y + 1, z + dz, BlockTypes.AIR);
				c.setBlockType(x + dx, y + 3, z + dz, BlockTypes.AIR);
			}
		}

		c.setBlockType(x + 3, y, z + 3, BlockTypes.WATER);

		c.setBlockType(x + 2, y + 1, z + 2, BlockTypes.FENCE);
		c.setBlockType(x + 2, y + 1, z + 4, BlockTypes.FENCE);
		c.setBlockType(x + 4, y + 1, z + 2, BlockTypes.FENCE);
		c.setBlockType(x + 4, y + 1, z + 4, BlockTypes.FENCE);

		c.setBlockType(x + 3, y + 2, z + 3, BlockTypes.GLOWSTONE);
		c.setBlockType(x + 2, y + 2, z + 2, BlockTypes.WOODEN_SLAB);
		c.setBlockType(x + 2, y + 2, z + 4, BlockTypes.WOODEN_SLAB);
		c.setBlockType(x + 4, y + 2, z + 2, BlockTypes.WOODEN_SLAB);
		c.setBlockType(x + 4, y + 2, z + 4, BlockTypes.WOODEN_SLAB);
		c.setBlock(x + 2, y + 2, z + 3, EAST_STAIRS);
		c.setBlock(x + 3, y + 2, z + 2, SOUTH_STAIRS);
		c.setBlock(x + 3, y + 2, z + 4, NORTH_STAIRS);
		c.setBlock(x + 4, y + 2, z + 3, WEST_STAIRS);
	}
}
