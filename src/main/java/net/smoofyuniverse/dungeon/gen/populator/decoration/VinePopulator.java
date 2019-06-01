/*
 * Copyright (c) 2017-2019 Hugo Dupanloup (Yeregorix)
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
import org.spongepowered.api.block.trait.BooleanTraits;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class VinePopulator extends RoomPopulator {
	public static final BlockState VINE = BlockTypes.VINE.getDefaultState(),
			VINE_UP = VINE.withTrait(BooleanTraits.VINE_UP, true).get(),
			VINE_EAST = VINE.withTrait(BooleanTraits.VINE_EAST, true).get(),
			VINE_NORTH = VINE.withTrait(BooleanTraits.VINE_NORTH, true).get(),
			VINE_SOUTH = VINE.withTrait(BooleanTraits.VINE_SOUTH, true).get(),
			VINE_WEST = VINE.withTrait(BooleanTraits.VINE_WEST, true).get();

	public VinePopulator() {
		super("vine");
		roomIterations(10, 0);
		roomIterationChance(0.3f, 0.1f);
	}

	@Override
	public boolean populateRoom(RoomInfo info, World w, Extent c, Random r) {
		int x = info.minX, y = info.minY, z = info.minZ;

		if (r.nextBoolean()) {
			y += r.nextInt(4) + 2;
			switch (r.nextInt(4)) {
				case 0:
					z += r.nextInt(6) + 1;

					if (c.getBlockType(x, y, z) == BlockTypes.STONEBRICK && c.getBlockType(x + 1, y, z) == BlockTypes.AIR) {
						c.setBlock(x + 1, y, z, VINE_WEST);
						return true;
					}
					return false;
				case 1:
					x += 7;
					z += r.nextInt(6) + 1;

					if (c.getBlockType(x, y, z) == BlockTypes.STONEBRICK && c.getBlockType(x - 1, y, z) == BlockTypes.AIR) {
						c.setBlock(x - 1, y, z, VINE_EAST);
						return true;
					}
					return false;
				case 2:
					x += r.nextInt(6) + 1;

					if (c.getBlockType(x, y, z) == BlockTypes.STONEBRICK && c.getBlockType(x, y, z + 1) == BlockTypes.AIR) {
						c.setBlock(x, y, z + 1, VINE_NORTH);
						return true;
					}
					return false;
				case 3:
					x += r.nextInt(6) + 1;
					z += 7;

					if (c.getBlockType(x, y, z) == BlockTypes.STONEBRICK && c.getBlockType(x, y, z - 1) == BlockTypes.AIR) {
						c.setBlock(x, y, z - 1, VINE_SOUTH);
						return true;
					}
					return false;
				default:
					return false;
			}
		} else {
			x += r.nextInt(6) + 1;
			y += info.floorOffset + 5;
			z += r.nextInt(6) + 1;

			if (c.getBlockType(x, y, z) == BlockTypes.AIR && c.getBlockType(x, y + 1, z) != BlockTypes.AIR) {
				c.setBlock(x, y, z, VINE_UP);
				return true;
			}
			return false;
		}
	}
}
