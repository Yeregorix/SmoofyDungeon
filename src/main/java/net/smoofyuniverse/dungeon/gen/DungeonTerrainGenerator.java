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

package net.smoofyuniverse.dungeon.gen;

import com.flowpowered.math.vector.Vector3i;
import net.smoofyuniverse.dungeon.SmoofyDungeon;
import net.smoofyuniverse.dungeon.gen.offset.MutableBlockVolumeAdapter;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.ImmutableBiomeVolume;
import org.spongepowered.api.world.extent.MutableBlockVolume;
import org.spongepowered.api.world.gen.GenerationPopulator;

import java.util.Random;

public class DungeonTerrainGenerator implements GenerationPopulator {
	public final GenerationPopulator surfaceGenerator;
	public final int surfaceOffsetY, surfaceMinY, layersCount;

	public DungeonTerrainGenerator(GenerationPopulator surfaceGenerator, int surfaceMinY, int layersCount) {
		if (surfaceGenerator == null)
			throw new IllegalArgumentException("surfaceGenerator");
		if (surfaceMinY < 0)
			throw new IllegalArgumentException("surfaceMinY");
		if (layersCount <= 0 || layersCount > 30)
			throw new IllegalArgumentException("layersCount");

		this.surfaceGenerator = surfaceGenerator;
		this.surfaceOffsetY = layersCount * 6 - surfaceMinY + 5;
		this.surfaceMinY = surfaceMinY;
		this.layersCount = layersCount;
	}

	@Override
	public void populate(World world, MutableBlockVolume volume, ImmutableBiomeVolume biomes) {
		SmoofyDungeon.validateChunkSize(volume);

		Vector3i min = volume.getBlockMin(), max = volume.getBlockMax();
		int minX = min.getX(), minZ = min.getZ(), maxX = max.getX(), maxZ = max.getZ();
		long seed = world.getProperties().getSeed();

		// Load random from seed and chunk coords
		Random r = new Random(seed);
		long a = r.nextLong(), b = r.nextLong();
		if (a % 2 == 0)
			a++;
		if (b % 2 == 0)
			b++;
		r.setSeed((minX >> 4) * a + (minZ >> 4) * b ^ seed);

		// Fill the floor with bedrock and stone
		for (int x = minX; x <= maxX; x++) {
			for (int z = minZ; z <= maxZ; z++) {
				int bedrockLevel = r.nextInt(3);
				for (int y = 0; y <= 4; y++)
					volume.setBlockType(x, y, z, y <= bedrockLevel ? BlockTypes.BEDROCK : BlockTypes.STONE);
			}
		}

		// Iterate over each layer
		for (int l = 0; l < this.layersCount; l++) {
			int y = 4 + l * 6;
			// Iterate over each room
			for (int x = 0; x < 16; x += 8) {
				for (int z = 0; z < 16; z += 8) {
					int floorOffset = r.nextBoolean() ? 0 : 1;
					int randX = (r.nextInt(3) - 1) * (x + 7), randZ = (r.nextInt(3) - 1) * (z + 7);

					for (int dx = 0; dx < 8; dx++) {
						for (int dz = 0; dz < 8; dz++) {
							// Fill the floor
							volume.setBlockType(minX + x + dx, y + floorOffset, minZ + z + dz, BlockTypes.COBBLESTONE);

							if (((dx == 0 || dx == 7) && (dz == 0 || dz == 7)) || randX == x + dx || randZ == z + dz) {
								// Fill the walls
								for (int dy = floorOffset; dy < 7; dy++)
									volume.setBlockType(minX + x + dx, y + dy, minZ + z + dz, BlockTypes.STONEBRICK);
							}
						}
					}
				}
			}
		}

		// Close the roof
		int topY = 4 + this.layersCount * 6;
		for (int x = minX; x <= maxX; x++) {
			for (int z = minZ; z <= maxZ; z++) {
				volume.setBlockType(x, topY, z, BlockTypes.COBBLESTONE);
				volume.setBlockType(x, topY + 1, z, BlockTypes.STONE);
			}
		}

		// Generate the surface
		this.surfaceGenerator.populate(world, new MutableBlockVolumeAdapter(volume, this.surfaceOffsetY, this.surfaceMinY + 1), biomes);
	}
}
