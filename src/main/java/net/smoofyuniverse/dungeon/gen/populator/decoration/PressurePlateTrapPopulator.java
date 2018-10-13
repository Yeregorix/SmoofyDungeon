/*
 * Copyright (c) 2017, 2018 Hugo Dupanloup (Yeregorix)
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

import net.smoofyuniverse.dungeon.gen.populator.api.info.RoomInfo;
import net.smoofyuniverse.dungeon.gen.populator.core.RoomPopulator;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.trait.EnumTraits;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.PortionTypes;
import org.spongepowered.api.data.type.SlabTypes;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class PressurePlateTrapPopulator extends RoomPopulator {
	public static final BlockState STAIRS = BlockTypes.STONE_STAIRS.getDefaultState().with(Keys.PORTION_TYPE, PortionTypes.TOP).get(),
			SLAB = BlockTypes.STONE_SLAB.getDefaultState().withTrait(EnumTraits.STONE_SLAB_VARIANT, SlabTypes.COBBLESTONE).get().with(Keys.PORTION_TYPE, PortionTypes.TOP).get();

	public PressurePlateTrapPopulator() {
		super("pressure_plate_trap");
		roomIterationChance(0.03f, 0.01f);
	}

	@Override
	public boolean populateRoom(RoomInfo info, World w, Extent c, Random r) {
		int x = info.minX + 1 + r.nextInt(6), z = info.minZ + 1 + r.nextInt(6);
		int y = info.minY + info.floorOffset;

		if (c.getBlockType(x, y, z) == BlockTypes.AIR)
			return false;

		c.setBlockType(x, y + 1, z, BlockTypes.STONE_PRESSURE_PLATE);
		c.setBlockType(x, y - 1, z, BlockTypes.TNT);
		if (info.layer.index != 0) {
			c.setBlock(x + 1, y - 1, z, STAIRS.with(Keys.DIRECTION, Direction.WEST).get());
			c.setBlock(x, y - 1, z + 1, STAIRS.with(Keys.DIRECTION, Direction.NORTH).get());
			c.setBlock(x - 1, y - 1, z, STAIRS.with(Keys.DIRECTION, Direction.EAST).get());
			c.setBlock(x, y - 1, z - 1, STAIRS.with(Keys.DIRECTION, Direction.SOUTH).get());

			c.setBlock(x, y - 2, z, SLAB);
		}

		return true;
	}
}
