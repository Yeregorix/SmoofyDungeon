/*
 * Copyright (c) 2017-2019 Hugo Dupanloup (Yeregorix)
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

import net.smoofyuniverse.dungeon.util.random.WeightedList;
import org.spongepowered.api.entity.Equipable;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.equipment.EquipmentType;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class EntityEquipmentGenerator {
	private final Map<EquipmentType, WeightedList<ItemStackSnapshot>> map;

	private EntityEquipmentGenerator(Map<EquipmentType, WeightedList<ItemStackSnapshot>> map) {
		this.map = map;
	}

	public void fill(Equipable equipable, Random r) {
		generateItems(r).forEach(equipable::equip);
	}

	public Map<EquipmentType, ItemStack> generateItems(Random r) {
		Map<EquipmentType, ItemStack> items = new HashMap<>();

		for (Entry<EquipmentType, WeightedList<ItemStackSnapshot>> e : this.map.entrySet()) {
			ItemStackSnapshot item = e.getValue().get(r);
			if (!item.isEmpty())
				items.put(e.getKey(), item.createStack());
		}

		return items;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private final Map<EquipmentType, WeightedList.Builder<ItemStackSnapshot>> map = new HashMap<>();

		private Builder() {}

		public Builder reset() {
			this.map.clear();
			return this;
		}

		public Builder add(EquipmentType type, ItemType itemType, double weight) {
			return add(type, itemType, 1, weight);
		}

		public Builder add(EquipmentType type, ItemType itemType, int count, double weight) {
			return add(type, ItemStack.of(itemType, count), weight);
		}

		public Builder add(EquipmentType type, ItemStack item, double weight) {
			return add(type, item.createSnapshot(), weight);
		}

		public Builder add(EquipmentType type, ItemStackSnapshot item, double weight) {
			if (type == null)
				throw new IllegalArgumentException("type");
			if (item == null)
				throw new IllegalArgumentException("item");

			WeightedList.Builder<ItemStackSnapshot> b = this.map.get(type);
			if (b == null) {
				b = WeightedList.builder();
				this.map.put(type, b);
			}

			b.add(item, weight);
			return this;
		}

		public Builder addNone(EquipmentType type, double weight) {
			return add(type, ItemStackSnapshot.NONE, weight);
		}

		public EntityEquipmentGenerator build() {
			Map<EquipmentType, WeightedList<ItemStackSnapshot>> map = new HashMap<>();
			for (Entry<EquipmentType, WeightedList.Builder<ItemStackSnapshot>> e : this.map.entrySet())
				map.put(e.getKey(), e.getValue().build());

			return new EntityEquipmentGenerator(map);
		}
	}
}
