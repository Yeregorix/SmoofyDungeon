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

package net.smoofyuniverse.dungeon.util;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.property.PropertyHolder;
import org.spongepowered.api.data.property.block.BlastResistanceProperty;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.World;

import java.util.*;

public class ResourceUtil {
	private static final Direction[] CARDINALS = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};

	public static Direction randomCardinal(Random r) {
		return CARDINALS[r.nextInt(4)];
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

	public static <T> List<T> asList(Iterable<T> it) {
		if (it instanceof List)
			return (List<T>) it;

		List<T> list = new ArrayList<>();
		for (T value : it)
			list.add(value);
		return list;
	}

	public static Set<Vector3i> simulateExplosion(World w, Random r, Vector3d pos, float size) {
		return simulateExplosion(w, r, pos.getX(), pos.getY(), pos.getZ(), size);
	}

	public static Set<Vector3i> simulateExplosion(World w, Random r, double x, double y, double z, float size) {
		Set<Vector3i> set = new HashSet<>();

		for (int j = 0; j < 16; ++j) {
			for (int k = 0; k < 16; ++k) {
				for (int l = 0; l < 16; ++l) {
					if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
						double d0 = (double) ((float) j / 15f * 2f - 1f);
						double d1 = (double) ((float) k / 15f * 2f - 1f);
						double d2 = (double) ((float) l / 15f * 2f - 1f);
						double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
						d0 = d0 / d3;
						d1 = d1 / d3;
						d2 = d2 / d3;
						float f = size * (0.7f + r.nextFloat() * 0.6f);
						double d4 = x;
						double d6 = y;
						double d8 = z;

						while (f > 0f) {
							Vector3i pos = new Vector3i(d4, d6, d8);
							BlockState block = w.getBlock(pos);

							if (block.getType() != BlockTypes.AIR) {
								f -= (getBlastResistance(block) + 0.3f) * 0.3f;
								if (f > 0f)
									set.add(pos);
							}

							d4 += d0 * 0.30000001192092896D;
							d6 += d1 * 0.30000001192092896D;
							d8 += d2 * 0.30000001192092896D;

							f -= 0.22500001f;
						}
					}
				}
			}
		}
		return set;
	}

	public static double getBlastResistance(PropertyHolder h) {
		return h.getProperty(BlastResistanceProperty.class).map(BlastResistanceProperty::getValue).orElse(0d);
	}

	// Micro optimizations
	public static boolean random(float chance, Random r) {
		if (chance >= 1f)
			return true;
		if (chance <= 0f)
			return false;
		if (chance == 0.5f)
			return r.nextBoolean();
		return r.nextFloat() < chance;
	}
}
