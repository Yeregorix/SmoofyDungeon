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

package net.smoofyuniverse.dungeon.gen.populator.spawner;

import net.smoofyuniverse.dungeon.gen.populator.RoomPopulator;
import net.smoofyuniverse.dungeon.util.WeightedList;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class SimpleSpawnerPopulator extends RoomPopulator {
	public static final WeightedList<EntityType> ENTITIES;

	public SimpleSpawnerPopulator() {
		super("simple_spawner");
		roomChance(0.1f, -0.003f);
	}

	@Override
	public boolean populateRoom(World w, Extent c, Random r, int layer, int room, int x, int y, int z) {
		x += r.nextInt(6) + 1;
		y += getFloorOffset(c, x, y, z) + 1;
		z += r.nextInt(6) + 1;

		if (c.getBlockType(x, y, z) == BlockTypes.AIR && c.getBlockType(x, y - 1, z) != BlockTypes.AIR) {
			generateSpawner(c, x, y, z, ENTITIES.get(r));
			return true;
		}
		return false;
	}

	static {
		ENTITIES = WeightedList.<EntityType>builder()
				.add(EntityTypes.ZOMBIE, 4)
				.add(EntityTypes.SKELETON, 2)
				.add(EntityTypes.SPIDER, 1.6)
				.add(EntityTypes.PIG_ZOMBIE, 0.8)
				.add(EntityTypes.CAVE_SPIDER, 0.4)
				.add(EntityTypes.ENDERMAN, 0.4)
				.add(EntityTypes.MAGMA_CUBE, 0.4)
				.add(EntityTypes.WITCH, 0.2)
				.add(EntityTypes.SILVERFISH, 0.2)
				.build();
	}
}
