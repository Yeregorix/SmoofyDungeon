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

package net.smoofyuniverse.dungeon.gen.populator.core;

import net.smoofyuniverse.dungeon.gen.populator.ChunkInfo;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

import static net.smoofyuniverse.dungeon.util.MathUtil.isValidProbability;

public abstract class LayerPopulator extends ChunkPopulator {
	private int minLayer = 0, maxLayer = 6;
	private float layerChance = 1f, layerChanceAPL = 0f;
	private int layerItAttempts = 1, layerItMax = 0;
	private float layerItChance = 1, layerItChanceAPL = 0f;

	protected LayerPopulator(String name) {
		super(name);
	}

	protected final void layers(int min, int max) {
		if (min > max)
			throw new IllegalArgumentException();
		if (min < 0)
			throw new IllegalArgumentException("min");
		if (max > 6)
			throw new IllegalArgumentException("max");
		this.minLayer = min;
		this.maxLayer = max;
	}

	protected final void layerChance(float chance, float APL) {
		validateProbability(chance, APL);
		this.layerChance = chance;
		this.layerChanceAPL = APL;
	}

	protected static void validateProbability(float value, float APL) {
		if (!isValidProbability(value))
			throw new IllegalArgumentException("Invalid probability");
		if (!isValidProbability(value + 6 * APL))
			throw new IllegalArgumentException("Invalid probability");
	}

	protected final void layerIterations(int attempts, int max) {
		validateIterations(attempts, max);
		this.layerItAttempts = attempts;
		this.layerItMax = max;
	}

	protected final void layerIterationChance(float chance, float APL) {
		validateProbability(chance, APL);
		this.layerItChance = chance;
		this.layerItChanceAPL = APL;
	}

	@Override
	public final boolean populateChunk(ChunkInfo info, World w, Extent c, Random r) {
		boolean success = false;

		for (int l = this.minLayer; l <= this.maxLayer; l++) {
			if (info.getFlag(l))
				continue;

			if (random(r, this.layerChance + l * this.layerChanceAPL)) {
				float itChance = this.layerItChance + l * this.layerItChanceAPL;
				int itCount = 0;

				for (int it = 0; it < this.layerItAttempts; it++) {
					if (this.layerItMax != 0 && itCount >= this.layerItMax)
						break;

					if (random(r, itChance) && populateLayer(info, w, c, r, l, l * 6 + 30))
						itCount++;
				}

				if (itCount != 0)
					success = true;
			}
		}

		return success;
	}

	public boolean populateLayer(ChunkInfo info, World w, Extent c, Random r, int layer, int y) {
		return populateLayer(w, c, r, layer, y);
	}

	public boolean populateLayer(World w, Extent c, Random r, int layer, int y) {
		throw new UnsupportedOperationException();
	}
}
