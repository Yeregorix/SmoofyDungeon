/*
 * Copyright (c) 2017-2021 Hugo Dupanloup (Yeregorix)
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
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class IronBarsPopulator extends RoomPopulator {

	public IronBarsPopulator() {
		super("iron_bars");
		roomChance(0.1f);
		roomIterations(10, 6);
		roomIterationChance(0.6f);
	}

	@Override
	public boolean populateRoom(RoomInfo info, World w, Extent c, Random r) {
		int x = info.minX, z = info.minZ;
		int y = info.minY + r.nextInt(5 - info.floorOffset) + info.floorOffset + 1;

		switch (r.nextInt(4)) {
			case 0:
				x += 7;
			case 1:
				z += r.nextInt(6) + 1;
				break;
			case 2:
				z += 7;
			case 3:
				x += r.nextInt(6) + 1;
				break;
		}

		BlockType type = c.getBlockType(x, y, z);
		if (type == BlockTypes.COBBLESTONE || type == BlockTypes.MOSSY_COBBLESTONE || type == BlockTypes.STONEBRICK) {
			c.setBlockType(x, y, z, BlockTypes.IRON_BARS);
			return true;
		}
		return false;
	}
}
