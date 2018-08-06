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

import com.flowpowered.math.vector.Vector2i;
import net.smoofyuniverse.dungeon.gen.populator.api.info.RoomInfo;
import net.smoofyuniverse.dungeon.gen.populator.core.RoomPopulator;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

import static com.flowpowered.math.GenericMath.floor;

public class LadderPopulator extends RoomPopulator {

	public LadderPopulator() {
		super("ladder");
		roomChance(0.05f);
	}

	@Override
	protected Vector2i getLayers(int layersCount) {
		return new Vector2i(0, floor(layersCount * 0.7));
	}

	@Override
	public boolean populateRoom(RoomInfo info, World w, Extent c, Random r) {
		int x = info.minX, z = info.minZ;
		int y = info.minY + info.floorOffset + 1;

		Direction dir = null;
		switch (r.nextInt(4)) {
			case 0:
				dir = Direction.EAST;
				x += 1;
				z += r.nextBoolean() ? 0 : 7;
				break;
			case 1:
				dir = Direction.WEST;
				x += 6;
				z += r.nextBoolean() ? 0 : 7;
				break;
			case 2:
				dir = Direction.SOUTH;
				x += r.nextBoolean() ? 0 : 7;
				z += 1;
				break;
			case 3:
				dir = Direction.NORTH;
				x += r.nextBoolean() ? 0 : 7;
				z += 6;
				break;
		}

		if (c.getBlockType(x, y, z) == BlockTypes.AIR) {
			// We use Keys.DIRECTION instead of EnumTraits.LADDER_FACING since Direction cannot be cast to net.minecraft.util.EnumFacing and so is not recognized by the implementation (issue to report)
			BlockState state = BlockTypes.LADDER.getDefaultState().with(Keys.DIRECTION, dir).get();
			for (int i = 0; i < 8; i++)
				c.setBlock(x, y + i, z, state);

			return true;
		}
		return false;
	}
}
