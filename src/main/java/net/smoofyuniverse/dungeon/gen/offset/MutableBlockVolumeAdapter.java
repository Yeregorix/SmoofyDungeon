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

package net.smoofyuniverse.dungeon.gen.offset;

import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.util.DiscreteTransform3;
import org.spongepowered.api.world.extent.ImmutableBlockVolume;
import org.spongepowered.api.world.extent.MutableBlockVolume;
import org.spongepowered.api.world.extent.StorageType;
import org.spongepowered.api.world.extent.UnmodifiableBlockVolume;
import org.spongepowered.api.world.extent.worker.MutableBlockVolumeWorker;

public class MutableBlockVolumeAdapter implements MutableBlockVolume {
	public final MutableBlockVolume delegate;
	public final int offsetY, minY, maxY;

	public MutableBlockVolumeAdapter(MutableBlockVolume delegate, int offsetY, int minY) {
		if (delegate == null)
			throw new IllegalArgumentException("delegate");

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
