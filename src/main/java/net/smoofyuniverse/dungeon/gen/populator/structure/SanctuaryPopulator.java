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

package net.smoofyuniverse.dungeon.gen.populator.structure;

import com.flowpowered.math.vector.Vector2i;
import net.smoofyuniverse.dungeon.gen.populator.api.info.RoomInfo;
import net.smoofyuniverse.dungeon.gen.populator.core.RoomPopulator;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class SanctuaryPopulator extends RoomPopulator {

	public SanctuaryPopulator() {
		super("sanctuary");
		roomChance(0.003f);
	}

	@Override
	protected Vector2i getLayers(int layersCount) {
		return new Vector2i(0, 0);
	}

	@Override
	public boolean populateRoom(RoomInfo info, World w, Extent c, Random r) {
		info.flag = true;

		int x = info.minX, z = info.minZ;
		int y = info.minY + info.floorOffset;

		for (int dx = 0; dx < 8; dx++) {
			for (int dz = 0; dz < 8; dz++)
				c.setBlockType(x + dx, y, z + dz, BlockTypes.OBSIDIAN);
		}

		y++;

		c.setBlockType(x + 2, y, z + 2, BlockTypes.GOLD_BLOCK);
		c.setBlockType(x + 3, y, z + 2, BlockTypes.NETHERRACK);
		c.setBlockType(x + 4, y, z + 2, BlockTypes.NETHERRACK);
		c.setBlockType(x + 5, y, z + 2, BlockTypes.GOLD_BLOCK);

		c.setBlockType(x + 2, y, z + 3, BlockTypes.NETHERRACK);
		c.setBlockType(x + 3, y, z + 3, BlockTypes.SOUL_SAND);
		c.setBlockType(x + 4, y, z + 3, BlockTypes.SOUL_SAND);
		c.setBlockType(x + 5, y, z + 3, BlockTypes.NETHERRACK);

		c.setBlockType(x + 2, y, z + 4, BlockTypes.GOLD_BLOCK);
		c.setBlockType(x + 3, y, z + 4, BlockTypes.NETHERRACK);
		c.setBlockType(x + 4, y, z + 4, BlockTypes.NETHERRACK);
		c.setBlockType(x + 5, y, z + 4, BlockTypes.GOLD_BLOCK);

		y++;

		c.setBlockType(x + 2, y, z + 2, BlockTypes.TORCH);
		c.setBlockType(x + 5, y, z + 2, BlockTypes.TORCH);
		c.setBlockType(x + 2, y, z + 4, BlockTypes.TORCH);
		c.setBlockType(x + 5, y, z + 4, BlockTypes.TORCH);

		return true;
	}
}
