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

package net.smoofyuniverse.dungeon.gen.loot;

import net.smoofyuniverse.dungeon.util.ResourceUtil;
import net.smoofyuniverse.dungeon.util.WeightedList;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.carrier.TileEntityCarrier;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.Carrier;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class ChestContentGenerator {
	private final WeightedList<ItemStackSnapshot> items;
	private final int[] counts;

	private ChestContentGenerator(WeightedList<ItemStackSnapshot> items, int[] counts) {
		this.items = items;
		this.counts = counts;
	}

	public void generateBlock(Extent c, int x, int y, int z, Random r) {
		generateBlock(c, x, y, z, r, ResourceUtil.randomCardinal(r));
	}

	public void generateBlock(Extent c, int x, int y, int z, Random r, Direction dir) {
		generateCarrier(c, x, y, z, r, BlockTypes.CHEST.getDefaultState().with(Keys.DIRECTION, dir).get());
	}

	public void generateCarrier(Extent c, int x, int y, int z, Random r, BlockType type) {
		generateCarrier(c, x, y, z, r, type.getDefaultState());
	}

	public void generateCarrier(Extent c, int x, int y, int z, Random r, BlockState state) {
		c.setBlock(x, y, z, state);
		fill((TileEntityCarrier) c.getTileEntity(x, y, z).get(), r);
	}

	public void fill(Carrier carrier, Random r) {
		ResourceUtil.fill(carrier.getInventory(), r, generateItems(r));
	}

	public ItemStack[] generateItems(Random r) {
		int count = this.counts[r.nextInt(this.counts.length)];

		ItemStack[] items = new ItemStack[count];
		for (int i = 0; i < count; i++)
			items[i] = this.items.get(r).createStack();

		return items;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private WeightedList.Builder<ItemStackSnapshot> items = WeightedList.builder();
		private int[] counts;

		private Builder() {}

		public Builder add(ItemType type, int count, double weight) {
			return add(ItemStack.of(type, count), weight);
		}

		public Builder add(ItemStack item, double weight) {
			return add(item.createSnapshot(), weight);
		}

		public Builder add(ItemStackSnapshot item, double weight) {
			if (item == null || item.isEmpty())
				throw new IllegalArgumentException("item");

			this.items.add(item, weight);
			return this;
		}

		public ChestContentGenerator build(int... counts) {
			return new ChestContentGenerator(this.items.build(), counts);
		}
	}
}
