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

import net.smoofyuniverse.dungeon.gen.populator.RoomPopulator;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.PortionTypes;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class StairsPopulator extends RoomPopulator {

	public static final BlockState STAIRS = BlockTypes.STONE_STAIRS.getDefaultState(),
			STAIRS_BOTTOM_WEST = STAIRS.with(Keys.PORTION_TYPE, PortionTypes.BOTTOM).get().with(Keys.DIRECTION, Direction.WEST).get(),
			STAIRS_BOTTOM_SOUTH = STAIRS.with(Keys.PORTION_TYPE, PortionTypes.BOTTOM).get().with(Keys.DIRECTION, Direction.SOUTH).get(),
			STAIRS_TOP_EAST = STAIRS.with(Keys.PORTION_TYPE, PortionTypes.TOP).get().with(Keys.DIRECTION, Direction.EAST).get(),
			STAIRS_TOP_NORTH = STAIRS.with(Keys.PORTION_TYPE, PortionTypes.TOP).get().with(Keys.DIRECTION, Direction.NORTH).get();

	@Override
	public int getMaximumLayer() {
		return 5;
	}

	@Override
	public float getRoomChance() {
		return 0.02f;
	}

	@Override
	public void populateRoom(World w, Extent c, Random r, int layer, int room, int x, int y, int z) {
		y += getFloorOffset(c, x, y, z) + 1;

		c.setBlock(x + 5, y, z + 2, STAIRS_BOTTOM_SOUTH);
		c.setBlock(x + 6, y, z + 2, STAIRS_BOTTOM_SOUTH);
		c.setBlock(x + 5, y + 1, z + 3, STAIRS_BOTTOM_SOUTH);
		c.setBlock(x + 6, y + 1, z + 3, STAIRS_BOTTOM_SOUTH);
		c.setBlock(x + 5, y + 2, z + 4, STAIRS_BOTTOM_SOUTH);
		c.setBlock(x + 6, y + 2, z + 4, STAIRS_BOTTOM_SOUTH);

		c.setBlock(x + 5, y, z + 3, STAIRS_TOP_NORTH);
		c.setBlock(x + 6, y, z + 3, STAIRS_TOP_NORTH);
		c.setBlock(x + 5, y + 1, z + 4, STAIRS_TOP_NORTH);
		c.setBlock(x + 6, y + 1, z + 4, STAIRS_TOP_NORTH);

		c.setBlockType(x + 5, y + 2, z + 5, BlockTypes.COBBLESTONE);
		c.setBlockType(x + 6, y + 2, z + 5, BlockTypes.COBBLESTONE);
		c.setBlockType(x + 5, y + 2, z + 6, BlockTypes.COBBLESTONE);
		c.setBlockType(x + 6, y + 2, z + 6, BlockTypes.COBBLESTONE);

		c.setBlock(x + 4, y + 3, z + 5, STAIRS_BOTTOM_WEST);
		c.setBlock(x + 4, y + 3, z + 6, STAIRS_BOTTOM_WEST);
		c.setBlock(x + 3, y + 4, z + 5, STAIRS_BOTTOM_WEST);
		c.setBlock(x + 3, y + 4, z + 6, STAIRS_BOTTOM_WEST);
		c.setBlock(x + 2, y + 5, z + 5, STAIRS_BOTTOM_WEST);
		c.setBlock(x + 2, y + 5, z + 6, STAIRS_BOTTOM_WEST);

		c.setBlock(x + 4, y + 2, z + 5, STAIRS_TOP_EAST);
		c.setBlock(x + 4, y + 2, z + 6, STAIRS_TOP_EAST);
		c.setBlock(x + 3, y + 3, z + 5, STAIRS_TOP_EAST);
		c.setBlock(x + 3, y + 3, z + 6, STAIRS_TOP_EAST);
		c.setBlock(x + 2, y + 4, z + 5, STAIRS_TOP_EAST);
		c.setBlock(x + 2, y + 4, z + 6, STAIRS_TOP_EAST);

		c.setBlockType(x + 3, y + 5, z + 5, BlockTypes.AIR);
		c.setBlockType(x + 3, y + 5, z + 6, BlockTypes.AIR);
		c.setBlockType(x + 4, y + 5, z + 5, BlockTypes.AIR);
		c.setBlockType(x + 4, y + 5, z + 6, BlockTypes.AIR);
		c.setBlockType(x + 5, y + 5, z + 5, BlockTypes.AIR);
		c.setBlockType(x + 5, y + 5, z + 6, BlockTypes.AIR);

		c.setBlockType(x + 2, y + 6, z + 5, BlockTypes.AIR);
		c.setBlockType(x + 2, y + 6, z + 6, BlockTypes.AIR);
		c.setBlockType(x + 3, y + 6, z + 5, BlockTypes.AIR);
		c.setBlockType(x + 3, y + 6, z + 6, BlockTypes.AIR);
		c.setBlockType(x + 4, y + 6, z + 5, BlockTypes.AIR);
		c.setBlockType(x + 4, y + 6, z + 6, BlockTypes.AIR);
		c.setBlockType(x + 5, y + 6, z + 5, BlockTypes.AIR);
		c.setBlockType(x + 5, y + 6, z + 6, BlockTypes.AIR);

		c.setBlockType(x + 2, y + 7, z + 5, BlockTypes.AIR);
		c.setBlockType(x + 2, y + 7, z + 6, BlockTypes.AIR);
		c.setBlockType(x + 3, y + 7, z + 5, BlockTypes.AIR);
		c.setBlockType(x + 3, y + 7, z + 6, BlockTypes.AIR);
		c.setBlockType(x + 4, y + 7, z + 5, BlockTypes.AIR);
		c.setBlockType(x + 4, y + 7, z + 6, BlockTypes.AIR);
		c.setBlockType(x + 5, y + 7, z + 5, BlockTypes.AIR);
		c.setBlockType(x + 5, y + 7, z + 6, BlockTypes.AIR);
	}
}
