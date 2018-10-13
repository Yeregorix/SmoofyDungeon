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

package net.smoofyuniverse.dungeon.gen.populator.spawner;

import com.flowpowered.math.vector.Vector2i;
import net.smoofyuniverse.dungeon.gen.populator.api.info.RoomInfo;
import net.smoofyuniverse.dungeon.gen.populator.core.RoomPopulator;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

import static com.flowpowered.math.GenericMath.floor;

public class CreeperRoomPopulator extends RoomPopulator {

	public CreeperRoomPopulator() {
		super("creeper_room");
		roomChance(0.003f, 0.002f);
	}

	@Override
	protected Vector2i getLayers(int layersCount) {
		return new Vector2i(0, floor(layersCount * 0.6));
	}

	@Override
	public boolean populateRoom(RoomInfo info, World w, Extent c, Random r) {
		info.flag = true;
		int x = info.minX, z = info.minZ;
		int y = info.minY + info.floorOffset + 1;

		c.setBlockType(x + 3, y, z + 4, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 4, y, z + 3, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 3, y, z + 2, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 2, y, z + 3, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 3, y + 1, z + 3, BlockTypes.NETHER_BRICK);

		generateSpawner(c, x + 3, y, z + 3, EntityTypes.CREEPER);

		return true;
	}
}
