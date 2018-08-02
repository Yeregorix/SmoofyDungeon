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
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.util.DiscreteTransform3;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.*;
import org.spongepowered.api.world.extent.worker.MutableBlockVolumeWorker;
import org.spongepowered.api.world.gen.GenerationPopulator;

import java.util.Random;

public class DungeonTerrainGenerator implements GenerationPopulator {
	public final int bottomY, layersCount, generatorMinY;
	private final GenerationPopulator baseGenerator;

	public DungeonTerrainGenerator(GenerationPopulator baseGenerator, int bottomY, int layersCount, int generatorMinY) {
		if (baseGenerator == null)
			throw new IllegalArgumentException("baseGenerator");
		if (bottomY <= 0 || bottomY > 30)
			throw new IllegalArgumentException("bottomY");
		if (layersCount <= 0 || layersCount > 30)
			throw new IllegalArgumentException("layersCount");
		if (generatorMinY < 0)
			throw new IllegalArgumentException("generatorMinY");

		this.baseGenerator = baseGenerator;
		this.bottomY = bottomY;
		this.layersCount = layersCount;
		this.generatorMinY = generatorMinY;
	}

	@Override
	public void populate(World w, MutableBlockVolume volume, ImmutableBiomeVolume biomes) {
		Vector3i min = volume.getBlockMin(), max = volume.getBlockMax();
		int minX = min.getX(), minZ = min.getZ(), maxX = max.getX(), maxZ = max.getZ();
		long seed = w.getProperties().getSeed();

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
				int bedrockLevel = r.nextInt(5);
				for (int y = 0; y <= this.bottomY; y++)
					volume.setBlockType(x, y, z, y <= bedrockLevel ? BlockTypes.BEDROCK : BlockTypes.STONE);
			}
		}

		// Iterate over each layer
		for (int l = 0; l < this.layersCount; l++) {
			int y = this.bottomY + l * 6;
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

		int topY = this.bottomY + this.layersCount * 6;

		// Close the roof
		for (int x = minX; x <= maxX; x++) {
			for (int z = minZ; z <= maxZ; z++) {
				volume.setBlockType(x, topY, z, BlockTypes.COBBLESTONE);
				volume.setBlockType(x, topY + 1, z, BlockTypes.STONE);
			}
		}

		// Generate the surface
		this.baseGenerator.populate(w, new BlockVolumeAdapter(volume, topY + 2 - this.generatorMinY, this.generatorMinY), biomes);
	}

	private static class BlockVolumeAdapter implements MutableBlockVolume {
		private final MutableBlockVolume delegate;
		private final int offsetY, minY, maxY;

		public BlockVolumeAdapter(MutableBlockVolume delegate, int offsetY, int minY) {
			this.delegate = delegate;
			this.offsetY = offsetY;
			this.minY = minY;
			this.maxY = this.delegate.getBlockMax().getY() - offsetY;
		}

		@Override
		public boolean setBlock(int x, int y, int z, BlockState block) {
			if (y < this.minY || y > this.maxY)
				return false;
			return this.delegate.setBlock(x, y + this.offsetY, z, block);
		}

		@Override
		public MutableBlockVolume getBlockView(Vector3i newMin, Vector3i newMax) {
			throw new UnsupportedOperationException();
		}

		@Override
		public MutableBlockVolume getBlockView(DiscreteTransform3 transform) {
			throw new UnsupportedOperationException();
		}

		@Override
		public MutableBlockVolumeWorker<? extends MutableBlockVolume> getBlockWorker() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Vector3i getBlockMin() {
			return this.delegate.getBlockMin();
		}

		@Override
		public Vector3i getBlockMax() {
			return this.delegate.getBlockMax();
		}

		@Override
		public Vector3i getBlockSize() {
			return this.delegate.getBlockSize();
		}

		@Override
		public boolean containsBlock(int x, int y, int z) {
			return this.delegate.containsBlock(x, y, z);
		}

		@Override
		public BlockState getBlock(int x, int y, int z) {
			if (y < this.minY || y > this.maxY)
				return BlockTypes.AIR.getDefaultState();
			return this.delegate.getBlock(x, y + this.offsetY, z);
		}

		@Override
		public BlockType getBlockType(int x, int y, int z) {
			if (y < this.minY || y > this.maxY)
				return BlockTypes.AIR;
			return this.delegate.getBlockType(x, y + this.offsetY, z);
		}

		@Override
		public UnmodifiableBlockVolume getUnmodifiableBlockView() {
			throw new UnsupportedOperationException();
		}

		@Override
		public MutableBlockVolume getBlockCopy(StorageType type) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ImmutableBlockVolume getImmutableBlockCopy() {
			throw new UnsupportedOperationException();
		}
	}
}
