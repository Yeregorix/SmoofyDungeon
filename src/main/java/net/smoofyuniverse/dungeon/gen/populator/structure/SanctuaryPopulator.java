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
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class SanctuaryPopulator extends RoomPopulator {

	@Override
	public int getMaximumLayer() {
		return 0;
	}

	@Override
	public float getRoomChance() {
		return 0.003f;
	}

	@Override
	public void populateRoom(World w, Extent c, Random r, int layer, int room, int x, int y, int z) {
		RoomPopulator.setFlag(c, layer, room, true, this.cause);

		y += getFloorOffset(c, x, y, z);

		for (int dx = 0; dx < 8; dx++) {
			for (int dz = 0; dz < 8; dz++)
				c.setBlockType(x + dx, y, z + dz, BlockTypes.OBSIDIAN, this.cause);
		}

		c.setBlockType(x + 2, y + 1, z + 2, BlockTypes.GOLD_BLOCK, this.cause);
		c.setBlockType(x + 3, y + 1, z + 2, BlockTypes.NETHERRACK, this.cause);
		c.setBlockType(x + 4, y + 1, z + 2, BlockTypes.NETHERRACK, this.cause);
		c.setBlockType(x + 5, y + 1, z + 2, BlockTypes.GOLD_BLOCK, this.cause);

		c.setBlockType(x + 2, y + 1, z + 3, BlockTypes.NETHERRACK, this.cause);
		c.setBlockType(x + 3, y + 1, z + 3, BlockTypes.SOUL_SAND, this.cause);
		c.setBlockType(x + 4, y + 1, z + 3, BlockTypes.SOUL_SAND, this.cause);
		c.setBlockType(x + 5, y + 1, z + 3, BlockTypes.NETHERRACK, this.cause);

		c.setBlockType(x + 2, y + 1, z + 4, BlockTypes.GOLD_BLOCK, this.cause);
		c.setBlockType(x + 3, y + 1, z + 4, BlockTypes.NETHERRACK, this.cause);
		c.setBlockType(x + 4, y + 1, z + 4, BlockTypes.NETHERRACK, this.cause);
		c.setBlockType(x + 5, y + 1, z + 4, BlockTypes.GOLD_BLOCK, this.cause);

		c.setBlockType(x + 2, y + 2, z + 2, BlockTypes.TORCH, this.cause);
		c.setBlockType(x + 5, y + 2, z + 2, BlockTypes.TORCH, this.cause);
		c.setBlockType(x + 2, y + 2, z + 4, BlockTypes.TORCH, this.cause);
		c.setBlockType(x + 5, y + 2, z + 4, BlockTypes.TORCH, this.cause);
	}
}
