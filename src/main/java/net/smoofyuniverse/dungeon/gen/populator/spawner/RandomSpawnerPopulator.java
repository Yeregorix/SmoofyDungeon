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
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class RandomSpawnerPopulator extends RoomPopulator {

	@Override
	public float getRoomIterationChance() {
		return 0.08f;
	}

	@Override
	public float getRoomIterationChanceAdditionPerLayer() {
		return -0.002f;
	}

	@Override
	public void populateRoom(World w, Extent c, Random r, int layer, int room, int x, int y, int z) {
		x += r.nextInt(6) + 1;
		y += getFloorOffset(c, x, y, z) + 1;
		z += r.nextInt(6) + 1;

		if (c.getBlockType(x, y, z) == BlockTypes.AIR && c.getBlockType(x, y - 1, z) != BlockTypes.AIR)
			generateSpawner(c, x, y, z, randomEntityType(r));
	}

	public static EntityType randomEntityType(Random r) {
		float f = r.nextFloat();
		if (f < 0.4f)
			return EntityTypes.ZOMBIE;            // p = 0.4
		if (f < 0.6f)
			return EntityTypes.SKELETON;        // p = 0.2
		if (f < 0.76f)
			return EntityTypes.SPIDER;            // p = 0.16
		if (f < 0.84f)
			return EntityTypes.PIG_ZOMBIE;        // p = 0.08
		if (f < 0.88f)
			return EntityTypes.CAVE_SPIDER;        // p = 0.04
		if (f < 0.92f)
			return EntityTypes.ENDERMAN;        // p = 0.04
		if (f < 0.96f)
			return EntityTypes.MAGMA_CUBE;        // p = 0.04
		if (f < 0.98f)
			return EntityTypes.WITCH;            // p = 0.02
		return EntityTypes.SILVERFISH;            // p = 0.02
	}
}
