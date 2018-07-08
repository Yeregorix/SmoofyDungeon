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

public class CreeperRoomPopulator extends RoomPopulator {

	public CreeperRoomPopulator() {
		super("creeper_room");
		layers(0, 4);
		roomChance(0.003f, -0.0003f);
	}

	@Override
	public boolean populateRoom(ChunkInfo info, World w, Extent c, Random r, int layer, int room, int x, int y, int z) {
		info.setFlag(layer, room, true);
		y += getFloorOffset(c, x, y, z);

		c.setBlockType(x + 3, y + 1, z + 4, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 4, y + 1, z + 3, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 3, y + 1, z + 2, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 2, y + 1, z + 3, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 3, y + 2, z + 3, BlockTypes.NETHER_BRICK);

		generateSpawner(c, x + 3, y + 1, z + 3, EntityTypes.CREEPER);

		return true;
	}
}
