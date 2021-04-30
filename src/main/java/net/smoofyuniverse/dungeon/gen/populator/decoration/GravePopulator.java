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

import com.flowpowered.math.vector.Vector2i;
import net.smoofyuniverse.dungeon.gen.populator.api.info.RoomInfo;
import net.smoofyuniverse.dungeon.gen.populator.core.RoomPopulator;
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

import static com.flowpowered.math.GenericMath.floor;

public class GravePopulator extends RoomPopulator {
	public static final BlockState SIGN = BlockTypes.STANDING_SIGN.getDefaultState().with(Keys.DIRECTION, Direction.WEST).get();
	public static final List<Text> LINES = Arrays.asList(Text.EMPTY, Text.of("R.I.P"), Text.EMPTY, Text.EMPTY);

	public GravePopulator() {
		super("grave");
		roomChance(0.006f);
	}

	@Override
	protected Vector2i getLayers(int layersCount) {
		return new Vector2i(1, floor(layersCount * 0.7));
	}

	@Override
	public boolean populateRoom(RoomInfo info, World w, Extent c, Random r) {
		int x = info.minX + r.nextInt(4) + 3, z = info.minZ + r.nextInt(6) + 1;
		int y = info.minY + info.floorOffset + 1;

		int air = 0;
		if (c.getBlockType(x, y - 1, z) == BlockTypes.AIR)
			air++;
		if (c.getBlockType(x - 1, y - 1, z) == BlockTypes.AIR)
			air++;
		if (c.getBlockType(x - 2, y - 1, z) == BlockTypes.AIR)
			air++;

		if (air > 1)
			return false;

		c.setBlockType(x, y, z, BlockTypes.DOUBLE_STONE_SLAB);
		c.setBlockType(x - 1, y, z, BlockTypes.STONE_SLAB);
		c.setBlockType(x - 2, y, z, BlockTypes.STONE_SLAB);

		c.setBlock(x, y + 1, z, SIGN);
		c.getTileEntity(x, y + 1, z).get().offer(Keys.SIGN_LINES, LINES);

		return true;
	}
}
