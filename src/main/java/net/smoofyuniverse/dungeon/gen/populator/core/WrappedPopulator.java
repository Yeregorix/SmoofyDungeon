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

package net.smoofyuniverse.dungeon.gen.populator.core;

import net.smoofyuniverse.dungeon.gen.populator.api.DungeonPopulator;
import net.smoofyuniverse.dungeon.gen.populator.api.info.ChunkInfo;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;
import org.spongepowered.api.world.extent.ImmutableBiomeVolume;
import org.spongepowered.api.world.gen.Populator;

import java.util.Random;

public class WrappedPopulator implements DungeonPopulator {
	private final Populator delegate;
	private final String name;

	public WrappedPopulator(String name, Populator delegate) {
		this.name = name;
		this.delegate = delegate;
	}

	public Populator getDelegate() {
		return this.delegate;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void populate(ChunkInfo info, World w, Extent c, Random r, ImmutableBiomeVolume biomes) {
		if (!info.flag)
			this.delegate.populate(w, c, r, biomes);
	}

	@Override
	public void populate(ChunkInfo info, World w, Extent c, Random r) {
		if (!info.flag)
			this.delegate.populate(w, c, r);
	}
}
