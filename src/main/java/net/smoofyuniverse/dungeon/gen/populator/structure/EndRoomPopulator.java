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

package net.smoofyuniverse.dungeon.gen.populator.structure;

import com.flowpowered.math.vector.Vector3i;
import net.smoofyuniverse.dungeon.gen.populator.api.info.RoomInfo;
import net.smoofyuniverse.dungeon.gen.populator.core.RoomPopulator;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;
import static net.smoofyuniverse.dungeon.util.MathUtil.lengthSquared;
import static net.smoofyuniverse.dungeon.util.MathUtil.squared;

public class EndRoomPopulator extends RoomPopulator {

	public EndRoomPopulator() {
		super("end_room");
		roomChance(0.001f);
	}

	@Override
	public boolean populateRoom(RoomInfo info, World w, Extent c, Random r) {
		info.flag = true;

		int x = info.minX, z = info.minZ;
		int y = info.minY + info.floorOffset;

		for (int dx = 0; dx < 8; dx++) {
			for (int dz = 0; dz < 8; dz++) {
				BlockType type = c.getBlockType(x + dx, y, z + dz);
				if (type == BlockTypes.COBBLESTONE && lengthSquared(3.5 - dx, 3.5 - dz) < squared(r.nextFloat() * 8))
					c.setBlockType(x + dx, y, z + dz, BlockTypes.END_STONE);
			}
		}

		y++;
		for (int i = 0; i < 8; i++) {
			if (c.getBlockType(x + i, y, z) == BlockTypes.STONEBRICK && abs(i - 4) < (r.nextFloat() * 4))
				c.setBlockType(x + i, y, z, BlockTypes.END_BRICKS);

			if (c.getBlockType(x + i, y, z + 7) == BlockTypes.STONEBRICK && abs(i - 4) < (r.nextFloat() * 4))
				c.setBlockType(x + i, y, z + 7, BlockTypes.END_BRICKS);

			if (c.getBlockType(x, y, z + i) == BlockTypes.STONEBRICK && abs(i - 4) < (r.nextFloat() * 4))
				c.setBlockType(x, y, z + i, BlockTypes.END_BRICKS);

			if (c.getBlockType(x + 7, y, z + i) == BlockTypes.STONEBRICK && abs(i - 4) < (r.nextFloat() * 4))
				c.setBlockType(x + 7, y, z + i, BlockTypes.END_BRICKS);
		}

		int count = r.nextBoolean() ? 2 : 3;
		List<Entity> entities = new ArrayList<>(count);
		for (int i = 0; i < count; i++)
			entities.add(c.createEntity(EntityTypes.SHULKER, new Vector3i(x + 1 + r.nextInt(6), y, z + 1 + r.nextInt(6))));
		c.spawnEntities(entities);

		return true;
	}
}
