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

package net.smoofyuniverse.dungeon.gen;

import com.flowpowered.math.vector.Vector3i;
import net.smoofyuniverse.dungeon.noise.SimplexOctaveGenerator;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.biome.BiomeTypes;
import org.spongepowered.api.world.extent.ImmutableBiomeVolume;
import org.spongepowered.api.world.extent.MutableBlockVolume;
import org.spongepowered.api.world.gen.GenerationPopulator;

import java.util.Random;

public class DungeonTerrainGenerator implements GenerationPopulator {

	@Override
	public void populate(World w, MutableBlockVolume volume, ImmutableBiomeVolume biomes) {
		Vector3i min = volume.getBlockMin(), max = volume.getBlockMax();
		int minX = min.getX(), minZ = min.getZ(), maxX = max.getX(), maxZ = max.getZ();
		long seed = w.getProperties().getSeed();

		// Load random from seed and chunk coords
		Random r = new Random(seed);
		long i1 = r.nextLong() / 2L * 2L + 1L;
		long j1 = r.nextLong() / 2L * 2L + 1L;
		r.setSeed((minX >> 4) * i1 + (minZ >> 4) * j1 ^ seed);

		// Fill the floor with bedrock and stone
		for (int x = minX; x <= maxX; x++)
			for (int z = minZ; z <= maxZ; z++) {
				int bedrockLevel = r.nextInt(5);
				for (int y = 0; y <= 30; y++)
					volume.setBlockType(x, y, z, y <= bedrockLevel ? BlockTypes.BEDROCK : BlockTypes.STONE);
			}

		// Iterate over each layer
		for (int y = 30; y < 72; y += 6) {
			// Iterate over each room
			for (int x = 0; x < 16; x += 8)
				for (int z = 0; z < 16; z += 8) {
					int randX = (r.nextInt(3) - 1) * (x + 7);
					int randZ = (r.nextInt(3) - 1) * (z + 7);
					int floorY = r.nextInt(2) + y;

					for (int x2 = x; x2 < x + 8; x2++)
						for (int z2 = z; z2 < z + 8; z2++) {
							// Fill the floor
							volume.setBlockType(minX + x2, floorY, minZ + z2, BlockTypes.COBBLESTONE);

							for (int y2 = floorY; y2 < y + 8; y2++) {
								// Fill the walls
								if (((x2 == x || x2 == x + 7) && (z2 == z || z2 == z + 7)) || randX == x2 || randZ == z2)
									volume.setBlockType(minX + x2, y2, minZ + z2, BlockTypes.STONEBRICK);
								else if (y2 != floorY)
									volume.setBlockType(minX + x2, y2, minZ + z2, BlockTypes.AIR);
							}
						}
				}
		}

		// Create the noise generator which generates wave forms to use for the surface
		SimplexOctaveGenerator octave = new SimplexOctaveGenerator(seed, 8);
		octave.setScale(1d / 48d);

		// Generate the ceiling and the grass land
		for (int x = minX; x <= maxX; x++)
			for (int z = minZ; z <= maxZ; z++) {
				double noise = octave.noise(x, z, 0.5d, 0.5d);
				int stoneLevel = (int) (noise * 3d) + 76;
				int groundLevel = (int) (noise * 4d) + 80;

				volume.setBlockType(x, 72, z, BlockTypes.COBBLESTONE);

				for (int y = 73; y < stoneLevel; y++)
					volume.setBlockType(x, y, z, BlockTypes.STONE);

				BiomeType biome = biomes.getBiome(x, 0, z);
				if (biome == BiomeTypes.DESERT || biome == BiomeTypes.DESERT_HILLS || biome == BiomeTypes.DESERT_MOUNTAINS) {
					for (int y = stoneLevel; y <= groundLevel; y++)
						volume.setBlockType(x, y, z, BlockTypes.SAND);
				} else if (biome == BiomeTypes.MUSHROOM_ISLAND) {
					for (int y = stoneLevel; y < groundLevel; y++)
						volume.setBlockType(x, y, z, BlockTypes.DIRT);
					volume.setBlockType(x, groundLevel, z, BlockTypes.MYCELIUM);
				} else {
					for (int y = stoneLevel; y < groundLevel; y++)
						volume.setBlockType(x, y, z, BlockTypes.DIRT);
					volume.setBlockType(x, groundLevel, z, BlockTypes.GRASS);
				}
			}
	}
}
