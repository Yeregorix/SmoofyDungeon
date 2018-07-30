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

import net.smoofyuniverse.dungeon.gen.populator.ChunkInfo;
import net.smoofyuniverse.dungeon.gen.populator.core.RoomPopulator;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class StoneRoomPopulator extends RoomPopulator {
	private static final BlockType[] ORES = {BlockTypes.COAL_ORE, BlockTypes.DIAMOND_ORE, BlockTypes.EMERALD_ORE,
			BlockTypes.GOLD_ORE, BlockTypes.IRON_ORE, BlockTypes.LAPIS_ORE, BlockTypes.REDSTONE_ORE, BlockTypes.QUARTZ_ORE};

	public StoneRoomPopulator() {
		super("stone_room");
		roomChance(0.005f, 0f);
	}

	@Override
	public boolean populateRoom(ChunkInfo info, World w, Extent c, Random r, int layer, int room, int x, int y, int z) {
		info.setFlag(layer, room, true);

		y += getFloorOffset(c, x, y, z) + 1;
		int ceilingY = y + getCeilingOffset(c, x, y, z) + 6;

		for (int cy = y; cy < ceilingY; cy++) {
			for (int i = 0; i < 8; i++) {
				c.setBlockType(x + i, cy, z, BlockTypes.STONEBRICK);
				c.setBlockType(x + i, cy, z + 7, BlockTypes.STONEBRICK);

				c.setBlockType(x, cy, z + i, BlockTypes.STONEBRICK);
				c.setBlockType(x + 7, cy, z + i, BlockTypes.STONEBRICK);
			}
			for (int dx = 1; dx < 7; dx++) {
				for (int dz = 1; dz < 7; dz++)
					c.setBlockType(x + dx, cy, z + dz, randomType(r));
			}
		}

		return true;
	}

	public static BlockType randomType(Random r) {
		return r.nextFloat() < 0.05f ? ORES[r.nextInt(ORES.length)] : BlockTypes.STONE;
	}
}
