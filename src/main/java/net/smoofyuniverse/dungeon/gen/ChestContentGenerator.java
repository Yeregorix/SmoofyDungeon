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

import net.smoofyuniverse.dungeon.util.RandomQueue;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.carrier.Chest;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.extent.Extent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChestContentGenerator {
	private List<ItemEntry> entries;
	private int[] counts;

	private ChestContentGenerator(List<ItemEntry> entries, int[] counts) {
		this.entries = entries;
		this.counts = counts;
	}

	public void generateBlock(Extent c, int x, int y, int z, Cause cause, Random r) {
		c.setBlockType(x, y, z, BlockTypes.CHEST, cause);
		fill(((Chest) c.getTileEntity(x, y, z).get()).getInventory(), r);
	}

	public void fill(Inventory inv, Random r) {
		fill(inv, r, generate(r));
	}

	public static void fill(Inventory inv, Random r, ItemStack... stacks) {
		List<Inventory> slots = asList(inv.slots());

		for (ItemStack stack : stacks) {
			Inventory slot;
			do {
				slot = slots.get(r.nextInt(slots.size()));
			} while (slot.peek().isPresent());
			slot.set(stack);
		}
	}

	public ItemStack[] generate(Random r) {
		List<ItemStack> items = new ArrayList<>();

		for (ItemEntry e : entries) {
			if (r.nextFloat() < e.chance)
				items.add(e.item);
		}

		if (items.isEmpty())
			return new ItemStack[0];

		int count = this.counts[r.nextInt(this.counts.length)];
		ItemStack[] result = new ItemStack[count];

		RandomQueue<ItemStack> queue = RandomQueue.of(items, r);
		for (int i = 0; i < count; i++)
			result[i] = queue.next();

		return result;
	}

	public static <T> List<T> asList(Iterable<T> it) {
		if (it instanceof List)
			return (List<T>) it;

		List<T> list = new ArrayList<>();
		for (T value : it)
			list.add(value);
		return list;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private List<ItemEntry> entries = new ArrayList<>();
		private int[] counts;

		private Builder() {}

		public Builder add(ItemType type, int count, float chance) {
			return add(ItemStack.of(type, count), chance);
		}

		public Builder add(ItemStack item, float chance) {
			this.entries.add(new ItemEntry(item, chance));
			return this;
		}

		public ChestContentGenerator build(int... counts) {
			return new ChestContentGenerator(this.entries, counts);
		}
	}

	private static class ItemEntry {
		public final ItemStack item;
		public final float chance;

		public ItemEntry(ItemStack item, float chance) {
			this.item = item;
			this.chance = chance;
		}
	}
}
