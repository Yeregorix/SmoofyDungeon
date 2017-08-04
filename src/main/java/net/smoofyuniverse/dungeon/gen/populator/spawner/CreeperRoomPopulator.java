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

import net.smoofyuniverse.dungeon.gen.populator.RoomPopulator;
import net.smoofyuniverse.dungeon.util.Minecraft;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class CreeperRoomPopulator extends RoomPopulator {

	@Override
	public void populateRoom(World w, Extent c, Random r, int layer, int room, int x, int y, int z, int floorOffset, int ceilingOffset) {
		RoomPopulator.setFlag(c, layer, room, true, this.cause);
		y += floorOffset;

		c.setBlockType(x + 3, y + 1, z + 4, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 4, y + 1, z + 3, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 3, y + 1, z + 2, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 2, y + 1, z + 3, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 3, y + 2, z + 3, BlockTypes.NETHER_BRICK, this.cause);

		c.setBlockType(x + 3, y + 1, z + 3, BlockTypes.MOB_SPAWNER, this.cause);
		Minecraft.setSpawnerType(c.getTileEntity(x + 3, y + 1, z + 3).get(), EntityTypes.CREEPER);
	}

	@Override
	public float getRoomIterationChance() {
		return 0.003f;
	}

	@Override
	public float getRoomIterationChanceAdditionPerLayer() {
		return -0.0003f;
	}

	@Override
	public int getMaximumLayer() {
		return 4;
	}
}
