/*
 * Copyright (c) 2017-2019 Hugo Dupanloup (Yeregorix)
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

package net.smoofyuniverse.dungeon.gen.offset;

import net.smoofyuniverse.dungeon.SmoofyDungeon;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.ImmutableBiomeVolume;
import org.spongepowered.api.world.extent.MutableBlockVolume;
import org.spongepowered.api.world.gen.GenerationPopulator;

import java.util.ArrayList;
import java.util.List;

public class GenerationPopulatorAdapter implements GenerationPopulator {
	public final int offsetY, minY;
	private final List<GenerationPopulator> populators = new ArrayList<>();

	public GenerationPopulatorAdapter(int offsetY, int minY) {
		this.offsetY = offsetY;
		this.minY = minY;
	}

	public List<GenerationPopulator> getPopulators() {
		return this.populators;
	}

	@Override
	public void populate(World world, MutableBlockVolume volume, ImmutableBiomeVolume biomes) {
		MutableBlockVolumeAdapter adapter = new MutableBlockVolumeAdapter(volume, this.offsetY, this.minY);

		for (GenerationPopulator p : this.populators) {
			try {
				p.populate(world, adapter, biomes);
			} catch (Exception e) {
				SmoofyDungeon.LOGGER.error("Generation populator '" + p.getClass().getName() + "' has thrown an exception", e);
			}
		}
	}
}
