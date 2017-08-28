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

import net.smoofyuniverse.dungeon.util.ResourceUtil;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public abstract class LayerPopulator extends ChunkPopulator {

	protected LayerPopulator() {}

	@Override
	public final void populateChunk(World w, Extent c, Random r) {
		for (int l = getMinimumLayer(); l <= getMaximumLayer(); l++) {
			if (hasFlag(c, l))
				continue;
			if (ResourceUtil.random(getLayerChance(), r)) {
				int itMax = getLayerIterationMax();
				int itCount = 0;
				for (int it = 0; it < getLayerIterations(); it++) {
					if (itCount >= itMax && itMax >= 0)
						break;
					if (ResourceUtil.random(getLayerIterationChance(), r)) {
						itCount++;
						populateLayer(w, c, r, l);
					}
				}
			}
		}
	}

	public int getMinimumLayer() {
		return 0;
	}

	public int getMaximumLayer() {
		return 6;
	}

	public static boolean hasFlag(Extent chunk, int layer) {
		return chunk.getBlockType(chunk.getBlockMin().add(layer + 1, 0, 0)) == BlockTypes.BARRIER;
	}

	public float getLayerChance() {
		return 1.0f;
	}

	public int getLayerIterationMax() {
		return -1;
	}

	public int getLayerIterations() {
		return 1;
	}

	public float getLayerIterationChance() {
		return 1.0f;
	}

	public void populateLayer(World w, Extent c, Random r, int layer) {
		populateLayer(w, c, r, layer, (layer * 6) + 30);
	}

	public abstract void populateLayer(World w, Extent c, Random r, int layer, int y);

	public static void setFlag(Extent chunk, int layer, boolean v, Cause c) {
		chunk.setBlockType(chunk.getBlockMin().add(layer + 1, 0, 0), v ? BlockTypes.BARRIER : BlockTypes.AIR, c);
	}
}
