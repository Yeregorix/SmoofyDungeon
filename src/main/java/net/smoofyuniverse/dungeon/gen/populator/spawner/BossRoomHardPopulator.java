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

package net.smoofyuniverse.dungeon.gen.populator.spawner;

import com.flowpowered.math.vector.Vector2i;
import net.smoofyuniverse.dungeon.gen.loot.ChestContentGenerator;
import net.smoofyuniverse.dungeon.gen.populator.api.info.LayerInfo;
import net.smoofyuniverse.dungeon.gen.populator.core.LayerPopulator;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

import static com.flowpowered.math.GenericMath.floor;

public class BossRoomHardPopulator extends LayerPopulator {
	public static final ChestContentGenerator CHEST_GENERATOR;

	public BossRoomHardPopulator() {
		super("boss_room_hard");
		layerChance(0.001f);
	}

	@Override
	protected Vector2i getLayers(int layersCount) {
		return new Vector2i(0, floor(layersCount * 0.5));
	}

	@Override
	public boolean populateLayer(LayerInfo info, World w, Extent c, Random r, float layerFactor) {
		info.flag = true;
		info.getRelative(1).flag = true;
		info.getRelative(2).flag = true;

		int x = info.minX, y = info.minY, z = info.minZ;

		for (int dx = 0; dx < 15; dx++)
			for (int dz = 0; dz < 15; dz++) {
				c.setBlockType(x + dx, y, z + dz, BlockTypes.OBSIDIAN);
				for (int dy = 1; dy < 18; dy++)
					c.setBlockType(x + dx, y + dy, z + dz, BlockTypes.AIR);
			}

		c.setBlockType(x + 2, y + 1, z + 3, BlockTypes.GLASS);
		c.setBlockType(x + 2, y + 1, z + 12, BlockTypes.GLASS);
		c.setBlockType(x + 3, y + 1, z + 2, BlockTypes.GLASS);
		c.setBlockType(x + 3, y + 1, z + 4, BlockTypes.GLASS);
		c.setBlockType(x + 3, y + 1, z + 11, BlockTypes.GLASS);
		c.setBlockType(x + 3, y + 1, z + 13, BlockTypes.GLASS);
		c.setBlockType(x + 4, y + 1, z + 3, BlockTypes.GLASS);
		c.setBlockType(x + 4, y + 1, z + 12, BlockTypes.GLASS);
		c.setBlockType(x + 5, y + 1, z + 7, BlockTypes.GLASS);
		c.setBlockType(x + 5, y + 1, z + 8, BlockTypes.GLASS);
		c.setBlockType(x + 6, y + 1, z + 6, BlockTypes.GLASS);
		c.setBlockType(x + 6, y + 1, z + 9, BlockTypes.GLASS);
		c.setBlockType(x + 7, y + 1, z + 5, BlockTypes.GLASS);
		c.setBlockType(x + 7, y + 1, z + 10, BlockTypes.GLASS);
		c.setBlockType(x + 8, y + 1, z + 5, BlockTypes.GLASS);
		c.setBlockType(x + 8, y + 1, z + 10, BlockTypes.GLASS);
		c.setBlockType(x + 9, y + 1, z + 6, BlockTypes.GLASS);
		c.setBlockType(x + 9, y + 1, z + 9, BlockTypes.GLASS);
		c.setBlockType(x + 10, y + 1, z + 7, BlockTypes.GLASS);
		c.setBlockType(x + 10, y + 1, z + 8, BlockTypes.GLASS);
		c.setBlockType(x + 11, y + 1, z + 3, BlockTypes.GLASS);
		c.setBlockType(x + 11, y + 1, z + 12, BlockTypes.GLASS);
		c.setBlockType(x + 12, y + 1, z + 2, BlockTypes.GLASS);
		c.setBlockType(x + 12, y + 1, z + 4, BlockTypes.GLASS);
		c.setBlockType(x + 12, y + 1, z + 11, BlockTypes.GLASS);
		c.setBlockType(x + 12, y + 1, z + 13, BlockTypes.GLASS);
		c.setBlockType(x + 13, y + 1, z + 3, BlockTypes.GLASS);
		c.setBlockType(x + 13, y + 1, z + 12, BlockTypes.GLASS);
		c.setBlockType(x + 3, y + 2, z + 3, BlockTypes.GLASS);
		c.setBlockType(x + 3, y + 2, z + 12, BlockTypes.GLASS);
		c.setBlockType(x + 5, y + 2, z + 7, BlockTypes.GLASS);
		c.setBlockType(x + 5, y + 2, z + 8, BlockTypes.GLASS);
		c.setBlockType(x + 6, y + 2, z + 6, BlockTypes.GLASS);
		c.setBlockType(x + 6, y + 2, z + 9, BlockTypes.GLASS);
		c.setBlockType(x + 7, y + 2, z + 5, BlockTypes.GLASS);
		c.setBlockType(x + 7, y + 2, z + 10, BlockTypes.GLASS);
		c.setBlockType(x + 8, y + 2, z + 5, BlockTypes.GLASS);
		c.setBlockType(x + 8, y + 2, z + 10, BlockTypes.GLASS);
		c.setBlockType(x + 9, y + 2, z + 6, BlockTypes.GLASS);
		c.setBlockType(x + 9, y + 2, z + 9, BlockTypes.GLASS);
		c.setBlockType(x + 10, y + 2, z + 7, BlockTypes.GLASS);
		c.setBlockType(x + 10, y + 2, z + 8, BlockTypes.GLASS);
		c.setBlockType(x + 12, y + 2, z + 3, BlockTypes.GLASS);
		c.setBlockType(x + 12, y + 2, z + 12, BlockTypes.GLASS);
		c.setBlockType(x + 6, y + 3, z + 7, BlockTypes.GLASS);
		c.setBlockType(x + 6, y + 3, z + 8, BlockTypes.GLASS);
		c.setBlockType(x + 7, y + 3, z + 6, BlockTypes.GLASS);
		c.setBlockType(x + 7, y + 3, z + 9, BlockTypes.GLASS);
		c.setBlockType(x + 8, y + 3, z + 6, BlockTypes.GLASS);
		c.setBlockType(x + 8, y + 3, z + 9, BlockTypes.GLASS);
		c.setBlockType(x + 9, y + 3, z + 7, BlockTypes.GLASS);
		c.setBlockType(x + 9, y + 3, z + 8, BlockTypes.GLASS);
		c.setBlockType(x + 7, y + 4, z + 7, BlockTypes.GLASS);
		c.setBlockType(x + 7, y + 4, z + 8, BlockTypes.GLASS);
		c.setBlockType(x + 8, y + 4, z + 7, BlockTypes.GLASS);
		c.setBlockType(x + 8, y + 4, z + 8, BlockTypes.GLASS);

		c.setBlockType(x + 6, y + 1, z + 7, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 6, y + 1, z + 8, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 7, y + 1, z + 6, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 7, y + 1, z + 9, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 8, y + 1, z + 6, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 8, y + 1, z + 9, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 9, y + 1, z + 7, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 9, y + 1, z + 8, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 6, y + 2, z + 7, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 6, y + 2, z + 8, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 7, y + 2, z + 6, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 7, y + 2, z + 9, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 8, y + 2, z + 6, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 8, y + 2, z + 9, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 9, y + 2, z + 7, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 9, y + 2, z + 8, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 7, y + 3, z + 7, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 7, y + 3, z + 8, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 8, y + 3, z + 7, BlockTypes.NETHER_BRICK);
		c.setBlockType(x + 8, y + 3, z + 8, BlockTypes.NETHER_BRICK);

		c.setBlockType(x + 7, y + 1, z + 7, BlockTypes.IRON_ORE);
		c.setBlockType(x + 7, y + 1, z + 8, BlockTypes.REDSTONE_ORE);
		c.setBlockType(x + 8, y + 1, z + 7, BlockTypes.IRON_ORE);

		CHEST_GENERATOR.generateBlock(c, x + 8, y + 1, z + 8, r);

		generateSpawner(c, x + 7, y + 2, z + 7, EntityTypes.GHAST);
		generateSpawner(c, x + 7, y + 2, z + 8, EntityTypes.ZOMBIE);
		generateSpawner(c, x + 8, y + 2, z + 7, EntityTypes.PIG_ZOMBIE);
		generateSpawner(c, x + 8, y + 2, z + 8, EntityTypes.ZOMBIE);
		generateSpawner(c, x + 3, y + 1, z + 3, EntityTypes.ZOMBIE);
		generateSpawner(c, x + 3, y + 1, z + 12, EntityTypes.SKELETON);
		generateSpawner(c, x + 12, y + 1, z + 3, EntityTypes.ZOMBIE);
		generateSpawner(c, x + 12, y + 1, z + 12, EntityTypes.SPIDER);

		return true;
	}

	static {
		CHEST_GENERATOR = ChestContentGenerator.builder()
				.add(ItemTypes.TORCH, 16, 0.8).add(ItemTypes.TORCH, 20, 0.4)
				.add(ItemTypes.ARROW, 24, 0.8).add(ItemTypes.ARROW, 1, 0.4)
				.add(ItemTypes.DIAMOND, 3, 0.2).add(ItemTypes.IRON_INGOT, 3, 0.5).add(ItemTypes.GOLD_INGOT, 3, 0.5)
				.add(ItemTypes.IRON_SWORD, 1, 0.5)
				.add(ItemTypes.MUSHROOM_STEW, 1, 0.8)
				.add(ItemTypes.IRON_HELMET, 1, 0.2).add(ItemTypes.IRON_CHESTPLATE, 1, 0.2).add(ItemTypes.IRON_LEGGINGS, 1, 0.2).add(ItemTypes.IRON_BOOTS, 1, 0.2)
				.add(ItemTypes.DIAMOND_HELMET, 1, 0.05).add(ItemTypes.DIAMOND_CHESTPLATE, 1, 0.05).add(ItemTypes.DIAMOND_LEGGINGS, 1, 0.05).add(ItemTypes.DIAMOND_BOOTS, 1, 0.05)
				.add(ItemTypes.FLINT, 1, 0.4)
				.add(ItemTypes.COOKED_PORKCHOP, 1, 0.8)
				.add(ItemTypes.GOLDEN_APPLE, 1, 0.1)
				.add(ItemTypes.REDSTONE, 7, 0.2)
				.add(ItemTypes.CAKE, 1, 0.2)
				.add(ItemTypes.COOKIE, 8, 0.8)
				.build(4, 5, 5, 6, 6, 6, 7, 7);
	}
}
