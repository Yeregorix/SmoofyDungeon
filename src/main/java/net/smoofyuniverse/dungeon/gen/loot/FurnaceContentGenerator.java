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
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.tileentity.carrier.Furnace;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.query.QueryOperation;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.slot.OutputSlot;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.extent.Extent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class FurnaceContentGenerator {
	private static final QueryOperation<?> QUERY = QueryOperationTypes.INVENTORY_TYPE.of(OutputSlot.class);

	private List<ItemEntry> entries;
	private float outputChance;

	private FurnaceContentGenerator(List<ItemEntry> entries, float outputChance) {
		this.entries = entries;
		this.outputChance = outputChance;
	}

	public void generateBlock(Extent c, int x, int y, int z, Random r) {
		generateBlock(c, x, y, z, r, ResourceUtil.randomCardinal(r));
	}

	public void generateBlock(Extent c, int x, int y, int z, Random r, Direction dir) {
		c.setBlock(x, y, z, BlockTypes.FURNACE.getDefaultState().with(Keys.DIRECTION, dir).get());
		((Furnace) c.getTileEntity(x, y, z).get()).getInventory().query(QUERY).set(generate(r).orElse(ItemStack.empty())); // TODO fix or report
	}

	public Optional<ItemStack> generate(Random r) {
		List<ItemStack> items = new ArrayList<>();

		for (ItemEntry e : this.entries) {
			if (r.nextFloat() < e.chance)
				items.add(e.item);
		}

		if (items.isEmpty())
			return Optional.empty();

		return r.nextFloat() < this.outputChance ? Optional.of(items.get(r.nextInt(items.size())).copy()) : Optional.empty();
	}

	private static class ItemEntry {
		public final ItemStack item;
		public final float chance;

		private ItemEntry(ItemStack item, float chance) {
			this.item = item;
			this.chance = chance;
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private List<ItemEntry> entries = new ArrayList<>();
		private float outputChance;

		private Builder() {}

		public Builder add(ItemType type, int count, float chance) {
			return add(ItemStack.of(type, count), chance);
		}

		public Builder add(ItemStack item, float chance) {
			this.entries.add(new ItemEntry(item, chance));
			return this;
		}

		public FurnaceContentGenerator build(float outputChance) {
			return new FurnaceContentGenerator(this.entries, outputChance);
		}
	}
}
