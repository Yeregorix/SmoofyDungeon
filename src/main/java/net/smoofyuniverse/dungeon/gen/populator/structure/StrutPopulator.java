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
import net.smoofyuniverse.dungeon.gen.populator.api.info.RoomInfo;
import net.smoofyuniverse.dungeon.gen.populator.core.RoomPopulator;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

import static com.flowpowered.math.GenericMath.floor;
import static java.lang.Math.max;

public class StrutPopulator extends RoomPopulator {

	public StrutPopulator() {
		super("strut");
		roomChance(0.03f, 0.05f);
	}

	@Override
	protected Vector2i getLayers(int layersCount) {
		return new Vector2i(max(floor(layersCount * 0.1), 1), layersCount - 1);
	}

	@Override
	public boolean populateRoom(RoomInfo info, World w, Extent c, Random r) {
		int x = info.minX, z = info.minZ;
		int floorY = info.minY + info.floorOffset + 1, ceilingY = info.minY + info.ceilingOffset + 5;

		if (r.nextBoolean()) {
			if (c.getBlockType(x + 2, ceilingY, z) == BlockTypes.AIR) {
				for (int dx = 1; dx < 7; dx++)
					c.setBlockType(x + dx, ceilingY, z, BlockTypes.PLANKS);

				for (int y = floorY; y < ceilingY; y++) {
					c.setBlockType(x + 1, y, z, BlockTypes.FENCE);
					c.setBlockType(x + 6, y, z, BlockTypes.FENCE);
				}

				return true;
			}
		} else {
			if (c.getBlockType(x, ceilingY, z + 2) == BlockTypes.AIR) {
				for (int dz = 1; dz < 7; dz++)
					c.setBlockType(x, ceilingY, z + dz, BlockTypes.PLANKS);

				for (int y = floorY; y < ceilingY; y++) {
					c.setBlockType(x, y, z + 1, BlockTypes.FENCE);
					c.setBlockType(x, y, z + 6, BlockTypes.FENCE);
				}

				return true;
			}
		}

		return false;
	}
}
