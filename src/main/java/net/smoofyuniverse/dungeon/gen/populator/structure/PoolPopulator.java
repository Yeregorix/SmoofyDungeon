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

import net.smoofyuniverse.dungeon.gen.populator.FlagManager.ChunkInfo;
import net.smoofyuniverse.dungeon.gen.populator.RoomPopulator;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class PoolPopulator extends RoomPopulator {

	@Override
	public float getRoomChance() {
		return 0.005f;
	}

	@Override
	public void populateRoom(ChunkInfo info, World w, Extent c, Random r, int layer, int room, int x, int y, int z) {
		info.setFlag(layer, room, true);

		y += getFloorOffset(c, x, y, z) + 1;
		boolean water = r.nextBoolean();

		for (int dy = 0; dy < 4; dy++) {
			for (int i = 0; i < 8; i++) {
				if (c.getBlockType(x + i, y + dy, z) == BlockTypes.AIR)
					c.setBlockType(x + i, y + dy, z, BlockTypes.BRICK_BLOCK);

				if (c.getBlockType(x + i, y + dy, z + 7) == BlockTypes.AIR)
					c.setBlockType(x + i, y + dy, z + 7, BlockTypes.BRICK_BLOCK);

				if (c.getBlockType(x, y + dy, z + i) == BlockTypes.AIR)
					c.setBlockType(x, y + dy, z + i, BlockTypes.BRICK_BLOCK);

				if (c.getBlockType(x + 7, y + dy, z + i) == BlockTypes.AIR)
					c.setBlockType(x + 7, y + dy, z + i, BlockTypes.BRICK_BLOCK);
			}

			for (int dx = 1; dx < 7; dx++) {
				for (int dz = 1; dz < 7; dz++)
					c.setBlockType(x + dx, y + dy, z + dz, water ? BlockTypes.WATER : BlockTypes.LAVA);
			}
		}
	}
}
