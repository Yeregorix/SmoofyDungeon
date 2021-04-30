/*
 * Copyright (c) 2017-2021 Hugo Dupanloup (Yeregorix)
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

import com.flowpowered.math.vector.Vector2i;
import net.smoofyuniverse.dungeon.gen.populator.api.info.RoomInfo;
import net.smoofyuniverse.dungeon.gen.populator.core.RoomPopulator;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.trait.EnumTraits;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.PortionType;
import org.spongepowered.api.data.type.PortionTypes;
import org.spongepowered.api.data.type.SlabTypes;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class StairsPopulator extends RoomPopulator {

	public static final BlockState STAIRS = BlockTypes.STONE_STAIRS.getDefaultState(),
			SLAB = BlockTypes.STONE_SLAB.getDefaultState().withTrait(EnumTraits.STONE_SLAB_VARIANT, SlabTypes.COBBLESTONE).get();

	public StairsPopulator() {
		super("stairs");
		roomChance(0.02f);
	}

	@Override
	protected Vector2i getLayers(int layersCount) {
		return new Vector2i(0, layersCount - 2);
	}

	@Override
	public boolean populateRoom(RoomInfo info, World w, Extent c, Random r) {
		int x = info.minX, z = info.minZ;
		int floorY = info.minY + info.floorOffset + 1, ceilingY = info.minY + info.ceilingOffset + 6;
		int d = info.ceilingOffset - info.floorOffset;

		c.setBlock(x + 5, floorY, z + 2, getBlock(PortionTypes.BOTTOM, Direction.SOUTH, r));
		c.setBlock(x + 6, floorY, z + 2, getBlock(PortionTypes.BOTTOM, Direction.SOUTH, r));

		c.setBlock(x + 5, floorY + 1, z + 3, getBlock(PortionTypes.BOTTOM, Direction.SOUTH, r));
		c.setBlock(x + 6, floorY + 1, z + 3, getBlock(PortionTypes.BOTTOM, Direction.SOUTH, r));
		c.setBlock(x + 5, floorY, z + 3, getBlock(PortionTypes.TOP, Direction.NORTH, r));
		c.setBlock(x + 6, floorY, z + 3, getBlock(PortionTypes.TOP, Direction.NORTH, r));

		c.setBlock(x + 5, floorY + 2, z + 4, getBlock(PortionTypes.BOTTOM, Direction.SOUTH, r));
		c.setBlock(x + 6, floorY + 2, z + 4, getBlock(PortionTypes.BOTTOM, Direction.SOUTH, r));
		c.setBlock(x + 5, floorY + 1, z + 4, getBlock(PortionTypes.TOP, Direction.NORTH, r));
		c.setBlock(x + 6, floorY + 1, z + 4, getBlock(PortionTypes.TOP, Direction.NORTH, r));

		c.setBlockType(x + 5, floorY + 2, z + 5, BlockTypes.COBBLESTONE);
		c.setBlockType(x + 6, floorY + 2, z + 5, BlockTypes.COBBLESTONE);
		c.setBlockType(x + 5, floorY + 2, z + 6, BlockTypes.COBBLESTONE);
		c.setBlockType(x + 6, floorY + 2, z + 6, BlockTypes.COBBLESTONE);

		c.setBlock(x + 4, floorY + 3, z + 5, getBlock(PortionTypes.BOTTOM, Direction.WEST, r));
		c.setBlock(x + 4, floorY + 3, z + 6, getBlock(PortionTypes.BOTTOM, Direction.WEST, r));
		c.setBlock(x + 4, floorY + 2, z + 5, getBlock(PortionTypes.TOP, Direction.EAST, r));
		c.setBlock(x + 4, floorY + 2, z + 6, getBlock(PortionTypes.TOP, Direction.EAST, r));

		c.setBlock(x + 3, floorY + 4, z + 5, getBlock(PortionTypes.BOTTOM, Direction.WEST, r));
		c.setBlock(x + 3, floorY + 4, z + 6, getBlock(PortionTypes.BOTTOM, Direction.WEST, r));
		c.setBlock(x + 3, floorY + 3, z + 5, getBlock(PortionTypes.TOP, Direction.EAST, r));
		c.setBlock(x + 3, floorY + 3, z + 6, getBlock(PortionTypes.TOP, Direction.EAST, r));

		if (d != -1) {
			c.setBlock(x + 2, floorY + 5, z + 5, getBlock(PortionTypes.BOTTOM, Direction.WEST, r));
			c.setBlock(x + 2, floorY + 5, z + 6, getBlock(PortionTypes.BOTTOM, Direction.WEST, r));
			c.setBlock(x + 2, floorY + 4, z + 5, getBlock(PortionTypes.TOP, Direction.EAST, r));
			c.setBlock(x + 2, floorY + 4, z + 6, getBlock(PortionTypes.TOP, Direction.EAST, r));
		}

		if (d == 1) {
			c.setBlock(x + 1, floorY + 6, z + 5, getBlock(PortionTypes.BOTTOM, Direction.WEST, r));
			c.setBlock(x + 1, floorY + 6, z + 6, getBlock(PortionTypes.BOTTOM, Direction.WEST, r));
			c.setBlock(x + 1, floorY + 5, z + 5, getBlock(PortionTypes.TOP, Direction.EAST, r));
			c.setBlock(x + 1, floorY + 5, z + 6, getBlock(PortionTypes.TOP, Direction.EAST, r));
		}

		if (r.nextBoolean()) {
			for (int y = floorY; y < ceilingY; y++)
				c.setBlockType(x + 4, y, z + 4, BlockTypes.STONEBRICK);
		}

		int dx = x - d;
		c.setBlockType(dx + 3, ceilingY, z + 5, BlockTypes.AIR);
		c.setBlockType(dx + 3, ceilingY, z + 6, BlockTypes.AIR);
		c.setBlockType(dx + 4, ceilingY, z + 5, BlockTypes.AIR);
		c.setBlockType(dx + 4, ceilingY, z + 6, BlockTypes.AIR);
		c.setBlockType(dx + 5, ceilingY, z + 5, BlockTypes.AIR);
		c.setBlockType(dx + 5, ceilingY, z + 6, BlockTypes.AIR);

		c.setBlockType(dx + 2, ceilingY + 1, z + 5, BlockTypes.AIR);
		c.setBlockType(dx + 2, ceilingY + 1, z + 6, BlockTypes.AIR);
		c.setBlockType(dx + 3, ceilingY + 1, z + 5, BlockTypes.AIR);
		c.setBlockType(dx + 3, ceilingY + 1, z + 6, BlockTypes.AIR);
		c.setBlockType(dx + 4, ceilingY + 1, z + 5, BlockTypes.AIR);
		c.setBlockType(dx + 4, ceilingY + 1, z + 6, BlockTypes.AIR);
		c.setBlockType(dx + 5, ceilingY + 1, z + 5, BlockTypes.AIR);
		c.setBlockType(dx + 5, ceilingY + 1, z + 6, BlockTypes.AIR);

		c.setBlockType(dx + 2, ceilingY + 2, z + 5, BlockTypes.AIR);
		c.setBlockType(dx + 2, ceilingY + 2, z + 6, BlockTypes.AIR);
		c.setBlockType(dx + 3, ceilingY + 2, z + 5, BlockTypes.AIR);
		c.setBlockType(dx + 3, ceilingY + 2, z + 6, BlockTypes.AIR);
		c.setBlockType(dx + 4, ceilingY + 2, z + 5, BlockTypes.AIR);
		c.setBlockType(dx + 4, ceilingY + 2, z + 6, BlockTypes.AIR);
		c.setBlockType(dx + 5, ceilingY + 2, z + 5, BlockTypes.AIR);
		c.setBlockType(dx + 5, ceilingY + 2, z + 6, BlockTypes.AIR);

		if (d == -1) {
			c.setBlockType(x + 5, ceilingY, z + 3, BlockTypes.AIR);
			c.setBlockType(x + 6, ceilingY, z + 3, BlockTypes.AIR);
			c.setBlockType(x + 5, ceilingY, z + 4, BlockTypes.AIR);
			c.setBlockType(x + 6, ceilingY, z + 4, BlockTypes.AIR);
		}

		return true;
	}

	public static BlockState getBlock(PortionType portion, Direction dir, Random r) {
		return getBlock(portion, dir, r, 0.9f);
	}

	public static BlockState getBlock(PortionType portion, Direction dir, Random r, float chance) {
		return r.nextFloat() < chance ? STAIRS.with(Keys.PORTION_TYPE, portion).get().with(Keys.DIRECTION, dir).get() : SLAB.with(Keys.PORTION_TYPE, portion).get();
	}
}
