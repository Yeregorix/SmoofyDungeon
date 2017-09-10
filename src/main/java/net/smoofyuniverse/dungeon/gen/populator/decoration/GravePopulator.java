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
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GravePopulator extends RoomPopulator {
	public static final BlockState SIGN = BlockTypes.STANDING_SIGN.getDefaultState().with(Keys.DIRECTION, Direction.WEST).get();
	public static final List<Text> LINES = Arrays.asList(Text.EMPTY, Text.of("R.I.P"), Text.EMPTY, Text.EMPTY);

	@Override
	public void populateRoom(World w, Extent c, Random r, int layer, int room, int x, int y, int z) {
		x += r.nextInt(4) + 3;
		y += getFloorOffset(c, x, y, z) + 1;
		z += r.nextInt(6) + 1;

		c.setBlockType(x, y, z, BlockTypes.DOUBLE_STONE_SLAB);
		c.setBlockType(x - 1, y, z, BlockTypes.STONE_SLAB);
		c.setBlockType(x - 2, y, z, BlockTypes.STONE_SLAB);

		c.setBlock(x, y + 1, z, SIGN);
		c.getTileEntity(x, y + 1, z).get().offer(Keys.SIGN_LINES, LINES);
	}

	@Override
	public float getRoomIterationChance() {
		return 0.005f;
	}

	@Override
	public int getMinimumLayer() {
		return 1;
	}

	@Override
	public int getMaximumLayer() {
		return 5;
	}
}
