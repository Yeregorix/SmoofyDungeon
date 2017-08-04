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
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class BossRoomInsanePopulator extends LayerPopulator {

	public static final ChestContentGenerator CHEST_GENERATOR = BossRoomHardPopulator.CHEST_GENERATOR;

	@Override
	public int getMaximumLayer() {
		return 2;
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
		c.setBlockType(x + 12, y + 2, z + 3, BlockTypes.GLASS, this.cause);
		c.setBlockType(x + 12, y + 2, z + 12, BlockTypes.GLASS, this.cause);

		c.setBlockType(x + 5, y + 1, z + 7, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 5, y + 1, z + 8, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 6, y + 1, z + 6, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 6, y + 1, z + 7, BlockTypes.SOUL_SAND, this.cause);
		c.setBlockType(x + 6, y + 1, z + 8, BlockTypes.SOUL_SAND, this.cause);
		c.setBlockType(x + 6, y + 1, z + 9, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 7, y + 1, z + 5, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 7, y + 1, z + 6, BlockTypes.SOUL_SAND, this.cause);
		c.setBlockType(x + 7, y + 1, z + 9, BlockTypes.SOUL_SAND, this.cause);
		c.setBlockType(x + 7, y + 1, z + 10, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 8, y + 1, z + 5, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 8, y + 1, z + 6, BlockTypes.SOUL_SAND, this.cause);
		c.setBlockType(x + 8, y + 1, z + 9, BlockTypes.SOUL_SAND, this.cause);
		c.setBlockType(x + 8, y + 1, z + 10, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 9, y + 1, z + 6, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 9, y + 1, z + 7, BlockTypes.SOUL_SAND, this.cause);
		c.setBlockType(x + 9, y + 1, z + 8, BlockTypes.SOUL_SAND, this.cause);
		c.setBlockType(x + 9, y + 1, z + 9, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 10, y + 1, z + 7, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 10, y + 1, z + 8, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 5, y + 2, z + 7, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 5, y + 2, z + 8, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 6, y + 2, z + 6, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 6, y + 2, z + 7, BlockTypes.SOUL_SAND, this.cause);
		c.setBlockType(x + 6, y + 2, z + 8, BlockTypes.SOUL_SAND, this.cause);
		c.setBlockType(x + 6, y + 2, z + 9, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 7, y + 2, z + 5, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 7, y + 2, z + 6, BlockTypes.SOUL_SAND, this.cause);
		c.setBlockType(x + 7, y + 2, z + 9, BlockTypes.SOUL_SAND, this.cause);
		c.setBlockType(x + 7, y + 2, z + 10, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 8, y + 2, z + 5, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 8, y + 2, z + 6, BlockTypes.SOUL_SAND, this.cause);
		c.setBlockType(x + 8, y + 2, z + 9, BlockTypes.SOUL_SAND, this.cause);
		c.setBlockType(x + 8, y + 2, z + 10, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 9, y + 2, z + 6, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 9, y + 2, z + 7, BlockTypes.SOUL_SAND, this.cause);
		c.setBlockType(x + 9, y + 2, z + 8, BlockTypes.SOUL_SAND, this.cause);
		c.setBlockType(x + 9, y + 2, z + 9, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 10, y + 2, z + 7, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 10, y + 2, z + 8, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 6, y + 3, z + 7, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 6, y + 3, z + 8, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 7, y + 3, z + 6, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 7, y + 3, z + 7, BlockTypes.SOUL_SAND, this.cause);
		c.setBlockType(x + 7, y + 3, z + 8, BlockTypes.SOUL_SAND, this.cause);
		c.setBlockType(x + 7, y + 3, z + 9, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 8, y + 3, z + 6, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 8, y + 3, z + 7, BlockTypes.SOUL_SAND, this.cause);
		c.setBlockType(x + 8, y + 3, z + 8, BlockTypes.SOUL_SAND, this.cause);
		c.setBlockType(x + 8, y + 3, z + 9, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 9, y + 3, z + 7, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 9, y + 3, z + 8, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 7, y + 4, z + 7, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 7, y + 4, z + 8, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 8, y + 4, z + 7, BlockTypes.NETHER_BRICK, this.cause);
		c.setBlockType(x + 8, y + 4, z + 8, BlockTypes.NETHER_BRICK, this.cause);

		c.setBlockType(x + 7, y + 1, z + 7, BlockTypes.GOLD_BLOCK, this.cause);
		c.setBlockType(x + 8, y + 1, z + 8, BlockTypes.IRON_BLOCK, this.cause);

		CHEST_GENERATOR.generateBlock(c, x + 7, y + 1, z + 8, this.cause, r);
		CHEST_GENERATOR.generateBlock(c, x + 8, y + 1, z + 7, this.cause, r);

		c.setBlockType(x + 7, y + 2, z + 7, BlockTypes.MOB_SPAWNER, this.cause);
		Minecraft.setSpawnerType(c.getTileEntity(x + 7, y + 2, z + 7).get(), EntityTypes.GHAST);

		c.setBlockType(x + 7, y + 2, z + 8, BlockTypes.MOB_SPAWNER, this.cause);
		Minecraft.setSpawnerType(c.getTileEntity(x + 7, y + 2, z + 8).get(), EntityTypes.ZOMBIE);

		c.setBlockType(x + 8, y + 2, z + 7, BlockTypes.MOB_SPAWNER, this.cause);
		Minecraft.setSpawnerType(c.getTileEntity(x + 8, y + 2, z + 7).get(), EntityTypes.PIG_ZOMBIE);

		c.setBlockType(x + 8, y + 2, z + 8, BlockTypes.MOB_SPAWNER, this.cause);
		Minecraft.setSpawnerType(c.getTileEntity(x + 8, y + 2, z + 8).get(), EntityTypes.PIG_ZOMBIE);

		c.setBlockType(x + 7, y + 3, z + 7, BlockTypes.MOB_SPAWNER, this.cause);
		Minecraft.setSpawnerType(c.getTileEntity(x + 7, y + 3, z + 7).get(), EntityTypes.SKELETON);

		c.setBlockType(x + 7, y + 3, z + 8, BlockTypes.MOB_SPAWNER, this.cause);
		Minecraft.setSpawnerType(c.getTileEntity(x + 7, y + 3, z + 8).get(), EntityTypes.ZOMBIE);

		c.setBlockType(x + 8, y + 3, z + 7, BlockTypes.MOB_SPAWNER, this.cause);
		Minecraft.setSpawnerType(c.getTileEntity(x + 8, y + 3, z + 7).get(), EntityTypes.PIG_ZOMBIE);

		c.setBlockType(x + 8, y + 3, z + 8, BlockTypes.MOB_SPAWNER, this.cause);
		Minecraft.setSpawnerType(c.getTileEntity(x + 8, y + 3, z + 8).get(), EntityTypes.ZOMBIE);

		c.setBlockType(x + 3, y + 1, z + 3, BlockTypes.MOB_SPAWNER, this.cause);
		Minecraft.setSpawnerType(c.getTileEntity(x + 3, y + 1, z + 3).get(), EntityTypes.ZOMBIE);

		c.setBlockType(x + 3, y + 1, z + 12, BlockTypes.MOB_SPAWNER, this.cause);
		Minecraft.setSpawnerType(c.getTileEntity(x + 3, y + 1, z + 12).get(), EntityTypes.SKELETON);

		c.setBlockType(x + 12, y + 1, z + 3, BlockTypes.MOB_SPAWNER, this.cause);
		Minecraft.setSpawnerType(c.getTileEntity(x + 12, y + 1, z + 3).get(), EntityTypes.ZOMBIE);

		c.setBlockType(x + 12, y + 1, z + 12, BlockTypes.MOB_SPAWNER, this.cause);
		Minecraft.setSpawnerType(c.getTileEntity(x + 12, y + 1, z + 12).get(), EntityTypes.SPIDER);
	}
}
