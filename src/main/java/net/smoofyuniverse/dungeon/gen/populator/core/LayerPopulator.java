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

package net.smoofyuniverse.dungeon.gen.populator.core;

import com.flowpowered.math.vector.Vector2i;
import net.smoofyuniverse.dungeon.gen.populator.api.info.ChunkInfo;
import net.smoofyuniverse.dungeon.gen.populator.api.info.LayerInfo;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public abstract class LayerPopulator extends ChunkPopulator {
	private int layerItAttempts = 1, layerItMax = 0;
	private float layerChanceBase = 1, layerChanceAddition = 0;
	private float layerItChanceBase = 1, layerItChanceAddition = 0;

	protected LayerPopulator(String name) {
		super(name);
	}

	protected final void layerIterations(int attempts, int max) {
		validateIterations(attempts, max);

		this.layerItAttempts = attempts;
		this.layerItMax = max;
	}

	protected final void layerChance(float value) {
		validateProbability(value);

		this.layerChanceBase = value;
		this.layerChanceAddition = 0;
	}

	protected final void layerChance(float from, float to) {
		validateProbability(from);
		validateProbability(to);

		this.layerChanceBase = from;
		this.layerChanceAddition = to - from;
	}

	protected final void layerIterationChance(float value) {
		validateProbability(value);

		this.layerItChanceBase = value;
		this.layerItChanceAddition = 0;
	}

	protected final void layerIterationChance(float from, float to) {
		validateProbability(from);
		validateProbability(to);

		this.layerItChanceBase = from;
		this.layerItChanceAddition = to - from;
	}

	@Override
	public final boolean populateChunk(ChunkInfo info, World w, Extent c, Random r) {
		Vector2i layers = getLayers(info.layersCount);
		int minL = layers.getX(), maxL = layers.getY();
		if (minL > maxL || minL >= info.layersCount || maxL < 0)
			return false;

		boolean success = false;

		int count = maxL - minL;

		for (int l = minL; l <= maxL; l++) {
			LayerInfo layer = info.getLayer(l);
			if (layer.flag)
				continue;

			float layerFactor = count == 0 ? 0 : (l - minL) / (float) count;

			if (random(r, this.layerChanceBase + this.layerChanceAddition * layerFactor)) {
				float itChance = this.layerItChanceBase + this.layerItChanceAddition * layerFactor;
				int itCount = 0;

				for (int it = 0; it < this.layerItAttempts; it++) {
					if (this.layerItMax != 0 && itCount >= this.layerItMax)
						break;

					if (random(r, itChance) && populateLayer(layer, w, c, r, layerFactor))
						itCount++;
				}

				if (itCount != 0)
					success = true;
			}
		}

		return success;
	}

	protected Vector2i getLayers(int layersCount) {
		return new Vector2i(0, layersCount - 1); // inclusive
	}

	public abstract boolean populateLayer(LayerInfo info, World w, Extent c, Random r, float layerFactor);
}
