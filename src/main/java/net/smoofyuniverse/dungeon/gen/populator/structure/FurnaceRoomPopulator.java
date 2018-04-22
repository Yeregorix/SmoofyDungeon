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

package net.smoofyuniverse.dungeon.gen.populator.structure;

import net.smoofyuniverse.dungeon.gen.loot.FurnaceContentGenerator;
import net.smoofyuniverse.dungeon.gen.populator.FlagManager.ChunkInfo;
import net.smoofyuniverse.dungeon.gen.populator.RoomPopulator;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class FurnaceRoomPopulator extends RoomPopulator {
	public static final FurnaceContentGenerator FURNACE_GENERATOR;

	public FurnaceRoomPopulator() {
		super("furnace_room");
	}

	@Override
	public int getMaximumLayer() {
		return 4;
	}

	@Override
	public float getRoomChance() {
		return 0.001f;
	}

	@Override
	public void populateRoom(ChunkInfo info, World w, Extent c, Random r, int layer, int room, int x, int y, int z) {
		info.setFlag(layer, room, true);

		int ceilingY = y + getCeilingOffset(c, x, y, z) + 6;

		for (int dx = 0; dx <= 8; dx++) {
			for (int dz = 0; dz <= 8; dz++) {
				c.setBlockType(x + dx, y, z + dz, BlockTypes.COBBLESTONE);
				c.setBlockType(x + dx, y + 1, z + dz, BlockTypes.STONE);
			}
		}

		for (int cy = y + 1; cy < ceilingY; cy++) {
			c.setBlockType(x + 1, cy, z + 1, BlockTypes.COBBLESTONE);
			c.setBlockType(x + 7, cy, z + 1, BlockTypes.COBBLESTONE);
			c.setBlockType(x + 1, cy, z + 7, BlockTypes.COBBLESTONE);
			c.setBlockType(x + 7, cy, z + 7, BlockTypes.COBBLESTONE);
		}

		FURNACE_GENERATOR.generateBlock(c, x + 2, y + 2, z + 2, r, Direction.NORTH);
		c.setBlockType(x + 3, y + 2, z + 2, BlockTypes.GLASS);
		c.setBlockType(x + 4, y + 2, z + 2, BlockTypes.GLASS);
		FURNACE_GENERATOR.generateBlock(c, x + 5, y + 2, z + 2, r, Direction.NORTH);
		c.setBlockType(x + 2, y + 2, z + 3, BlockTypes.GLASS);
		c.setBlockType(x + 3, y + 2, z + 3, BlockTypes.LAVA);
		c.setBlockType(x + 4, y + 2, z + 3, BlockTypes.LAVA);
		c.setBlockType(x + 5, y + 2, z + 3, BlockTypes.GLASS);
		c.setBlockType(x + 2, y + 2, z + 4, BlockTypes.GLASS);
		c.setBlockType(x + 3, y + 2, z + 4, BlockTypes.LAVA);
		c.setBlockType(x + 4, y + 2, z + 4, BlockTypes.LAVA);
		c.setBlockType(x + 5, y + 2, z + 4, BlockTypes.GLASS);
		FURNACE_GENERATOR.generateBlock(c, x + 2, y + 2, z + 5, r, Direction.SOUTH);
		c.setBlockType(x + 3, y + 2, z + 5, BlockTypes.GLASS);
		c.setBlockType(x + 4, y + 2, z + 5, BlockTypes.GLASS);
		FURNACE_GENERATOR.generateBlock(c, x + 5, y + 2, z + 5, r, Direction.SOUTH);

		for (int dx = 3; dx <= 4; dx++) {
			for (int dz = 3; dz <= 4; dz++) {
				for (int cy = y + 3; cy < ceilingY; cy++)
					c.setBlockType(x + dx, cy, z + dz, BlockTypes.BRICK_BLOCK);
			}
		}
	}

	static {
		FURNACE_GENERATOR = FurnaceContentGenerator.builder()
				.add(ItemTypes.GOLD_BLOCK, 1, 0.05).add(ItemTypes.IRON_BLOCK, 1, 0.05)
				.add(ItemTypes.BRICK_BLOCK, 1, 0.20).add(ItemTypes.COAL, 1, 0.80).add(ItemTypes.COAL, 1, 0.80)
				.add(ItemTypes.IRON_INGOT, 2, 0.80).add(ItemTypes.IRON_INGOT, 4, 0.20).add(ItemTypes.GOLD_INGOT, 2, 0.80).add(ItemTypes.GOLD_INGOT, 4, 0.20)
				.add(ItemTypes.BREAD, 1, 0.40).add(ItemTypes.BUCKET, 1, 0.40)
				.add(ItemTypes.COOKED_CHICKEN, 2, 0.80).add(ItemTypes.COOKED_CHICKEN, 4, 0.20)
				.add(ItemTypes.FLINT, 3, 0.80).add(ItemTypes.FLINT, 5, 0.40)
				.add(ItemTypes.COOKED_PORKCHOP, 1, 0.80).add(ItemTypes.COOKED_FISH, 1, 0.40)
				.add(ItemTypes.ENDER_PEARL, 1, 0.30).add(ItemTypes.BLAZE_ROD, 1, 0.30).add(ItemTypes.GHAST_TEAR, 1, 0.30)
				.add(ItemTypes.GOLD_NUGGET, 1, 0.45).add(ItemTypes.NETHER_WART, 1, 0.30)
				.add(ItemTypes.SPIDER_EYE, 1, 0.30).add(ItemTypes.BLAZE_POWDER, 1, 0.30).add(ItemTypes.MAGMA_CREAM, 1, 0.30)
				.add(ItemTypes.ENDER_EYE, 1, 0.30).add(ItemTypes.SPECKLED_MELON, 1, 0.30)
				.addNone(2).build();
	}
}
