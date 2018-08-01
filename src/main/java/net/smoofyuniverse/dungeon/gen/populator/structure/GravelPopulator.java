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

package net.smoofyuniverse.dungeon.gen.populator.structure;

import com.flowpowered.math.vector.Vector2i;
import net.smoofyuniverse.dungeon.gen.populator.core.RoomPopulator;
import net.smoofyuniverse.dungeon.gen.populator.core.info.RoomInfo;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

import static com.flowpowered.math.GenericMath.floor;
import static java.lang.Math.max;

public class GravelPopulator extends RoomPopulator {
	private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

	public GravelPopulator() {
		super("gravel");
		roomIterations(3, 2);
		roomIterationChance(0.05f);
	}

	@Override
	protected Vector2i getLayers(int layersCount) {
		return new Vector2i(max(floor(layersCount * 0.1), 1), layersCount - 1);
	}

	@Override
	public boolean populateRoom(RoomInfo info, World w, Extent c, Random r) {
		int x = info.minX, z = info.minZ;
		int y = info.minY + info.floorOffset + 1;

		int ox = r.nextInt(6) + 1;
		int oz = r.nextInt(6) + 1;
		int oHeight = r.nextInt(2) + 1;
		int[] dir1 = DIRECTIONS[r.nextInt(4)], dir2 = DIRECTIONS[r.nextInt(4)];

		int dx = ox;
		int dz = oz;
		int height = oHeight;
		while (height > 0 && dx >= 0 && dx < 8 && dz >= 0 && dz < 8) {
			for (int dy = 0; dy < height; dy++) {
				if (c.getBlockType(x + dx, y + dy, z + dz) == BlockTypes.AIR)
					c.setBlockType(x + dx, y + dy, z + dz, BlockTypes.GRAVEL);
			}

			if (r.nextBoolean())
				height--;
			dx += dir1[0];
			dz += dir1[1];
		}

		if (dir1 != dir2) {
			dx = ox;
			dz = oz;
			height = oHeight;
			while (height > 0 && dx >= 0 && dx < 8 && dz >= 0 && dz < 8) {
				for (int dy = 0; dy < height; dy++) {
					if (c.getBlockType(x + dx, y + dy, z + dz) == BlockTypes.AIR)
						c.setBlockType(x + dx, y + dy, z + dz, BlockTypes.GRAVEL);
				}

				if (r.nextBoolean())
					height--;
				dx += dir2[0];
				dz += dir2[1];
			}
		}

		return true;
	}
}
