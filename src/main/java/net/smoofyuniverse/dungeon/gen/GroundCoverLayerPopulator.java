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
import com.flowpowered.noise.module.source.Perlin;
import net.smoofyuniverse.dungeon.SmoofyDungeon;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.property.block.MatterProperty;
import org.spongepowered.api.data.property.block.MatterProperty.Matter;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.biome.GroundCoverLayer;
import org.spongepowered.api.world.extent.ImmutableBiomeVolume;
import org.spongepowered.api.world.extent.MutableBlockVolume;
import org.spongepowered.api.world.gen.GenerationPopulator;

import java.util.*;

public class GroundCoverLayerPopulator implements GenerationPopulator {
	private final Map<BiomeType, List<GroundCoverLayer>> biomeLayers = new HashMap<>();

	public List<GroundCoverLayer> getBiomeLayers(BiomeType biomeType) {
		List<GroundCoverLayer> l = this.biomeLayers.get(biomeType);
		if (l == null) {
			l = new ArrayList<>();
			this.biomeLayers.put(biomeType, l);
		}
		return l;
	}

	@Override
	public void populate(World world, MutableBlockVolume volume, ImmutableBiomeVolume biomes) {
		SmoofyDungeon.validateChunkSize(volume);

		Vector3i min = biomes.getBiomeMin();
		int minX = min.getX(), minZ = min.getZ();
		Random r = new Random((minX >> 4) * 341873128712L + (minZ >> 4) * 132897987541L);

		Perlin perlin = new Perlin();
		perlin.setSeed(Long.hashCode(world.getProperties().getSeed()));
		perlin.setOctaveCount(4);
		perlin.setFrequency(0.03125D * 2D);
		perlin.setLacunarity(0.5);
		perlin.setPersistence(2);

		for (int dx = 0; dx < 16; dx++) {
			for (int dz = 0; dz < 16; dz++) {
				BiomeType biomeType = biomes.getBiome(minX + dx, 0, minZ + dz);
				List<GroundCoverLayer> groundCover = this.biomeLayers.get(biomeType);
				if (groundCover == null)
					continue;
				generateBiomeTerrain(world, r, volume, minX + dx, minZ + dz, perlin.getValue(minX + dx, 0, minZ + dz) * 0.55D, groundCover);
			}
		}
	}

	public void generateBiomeTerrain(World world, Random r, MutableBlockVolume volume, int x, int z, double stoneNoise, List<GroundCoverLayer> groundCover) {
		if (groundCover.isEmpty())
			return;

		int seaLevel = world.getSeaLevel();

		BlockState currentPlacement = null;
		int layerProgress = -1;
		int layerDepth = 0;

		for (int currentY = 255; currentY >= 0; --currentY) {
			BlockState nextBlock = volume.getBlock(x, currentY, z);

			if (nextBlock.getProperty(MatterProperty.class).get().getValue() == Matter.GAS) { // Equivalent to nms : nextBlock.getMaterial() == Material.AIR
				layerProgress = -1;
			} else if (nextBlock.getType() == BlockTypes.STONE) {
				if (layerProgress == -1) {
					if (groundCover.isEmpty()) {
						layerProgress = 0;
						continue;
					}

					layerDepth = 0;
					GroundCoverLayer layer = groundCover.get(layerDepth);
					currentPlacement = layer.getBlockState().apply(stoneNoise);

					layerProgress = layer.getDepth().getFlooredAmount(r, stoneNoise);
					if (layerProgress <= 0) {
						continue;
					}

					if (currentY >= seaLevel - 1) {
						volume.setBlock(x, currentY, z, currentPlacement);
						layerDepth++;
						if (layerDepth < groundCover.size()) {
							layer = groundCover.get(layerDepth);
							layerProgress = layer.getDepth().getFlooredAmount(r, stoneNoise);
							currentPlacement = layer.getBlockState().apply(stoneNoise);
						}
					} else if (currentY < seaLevel - 7 - layerProgress) {
						layerProgress = 0;
						volume.setBlockType(x, currentY, z, BlockTypes.GRAVEL);
					} else {
						layerDepth++;
						if (layerDepth < groundCover.size()) {
							layer = groundCover.get(layerDepth);
							layerProgress = layer.getDepth().getFlooredAmount(r, stoneNoise);
							currentPlacement = layer.getBlockState().apply(stoneNoise);
							volume.setBlock(x, currentY, z, currentPlacement);
						}
					}
				} else if (layerProgress > 0) {
					layerProgress--;
					volume.setBlock(x, currentY, z, currentPlacement);
					if (layerProgress == 0) {
						layerDepth++;
						if (layerDepth < groundCover.size()) {
							GroundCoverLayer layer = groundCover.get(layerDepth);
							layerProgress = layer.getDepth().getFlooredAmount(r, stoneNoise);
							currentPlacement = layer.getBlockState().apply(stoneNoise);
						}
					}
				}
			}
		}
	}
}
