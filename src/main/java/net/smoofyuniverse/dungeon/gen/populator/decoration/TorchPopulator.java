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
import net.smoofyuniverse.dungeon.gen.populator.core.RoomPopulator;
import net.smoofyuniverse.dungeon.gen.populator.core.info.RoomInfo;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

import static com.flowpowered.math.GenericMath.floor;
import static java.lang.Math.max;

public class TorchPopulator extends RoomPopulator {

	public TorchPopulator() {
		super("torch");
		roomChance(0.1f, 0.25f);
	}

	@Override
	protected Vector2i getLayers(int layersCount) {
		return new Vector2i(max(floor(layersCount * 0.1), 1), layersCount - 1);
	}

	@Override
	public boolean populateRoom(RoomInfo info, World w, Extent c, Random r) {
		int x = info.minX + r.nextInt(6) + 1, z = info.minZ + r.nextInt(6) + 1;
		int y = info.minY + info.floorOffset;

		if (c.getBlockType(x, y, z) == BlockTypes.AIR && c.getBlockType(x, y - 1, z) != BlockTypes.AIR) {
			c.setBlockType(x, y, z, BlockTypes.TORCH);
			return true;
		}
		return false;
	}
}
