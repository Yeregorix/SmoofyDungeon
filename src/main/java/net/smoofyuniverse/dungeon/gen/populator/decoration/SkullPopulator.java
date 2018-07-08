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

import net.smoofyuniverse.dungeon.gen.populator.RoomPopulator;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class SkullPopulator extends RoomPopulator {
	public static final BlockState SKULL = BlockTypes.SKULL.getDefaultState().with(Keys.DIRECTION, Direction.UP).get();

	public SkullPopulator() {
		super("skull");
		layers(0, 3);
		roomIterations(5, 3);
		roomIterationChance(0.002f, 0f);
	}

	@Override
	public boolean populateRoom(World w, Extent c, Random r, int layer, int room, int x, int y, int z) {
		x += r.nextInt(6) + 1;
		y += getFloorOffset(c, x, y, z) + 1;
		z += r.nextInt(6) + 1;

		if (c.getBlockType(x, y - 1, z) == BlockTypes.AIR)
			return false;

		if (r.nextBoolean()) {
			c.setBlockType(x, y, z, BlockTypes.FENCE);
			y++;
		}

		c.setBlock(x, y, z, SKULL);
		c.getTileEntity(x, y, z).get().offer(Keys.DIRECTION, Direction.values()[r.nextInt(16)]);

		return true;
	}
}
