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

package net.smoofyuniverse.dungeon.gen.populator.structure;

import net.smoofyuniverse.dungeon.gen.populator.core.ChunkPopulator;
import net.smoofyuniverse.dungeon.gen.populator.core.info.ChunkInfo;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.biome.BiomeTypes;
import org.spongepowered.api.world.extent.Extent;
import org.spongepowered.api.world.gen.PopulatorObject;
import org.spongepowered.api.world.gen.type.BiomeTreeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OasisPopulator extends ChunkPopulator {
	public static final PopulatorObject[] TREES;

	public OasisPopulator() {
		super("oasis");
		chunkChance(0.003f);
	}

	@Override
	public boolean populateChunk(ChunkInfo info, World w, Extent c, Random r) {
		info.flag = true;

		int x = info.minX, z = info.minZ;
		int bottomY = info.bottomY, topY = info.topY;

		BiomeType biome = w.getBiome(x + 3, 0, z + 3);
		boolean useGlass = biome == BiomeTypes.OCEAN || biome == BiomeTypes.DEEP_OCEAN || biome == BiomeTypes.FROZEN_OCEAN;

		for (int dx = 0; dx < 16; dx++) {
			for (int dz = 0; dz < 16; dz++) {
				c.setBlockType(x + dx, bottomY - 1, z + dz, r.nextFloat() < 0.1f ? BlockTypes.CLAY : BlockTypes.DIRT);
				c.setBlockType(x + dx, bottomY, z + dz, BlockTypes.GRASS);
				c.setBlockType(x + dx, bottomY + 1, z + dz, r.nextFloat() < 0.1f ? BlockTypes.TALLGRASS : BlockTypes.AIR);

				for (int y = bottomY + 2; y <= topY; y++)
					c.setBlockType(x + dx, y, z + dz, BlockTypes.AIR);

				if (useGlass)
					c.setBlockType(x + dx, topY + 1, z + dz, BlockTypes.GLASS);
				else {
					int maxY = w.getHighestYAt(x + dx, z + dz) + 10;
					for (int y = topY + 1; y <= maxY; y++)
						c.setBlockType(x + dx, y, z + dz, BlockTypes.AIR);
				}
			}
		}

		if (!useGlass) {
			for (int i = 0; i <= 15; i++) {
				c.setBlockType(x + i, topY, z, BlockTypes.STONEBRICK);
				c.setBlockType(x + i, topY, z + 15, BlockTypes.STONEBRICK);

				c.setBlockType(x, topY, z + i, BlockTypes.STONEBRICK);
				c.setBlockType(x + 15, topY, z + i, BlockTypes.STONEBRICK);
			}

			for (int i = 1; i <= 14; i++) {
				c.setBlockType(x + i, topY + 1, z + 1, BlockTypes.STONEBRICK);
				c.setBlockType(x + i, topY + 1, z + 14, BlockTypes.STONEBRICK);

				c.setBlockType(x + 1, topY + 1, z + i, BlockTypes.STONEBRICK);
				c.setBlockType(x + 14, topY + 1, z + i, BlockTypes.STONEBRICK);
			}
		}

		for (int i = 4; i <= 11; i++) {
			c.setBlockType(x + i, bottomY, z + 4, BlockTypes.WATER);
			c.setBlockType(x + i, bottomY, z + 11, BlockTypes.WATER);

			c.setBlockType(x + 4, bottomY, z + i, BlockTypes.WATER);
			c.setBlockType(x + 11, bottomY, z + i, BlockTypes.WATER);
		}

		c.setBlockType(x + 5, bottomY + 1, z + 5, BlockTypes.REEDS);
		c.setBlockType(x + 5, bottomY + 1, z + 10, BlockTypes.REEDS);
		c.setBlockType(x + 10, bottomY + 1, z + 5, BlockTypes.REEDS);
		c.setBlockType(x + 10, bottomY + 1, z + 10, BlockTypes.REEDS);

		TREES[r.nextInt(TREES.length)].placeObject(w, r, x + 7, bottomY + 1, z + 7);

		return true;
	}

	static {
		List<PopulatorObject> trees = new ArrayList<>();
		for (BiomeTreeType type : Sponge.getRegistry().getAllOf(BiomeTreeType.class)) {
			trees.add(type.getPopulatorObject());
			type.getLargePopulatorObject().ifPresent(trees::add);
		}
		TREES = trees.toArray(new PopulatorObject[0]);
	}
}
