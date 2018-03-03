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

package net.smoofyuniverse.dungeon.gen.populator.spawner;

import net.smoofyuniverse.dungeon.gen.populator.FlagManager.ChunkInfo;
import net.smoofyuniverse.dungeon.gen.populator.RoomPopulator;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class BossRoomEasyPopulator extends RoomPopulator {

	public BossRoomEasyPopulator() {
		super("boss_room_easy");
	}

	@Override
	public void populateRoom(ChunkInfo info, World w, Extent c, Random r, int layer, int room, int x, int y, int z) {
		info.setFlag(layer, room, true);
		y += getFloorOffset(c, x, y, z);

		for (int dx = 0; dx < 7; dx++)
			for (int dz = 0; dz < 7; dz++)
				c.setBlockType(x + dx, y, z + dz, BlockTypes.MOSSY_COBBLESTONE);

		generateSpawner(c, x + 1, y + 1, z + 1, EntityTypes.ZOMBIE);
		generateSpawner(c, x + 3, y + 1, z + 3, EntityTypes.PIG_ZOMBIE);
		generateSpawner(c, x + 5, y + 1, z + 5, EntityTypes.SPIDER);

		c.setBlockType(x + 1, y + 1, z + 5, BlockTypes.COAL_ORE);
		c.setBlockType(x + 5, y + 1, z + 1, BlockTypes.COAL_ORE);
	}

	@Override
	public float getRoomIterationChance() {
		return 0.004f;
	}

	@Override
	public int getMaximumLayer() {
		return 5;
	}
}
