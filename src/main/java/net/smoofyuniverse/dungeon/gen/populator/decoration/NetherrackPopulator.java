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

package net.smoofyuniverse.dungeon.gen.populator.decoration;

import net.smoofyuniverse.dungeon.gen.populator.core.RoomPopulator;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class NetherrackPopulator extends RoomPopulator {

	public NetherrackPopulator() {
		super("netherrack");
		layers(0, 1);
		roomIterations(5, 3);
		roomIterationChance(0.05f, 0f);
	}

	@Override
	public boolean populateRoom(World w, Extent c, Random r, int layer, int room, int x, int y, int z) {
		x += r.nextInt(8);
		y += getFloorOffset(c, x, y, z);
		z += r.nextInt(8);

		if (c.getBlockType(x, y, z) == BlockTypes.COBBLESTONE) {
			c.setBlockType(x, y, z, BlockTypes.NETHERRACK);
			if (c.getBlockType(x, y + 1, z) == BlockTypes.AIR && r.nextFloat() < 0.33f)
				c.setBlockType(x, y + 1, z, BlockTypes.FIRE);
			return true;
		}
		return false;
	}
}
