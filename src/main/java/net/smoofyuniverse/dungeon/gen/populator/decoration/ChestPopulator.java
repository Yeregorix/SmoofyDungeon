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

package net.smoofyuniverse.dungeon.gen.populator.decoration;

import net.smoofyuniverse.dungeon.gen.loot.ChestContentGenerator;
import net.smoofyuniverse.dungeon.gen.populator.api.info.RoomInfo;
import net.smoofyuniverse.dungeon.gen.populator.core.RoomPopulator;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class ChestPopulator extends RoomPopulator {
	public static final ChestContentGenerator CHEST_GENERATOR;

	public ChestPopulator() {
		super("chest");
		roomChance(0.03f, 0.01f);
	}

	@Override
	public boolean populateRoom(RoomInfo info, World w, Extent c, Random r) {
		int x = info.minX + r.nextInt(6) + 1, z = info.minZ + r.nextInt(6) + 1;
		int y = info.minY + info.floorOffset + 1;

		if (c.getBlockType(x, y, z) == BlockTypes.AIR && c.getBlockType(x, y - 1, z) != BlockTypes.AIR) {
			CHEST_GENERATOR.generateBlock(c, x, y, z, r);
			return true;
		}
		return false;
	}

	static {
		CHEST_GENERATOR = ChestContentGenerator.builder()
				.add(ItemTypes.TORCH, 4, 0.8).add(ItemTypes.TORCH, 8, 0.4).add(ItemTypes.TORCH, 12, 0.2)
				.add(ItemTypes.APPLE, 1, 0.4)
				.add(ItemTypes.ARROW, 16, 0.1).add(ItemTypes.ARROW, 24, 0.05)
				.add(ItemTypes.DIAMOND, 1, 0.2).add(ItemTypes.IRON_INGOT, 1, 0.5).add(ItemTypes.GOLD_INGOT, 1, 0.6)
				.add(ItemTypes.IRON_SWORD, 1, 0.1).add(ItemTypes.STONE_SWORD, 1, 0.2).add(ItemTypes.WOODEN_SWORD, 1, 0.4)
				.add(ItemTypes.WHEAT, 1, 0.8).add(ItemTypes.WHEAT, 2, 0.1).add(ItemTypes.WHEAT, 3, 0.05).add(ItemTypes.BREAD, 1, 0.2)
				.add(ItemTypes.LEATHER_HELMET, 1, 0.2).add(ItemTypes.LEATHER_CHESTPLATE, 1, 0.2).add(ItemTypes.LEATHER_LEGGINGS, 1, 0.2).add(ItemTypes.LEATHER_BOOTS, 1, 0.2)
				.add(ItemTypes.CHAINMAIL_HELMET, 1, 0.4).add(ItemTypes.CHAINMAIL_CHESTPLATE, 1, 0.4).add(ItemTypes.CHAINMAIL_LEGGINGS, 1, 0.4).add(ItemTypes.CHAINMAIL_BOOTS, 1, 0.4)
				.add(ItemTypes.IRON_HELMET, 1, 0.1).add(ItemTypes.IRON_CHESTPLATE, 1, 0.1).add(ItemTypes.IRON_LEGGINGS, 1, 0.1).add(ItemTypes.IRON_BOOTS, 1, 0.1)
				.add(ItemTypes.FLINT, 3, 0.3).add(ItemTypes.FLINT, 5, 0.2).add(ItemTypes.FLINT, 7, 0.1)
				.add(ItemTypes.PORKCHOP, 1, 0.8).add(ItemTypes.COOKED_PORKCHOP, 1, 0.1)
				.add(ItemTypes.REDSTONE, 5, 0.15).add(ItemTypes.REDSTONE, 8, 0.1).add(ItemTypes.REDSTONE, 13, 0.05).add(ItemTypes.REDSTONE, 21, 0.03)
				.add(ItemTypes.COMPASS, 1, 0.1)
				.add(ItemTypes.FISH, 1, 0.8).add(ItemTypes.COOKED_FISH, 1, 0.2)
				.add(ItemTypes.DYE, 1, 0.2)
				.add(ItemTypes.CAKE, 1, 0.05)
				.add(ItemTypes.COOKIE, 3, 0.8).add(ItemTypes.COOKIE, 5, 0.2)
				.build(3, 4, 4, 4, 5, 5, 5, 6);
	}
}
