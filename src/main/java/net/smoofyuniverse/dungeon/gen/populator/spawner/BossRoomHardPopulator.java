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

package net.smoofyuniverse.dungeon.gen.populator.spawner;

import com.flowpowered.math.vector.Vector3i;
import net.smoofyuniverse.dungeon.gen.ChestContentGenerator;
import net.smoofyuniverse.dungeon.gen.populator.LayerPopulator;
import net.smoofyuniverse.dungeon.util.Minecraft;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class BossRoomHardPopulator extends LayerPopulator {

	public static final ChestContentGenerator CHEST_GENERATOR;

	@Override
	public int getMaximumLayer() {
		return 3;
	}

	@Override
	public float getLayerChance() {
		return 0.001f;
	}

	@Override
	public void populateLayer(World w, Extent c, Random r, int layer, int y) {
		LayerPopulator.setFlag(c, layer, true, this.cause);
		LayerPopulator.setFlag(c, layer + 1, true, this.cause);
		LayerPopulator.setFlag(c, layer + 2, true, this.cause);

		Vector3i min = c.getBlockMin();
		int x = min.getX(), z = min.getZ();

		for (int dx = 0; dx < 15; dx++)
			for (int dz = 0; dz < 15; dz++) {
				c.setBlockType(x + dx, y, z + dz, BlockTypes.OBSIDIAN, this.cause);
				for (int dy = 1; dy < 18; dy++)
					c.setBlockType(x + dx, y + dy, z + dz, BlockTypes.AIR, this.cause);
			}

		c.setBlockType(x + 2, y + 1, z + 3, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 2, y + 1, z + 12, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 3, y + 1, z + 2, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 3, y + 1, z + 4, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 3, y + 1, z + 11, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 3, y + 1, z + 13, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 4, y + 1, z + 3, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 4, y + 1, z + 12, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 5, y + 1, z + 7, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 5, y + 1, z + 8, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 6, y + 1, z + 6, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 6, y + 1, z + 9, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 7, y + 1, z + 5, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 7, y + 1, z + 10, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 8, y + 1, z + 5, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 8, y + 1, z + 10, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 9, y + 1, z + 6, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 9, y + 1, z + 9, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 10, y + 1, z + 7, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 10, y + 1, z + 8, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 11, y + 1, z + 3, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 11, y + 1, z + 12, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 12, y + 1, z + 2, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 12, y + 1, z + 4, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 12, y + 1, z + 11, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 12, y + 1, z + 13, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 13, y + 1, z + 3, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 13, y + 1, z + 12, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 3, y + 2, z + 3, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 3, y + 2, z + 12, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 5, y + 2, z + 7, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 5, y + 2, z + 8, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 6, y + 2, z + 6, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 6, y + 2, z + 9, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 7, y + 2, z + 5, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 7, y + 2, z + 10, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 8, y + 2, z + 5, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 8, y + 2, z + 10, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 9, y + 2, z + 6, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 9, y + 2, z + 9, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 10, y + 2, z + 7, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 10, y + 2, z + 8, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 12, y + 2, z + 3, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 12, y + 2, z + 12, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 6, y + 3, z + 7, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 6, y + 3, z + 8, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 7, y + 3, z + 6, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 7, y + 3, z + 9, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 8, y + 3, z + 6, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 8, y + 3, z + 9, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 9, y + 3, z + 7, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 9, y + 3, z + 8, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 7, y + 4, z + 7, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 7, y + 4, z + 8, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 8, y + 4, z + 7, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 8, y + 4, z + 8, BlockTypes.GLASS, this.cause);

		c.setBlockType(x + 6, y + 1, z + 7, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 6, y + 1, z + 8, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 7, y + 1, z + 6, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 7, y + 1, z + 9, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 8, y + 1, z + 6, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 8, y + 1, z + 9, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 9, y + 1, z + 7, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 9, y + 1, z + 8, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 6, y + 2, z + 7, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 6, y + 2, z + 8, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 7, y + 2, z + 6, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 7, y + 2, z + 9, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 8, y + 2, z + 6, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 8, y + 2, z + 9, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 9, y + 2, z + 7, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 9, y + 2, z + 8, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 7, y + 3, z + 7, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 7, y + 3, z + 8, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 8, y + 3, z + 7, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 8, y + 3, z + 8, BlockTypes.NETHER_BRICK, this.cause);

		c.setBlockType(x + 7, y + 1, z + 7, BlockTypes.IRON_ORE, this.cause);
		c.setBlockType(x + 7, y + 1, z + 8, BlockTypes.REDSTONE_ORE, this.cause);
		c.setBlockType(x + 8, y + 1, z + 7, BlockTypes.IRON_ORE, this.cause);

		CHEST_GENERATOR.generateBlock(c, x + 8, y + 1, z + 8, this.cause, r);

		c.setBlockType(x + 7, y + 2, z + 7, BlockTypes.MOB_SPAWNER, this.cause);
		Minecraft.setSpawnerType(c.getTileEntity(x + 7, y + 2, z + 7).get(), EntityTypes.GHAST);

		c.setBlockType(x + 7, y + 2, z + 8, BlockTypes.MOB_SPAWNER, this.cause);
		Minecraft.setSpawnerType(c.getTileEntity(x + 7, y + 2, z + 8).get(), EntityTypes.ZOMBIE);

		c.setBlockType(x + 8, y + 2, z + 7, BlockTypes.MOB_SPAWNER, this.cause);
		Minecraft.setSpawnerType(c.getTileEntity(x + 8, y + 2, z + 7).get(), EntityTypes.PIG_ZOMBIE);

		c.setBlockType(x + 8, y + 2, z + 8, BlockTypes.MOB_SPAWNER, this.cause);
		Minecraft.setSpawnerType(c.getTileEntity(x + 8, y + 2, z + 8).get(), EntityTypes.ZOMBIE);

		c.setBlockType(x + 3, y + 1, z + 3, BlockTypes.MOB_SPAWNER, this.cause);
		Minecraft.setSpawnerType(c.getTileEntity(x + 3, y + 1, z + 3).get(), EntityTypes.ZOMBIE);

		c.setBlockType(x + 3, y + 1, z + 12, BlockTypes.MOB_SPAWNER, this.cause);
		Minecraft.setSpawnerType(c.getTileEntity(x + 3, y + 1, z + 12).get(), EntityTypes.SKELETON);

		c.setBlockType(x + 12, y + 1, z + 3, BlockTypes.MOB_SPAWNER, this.cause);
		Minecraft.setSpawnerType(c.getTileEntity(x + 12, y + 1, z + 3).get(), EntityTypes.ZOMBIE);

		c.setBlockType(x + 12, y + 1, z + 12, BlockTypes.MOB_SPAWNER, this.cause);
		Minecraft.setSpawnerType(c.getTileEntity(x + 12, y + 1, z + 12).get(), EntityTypes.SPIDER);
	}

	static {
		CHEST_GENERATOR = ChestContentGenerator.builder()
				.add(ItemTypes.TORCH, 16, 0.8f).add(ItemTypes.TORCH, 20, 0.4f)
				.add(ItemTypes.ARROW, 24, 0.8f).add(ItemTypes.ARROW, 1, 0.4f)
				.add(ItemTypes.DIAMOND, 3, 0.2f).add(ItemTypes.IRON_INGOT, 3, 0.5f).add(ItemTypes.GOLD_INGOT, 3, 0.5f)
				.add(ItemTypes.IRON_SWORD, 1, 0.5f)
				.add(ItemTypes.MUSHROOM_STEW, 1, 0.8f)
				.add(ItemTypes.IRON_HELMET, 1, 0.2f).add(ItemTypes.IRON_CHESTPLATE, 1, 0.2f).add(ItemTypes.IRON_LEGGINGS, 1, 0.2f).add(ItemTypes.IRON_BOOTS, 1, 0.2f)
				.add(ItemTypes.DIAMOND_HELMET, 1, 0.05f).add(ItemTypes.DIAMOND_CHESTPLATE, 1, 0.05f).add(ItemTypes.DIAMOND_LEGGINGS, 1, 0.05f).add(ItemTypes.DIAMOND_BOOTS, 1, 0.05f)
				.add(ItemTypes.FLINT, 1, 0.4f)
				.add(ItemTypes.COOKED_PORKCHOP, 1, 0.8f)
				.add(ItemTypes.GOLDEN_APPLE, 1, 0.1f)
				.add(ItemTypes.REDSTONE, 7, 0.2f)
				.add(ItemTypes.CAKE, 1, 0.2f)
				.add(ItemTypes.COOKIE, 8, 0.8f)
				.build(4, 5, 5, 6, 6, 6, 7, 7);
	}
}
