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

package net.smoofyuniverse.dungeon.gen.populator;

import net.smoofyuniverse.dungeon.SmoofyDungeon;
import net.smoofyuniverse.dungeon.gen.populator.FlagManager.ChunkInfo;
import net.smoofyuniverse.dungeon.util.ResourceUtil;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityArchetype;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.util.weighted.WeightedSerializableObject;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;
import org.spongepowered.api.world.gen.Populator;
import org.spongepowered.api.world.gen.PopulatorType;
import org.spongepowered.api.world.gen.PopulatorTypes;

import java.util.Random;

public abstract class ChunkPopulator implements Populator {
	protected final Cause cause;

	protected ChunkPopulator() {
		this.cause = SmoofyDungeon.get().newCause().named("Populator", this).build();
	}

	@Override
	public PopulatorType getType() {
		return PopulatorTypes.GENERIC_OBJECT;
	}

	@Override
	public final void populate(World w, Extent c, Random r) {
		ChunkInfo info = FlagManager.of(w).getChunk(c);
		if (info.getFlag())
			return;
		if (ResourceUtil.random(getChunkChance(), r)) {
			int itMax = getChunkIterationMax();
			int itCount = 0;
			for (int it = 0; it < getChunkIterations(); it++) {
				if (itCount >= itMax && itMax >= 0)
					break;
				if (ResourceUtil.random(getChunkIterationChance(), r)) {
					itCount++;
					populateChunk(info, w, c, r);
				}
			}
		}
	}

	public float getChunkChance() {
		return 1.0f;
	}

	public int getChunkIterationMax() {
		return -1;
	}

	public int getChunkIterations() {
		return 1;
	}

	public float getChunkIterationChance() {
		return 1.0f;
	}

	public void populateChunk(ChunkInfo info, World w, Extent c, Random r) {
		populateChunk(w, c, r);
	}

	public void populateChunk(World w, Extent c, Random r) {}

	public static void generateSpawner(Extent e, int x, int y, int z, EntityType t, Cause cause) {
		e.setBlockType(x, y, z, BlockTypes.MOB_SPAWNER, cause);
		e.getTileEntity(x, y, z).get().offer(Keys.SPAWNER_NEXT_ENTITY_TO_SPAWN, new WeightedSerializableObject<>(EntityArchetype.of(t), 1));
	}
}
