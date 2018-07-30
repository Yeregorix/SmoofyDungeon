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

import com.flowpowered.math.vector.Vector3i;
import net.smoofyuniverse.dungeon.SmoofyDungeon;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.extent.Extent;
import org.spongepowered.api.world.extent.ImmutableBiomeVolume;
import org.spongepowered.api.world.gen.Populator;
import org.spongepowered.api.world.gen.PopulatorType;
import org.spongepowered.api.world.gen.PopulatorTypes;

import java.util.*;

public class DungeonParentPopulator implements Populator {
	private final List<DungeonPopulator> globalPopulators = new ArrayList<>();
	private final Map<BiomeType, List<DungeonPopulator>> biomePopulators = new HashMap<>();

	public List<DungeonPopulator> getGlobalPopulators() {
		return this.globalPopulators;
	}

	public List<DungeonPopulator> getBiomePopulators(BiomeType biomeType) {
		List<DungeonPopulator> l = this.biomePopulators.get(biomeType);
		if (l == null) {
			l = new ArrayList<>();
			this.biomePopulators.put(biomeType, l);
		}
		return l;
	}

	@Override
	public PopulatorType getType() {
		return PopulatorTypes.GENERIC_OBJECT;
	}

	@Override
	public void populate(World world, Extent volume, Random random) {
		if (!volume.getBlockSize().equals(SmoofyDungeon.CHUNK_SIZE))
			throw new UnsupportedOperationException();

		Vector3i min = volume.getBlockMin();
		ChunkInfo info = new ChunkInfo(min.getX() >> 4, min.getZ() >> 4);

		for (DungeonPopulator p : this.globalPopulators) {
			try {
				p.populate(info, world, volume, random);
			} catch (Exception e) {
				SmoofyDungeon.LOGGER.error("Global populator '" + p.getName() + "' has thrown an exception", e);
			}
		}
	}

	@Override
	public void populate(World world, Extent volume, Random random, ImmutableBiomeVolume biomes) {
		if (!volume.getBlockSize().equals(SmoofyDungeon.CHUNK_SIZE))
			throw new UnsupportedOperationException();

		Vector3i min = volume.getBlockMin(), max = volume.getBlockMax();
		int minX = min.getX(), minZ = min.getZ();
		int maxX = max.getX(), maxZ = max.getZ();

		List<BiomeType> biomeTypes = new ArrayList<>();
		for (int x = minX; x <= maxX; x++) {
			for (int z = minZ; z <= maxZ; z++) {
				BiomeType biome = world.getBiome(x, 0, z);
				if (!biomeTypes.contains(biome))
					biomeTypes.add(biome);
			}
		}

		ChunkInfo info = new ChunkInfo(minX >> 4, minZ >> 4);

		for (DungeonPopulator p : this.globalPopulators) {
			try {
				p.populate(info, world, volume, random, biomes);
			} catch (Exception e) {
				SmoofyDungeon.LOGGER.error("Global populator '" + p.getName() + "' has thrown an exception", e);
			}
		}

		for (BiomeType biomeType : biomeTypes) {
			List<DungeonPopulator> l = this.biomePopulators.get(biomeType);
			if (l != null) {
				for (DungeonPopulator p : l) {
					try {
						p.populate(info, world, volume, random, biomes);
					} catch (Exception e) {
						SmoofyDungeon.LOGGER.error("Biome populator '" + p.getName() + "' has thrown an exception", e);
					}
				}
			}
		}
	}
}
