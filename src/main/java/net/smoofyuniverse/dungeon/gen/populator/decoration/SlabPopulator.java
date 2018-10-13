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
import org.spongepowered.api.data.type.PortionTypes;
import org.spongepowered.api.data.type.SlabTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class SlabPopulator extends RoomPopulator {
	public static final BlockState TOP_SLAB = BlockTypes.STONE_SLAB.getDefaultState()
			.withTrait(EnumTraits.STONE_SLAB_HALF, PortionTypes.TOP).get()
			.withTrait(EnumTraits.STONE_SLAB_VARIANT, SlabTypes.COBBLESTONE).get();
	public static final BlockState BOTTOM_SLAB = BlockTypes.STONE_SLAB.getDefaultState()
			.withTrait(EnumTraits.STONE_SLAB_HALF, PortionTypes.BOTTOM).get()
			.withTrait(EnumTraits.STONE_SLAB_VARIANT, SlabTypes.COBBLESTONE).get();

	public SlabPopulator() {
		super("slab");
		roomIterations(8, 5);
		roomIterationChance(0.25f);
	}

	@Override
	public boolean populateRoom(RoomInfo info, World w, Extent c, Random r) {
		boolean ceiling = r.nextBoolean();
		int x = info.minX + r.nextInt(6) + 1, z = info.minZ + r.nextInt(6) + 1;
		int y = info.minY + (ceiling ? (info.ceilingOffset + 5) : (info.floorOffset + 1));

		if (c.getBlockType(x, y, z) == BlockTypes.AIR && c.getBlockType(x, y + (ceiling ? 1 : -1), z) != BlockTypes.AIR) {
			c.setBlock(x, y, z, ceiling ? TOP_SLAB : BOTTOM_SLAB);
			return true;
		}
		return false;
	}
}
