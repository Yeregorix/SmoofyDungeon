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

package net.smoofyuniverse.dungeon.gen.populator.structure;

import com.flowpowered.math.vector.Vector2i;
import net.smoofyuniverse.dungeon.gen.loot.ChestContentGenerator;
import net.smoofyuniverse.dungeon.gen.populator.api.info.RoomInfo;
import net.smoofyuniverse.dungeon.gen.populator.core.RoomPopulator;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

import static com.flowpowered.math.GenericMath.floor;
import static java.lang.Math.max;

public class LibraryPopulator extends RoomPopulator {
	public static final ChestContentGenerator CHEST_GENERATOR;

	public LibraryPopulator() {
		super("library");
		roomChance(0.001f);
	}

	@Override
	protected Vector2i getLayers(int layersCount) {
		return new Vector2i(max(floor(layersCount * 0.3), 1), layersCount - 2);
	}

	@Override
	public boolean populateRoom(RoomInfo info, World w, Extent c, Random r) {
		info.flag = true;

		int x = info.minX, z = info.minZ;
		int floorY = info.minY + info.floorOffset + 1, ceilingY = info.minY + info.ceilingOffset + 6;

		for (int y = floorY; y < ceilingY; y++) {
			for (int dx = 1; dx < 7; dx++) {
				c.setBlockType(x + dx, y, z, BlockTypes.STONEBRICK);
				c.setBlockType(x + dx, y, z + 7, BlockTypes.STONEBRICK);
			}
			for (int dz = 1; dz < 7; dz++) {
				c.setBlockType(x, y, z + dz, BlockTypes.STONEBRICK);
				c.setBlockType(x + 7, y, z + dz, BlockTypes.STONEBRICK);
			}
		}

		for (int dy = 0; dy < 3; dy++) {
			for (int dx = 3; dx < 5; dx++) {
				c.setBlockType(x + dx, floorY + dy, z, BlockTypes.AIR);
				c.setBlockType(x + dx, floorY + dy, z + 7, BlockTypes.AIR);

				c.setBlockType(x + dx + 2, floorY + dy, z + 1, BlockTypes.BOOKSHELF);
				c.setBlockType(x + dx - 2, floorY + dy, z + 6, BlockTypes.BOOKSHELF);
			}
			for (int dz = 3; dz < 5; dz++) {
				c.setBlockType(x, floorY + dy, z + dz, BlockTypes.AIR);
				c.setBlockType(x + 7, floorY + dy, z + dz, BlockTypes.AIR);

				c.setBlockType(x + 1, floorY + dy, z + dz + 2, BlockTypes.BOOKSHELF);
				c.setBlockType(x + 6, floorY + dy, z + dz - 2, BlockTypes.BOOKSHELF);
			}
		}

		c.setBlockType(x + 3, floorY, z + 4, BlockTypes.BOOKSHELF);
		c.setBlockType(x + 4, floorY, z + 3, BlockTypes.BOOKSHELF);

		c.setBlockType(x + 3, floorY + 1, z + 4, BlockTypes.ENCHANTING_TABLE);
		c.setBlockType(x + 4, floorY + 1, z + 3, BlockTypes.ENCHANTING_TABLE);

		CHEST_GENERATOR.generateBlock(c, x + 3, floorY, z + 3, r, r.nextBoolean() ? Direction.NORTH : Direction.WEST);
		CHEST_GENERATOR.generateBlock(c, x + 4, floorY, z + 4, r, r.nextBoolean() ? Direction.SOUTH : Direction.EAST);

		c.setBlockType(x + 2, floorY, z + 1, BlockTypes.TORCH);
		c.setBlockType(x + 5, floorY, z + 6, BlockTypes.TORCH);

		return true;
	}

	static {
		CHEST_GENERATOR = ChestContentGenerator.builder()
				.add(ItemTypes.TORCH, 16, 0.8).add(ItemTypes.TORCH, 20, 0.4)
				.add(ItemTypes.ARROW, 24, 0.8).add(ItemTypes.ARROW, 5, 0.4)
				.add(ItemTypes.DIAMOND, 3, 0.2).add(ItemTypes.GOLD_INGOT, 3, 0.5).add(ItemTypes.IRON_INGOT, 3, 0.5)
				.add(ItemTypes.IRON_SWORD, 1, 0.5).add(ItemTypes.BEETROOT_SOUP, 1, 0.8)
				.add(ItemTypes.IRON_HELMET, 1, 0.2).add(ItemTypes.IRON_CHESTPLATE, 1, 0.2).add(ItemTypes.IRON_LEGGINGS, 1, 0.2).add(ItemTypes.IRON_BOOTS, 1, 0.2)
				.add(ItemTypes.DIAMOND_HELMET, 1, 0.05).add(ItemTypes.DIAMOND_CHESTPLATE, 1, 0.05).add(ItemTypes.DIAMOND_LEGGINGS, 1, 0.05).add(ItemTypes.DIAMOND_BOOTS, 1, 0.05)
				.add(ItemTypes.FLINT, 1, 0.4).add(ItemTypes.COOKED_MUTTON, 1, 0.8)
				.add(ItemTypes.GOLDEN_APPLE, 1, 0.1).add(ItemTypes.REDSTONE, 7, 0.2)
				.add(ItemTypes.CAKE, 1, 0.2).add(ItemTypes.COOKIE, 8, 0.8)
				.build(3, 3, 4, 4, 4, 5, 5, 6);
	}
}
