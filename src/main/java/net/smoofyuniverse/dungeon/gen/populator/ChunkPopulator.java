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

import net.smoofyuniverse.dungeon.gen.populator.FlagManager.ChunkInfo;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityArchetype;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.util.weighted.WeightedSerializableObject;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;
import org.spongepowered.api.world.gen.Populator;
import org.spongepowered.api.world.gen.PopulatorType;
import org.spongepowered.api.world.gen.PopulatorTypes;

import java.util.Random;

import static net.smoofyuniverse.dungeon.util.MathUtil.isValidProbability;

public abstract class ChunkPopulator implements Populator {
	protected final String name;

	private float chunkChance = 1f;
	private int chunkItAttempts = 1, chunkItMax = 0;
	private float chunkItChance = 1;

	protected ChunkPopulator(String name) {
		this.name = name;
	}

	public final String getName() {
		return this.name;
	}

	protected final void chunkChance(float chance) {
		validateProbability(chance);
		this.chunkChance = chance;
	}

	protected static void validateProbability(float value) {
		if (!isValidProbability(value))
			throw new IllegalArgumentException("Invalid probability");
	}

	protected final void chunkIterations(int attempts, int max) {
		validateIterations(attempts, max);
		this.chunkItAttempts = attempts;
		this.chunkItMax = max;
	}

	@Override
	public PopulatorType getType() {
		return PopulatorTypes.GENERIC_OBJECT;
	}

	protected static void validateIterations(int attempts, int max) {
		if (attempts <= 0)
			throw new IllegalArgumentException("attempts");
		if (max != 0 && max >= attempts)
			throw new IllegalArgumentException("max");
	}

	protected final void chunkIterationChance(float chance) {
		validateProbability(chance);
		this.chunkItChance = chance;
	}

	@Override
	public final void populate(World w, Extent c, Random r) {
		ChunkInfo info = FlagManager.of(w).getChunk(c);
		if (info.getFlag())
			return;

		if (random(r, this.chunkChance)) {
			int itCount = 0;

			for (int it = 0; it < this.chunkItAttempts; it++) {
				if (this.chunkItMax != 0 && itCount >= this.chunkItMax)
					break;

				if (random(r, this.chunkItChance) && populateChunk(info, w, c, r))
					itCount++;
			}
		}
	}

	public static void generateSpawner(Extent e, int x, int y, int z, EntityType t) {
		e.setBlockType(x, y, z, BlockTypes.MOB_SPAWNER);
		e.getTileEntity(x, y, z).get().offer(Keys.SPAWNER_NEXT_ENTITY_TO_SPAWN, new WeightedSerializableObject<>(EntityArchetype.of(t), 1));
	}

	protected static boolean random(Random r, float chance) {
		if (chance == 1f)
			return true;
		if (chance == 0f)
			return false;
		if (chance == 0.5f)
			return r.nextBoolean();
		return r.nextFloat() < chance;
	}

	public boolean populateChunk(ChunkInfo info, World w, Extent c, Random r) {
		return populateChunk(w, c, r);
	}

	public boolean populateChunk(World w, Extent c, Random r) {
		throw new UnsupportedOperationException();
	}
}
