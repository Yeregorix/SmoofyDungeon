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

		for (int a = 0; a < 16; a++) {
			for (int b = 0; b < 16; b++) {
				for (int c = 0; c < 16; c++) {
					if (a == 0 || a == 15 || b == 0 || b == 15 || c == 0 || c == 15) {
						double dx = (double) ((float) a / 15f * 2f - 1f);
						double dy = (double) ((float) b / 15f * 2f - 1f);
						double dz = (double) ((float) c / 15f * 2f - 1f);

						double l = Math.sqrt(dx * dx + dy * dy + dz * dz);
						dx /= l;
						dy /= l;
						dz /= l;

						float f = size * (0.7f + r.nextFloat() * 0.6f);
						double cx = x;
						double cy = y;
						double cz = z;

						while (f > 0f) {
							Vector3i pos = new Vector3i(cx, cy, cz);
							BlockState block = w.getBlock(pos);

							if (block.getType() != BlockTypes.AIR) {
								f -= (getBlastResistance(block) + 0.3f) * 0.3f;
								if (f > 0f)
									set.add(pos);
							}

							cx += dx * 0.30000001192092896D;
							cy += dy * 0.30000001192092896D;
							cz += dz * 0.30000001192092896D;

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
