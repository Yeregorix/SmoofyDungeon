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

import net.smoofyuniverse.dungeon.gen.populator.RoomPopulator;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.trait.BooleanTraits;
import org.spongepowered.api.block.trait.EnumTraits;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.SlabTypes;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class EntrancePopulator extends RoomPopulator {
	public static final BlockState VINE_NORTH = BlockTypes.VINE.getDefaultState().withTrait(BooleanTraits.VINE_NORTH, true).get(),
			LADDER_EAST = BlockTypes.LADDER.getDefaultState().with(Keys.DIRECTION, Direction.EAST).get(),
			LADDER_NORTH = BlockTypes.LADDER.getDefaultState().with(Keys.DIRECTION, Direction.NORTH).get(),
			STONEBRICK_SLAB = BlockTypes.STONE_SLAB.getDefaultState().withTrait(EnumTraits.STONE_SLAB_VARIANT, SlabTypes.SMOOTH_BRICK).get(),
			TORCH_WEST = BlockTypes.TORCH.getDefaultState().with(Keys.DIRECTION, Direction.WEST).get(),
			TORCH_SOUTH = BlockTypes.TORCH.getDefaultState().with(Keys.DIRECTION, Direction.SOUTH).get();

	@Override
	public int getMinimumLayer() {
		return 6;
	}

	@Override
	public float getRoomChance() {
		return 0.004f;
	}

	@Override
	public void populateRoom(World w, Extent c, Random r, int layer, int room, int x, int y, int z) {
		int ground = 100;

		switch (r.nextInt(4)) {
			case 0:
				while (!isGround(c.getBlockType(x, ground, z + 3)))
					ground--;
				ground++;

				for (int dx = 0; dx < 8; dx++) {
					for (int dz = 0; dz < 8; dz++) {
						BlockType type = dx == 0 || dx == 7 || dz == 0 || dz == 7 ? BlockTypes.STONEBRICK : BlockTypes.AIR;
						for (int cy = y; cy < ground; cy++)
							c.setBlockType(x + dx, cy, z + dz, type, this.cause);

						for (int cy = ground; cy < ground + 3; cy++)
							c.setBlockType(x + dx, cy, z + dz, BlockTypes.AIR, this.cause);
					}
				}

				for (int cy = y; cy < ground; cy++) {
					if (r.nextFloat() < 0.8f)
						c.setBlock(x + 1, cy, z + 3, LADDER_EAST, this.cause);
					if (r.nextFloat() < 0.8f)
						c.setBlock(x + 1, cy, z + 4, LADDER_EAST, this.cause);
				}

				int floor = y - 6;
				floor += getFloorOffset(c, x, floor, z);

				if (c.getBlockType(x + 1, floor, z + 1) == BlockTypes.AIR) {
					c.setBlockType(x + 1, floor, z + 1, BlockTypes.PLANKS, this.cause);
					c.setBlockType(x + 1, floor, z + 6, BlockTypes.PLANKS, this.cause);
					c.setBlockType(x + 6, floor, z + 1, BlockTypes.PLANKS, this.cause);
					c.setBlockType(x + 6, floor, z + 6, BlockTypes.PLANKS, this.cause);
				}

				for (int cy = floor + 1; cy < ground + 3; cy++) {
					c.setBlockType(x + 1, cy, z + 1, BlockTypes.PLANKS, this.cause);
					c.setBlockType(x + 1, cy, z + 6, BlockTypes.PLANKS, this.cause);
					c.setBlockType(x + 6, cy, z + 1, BlockTypes.PLANKS, this.cause);
					c.setBlockType(x + 6, cy, z + 6, BlockTypes.PLANKS, this.cause);
				}

				for (int cy = ground; cy < ground + 3; cy++) {
					c.setBlockType(x, cy, z, BlockTypes.STONEBRICK, this.cause);
					c.setBlockType(x, cy, z + 7, BlockTypes.STONEBRICK, this.cause);
					c.setBlockType(x + 7, cy, z, BlockTypes.STONEBRICK, this.cause);
					c.setBlockType(x + 7, cy, z + 7, BlockTypes.STONEBRICK, this.cause);
				}

				for (int i = 1; i < 7; i++) {
					for (int cy = ground; cy < ground + 3; cy++) {
						c.setBlockType(x + i, cy, z, BlockTypes.COBBLESTONE, this.cause);
						c.setBlockType(x + i, cy, z + 7, BlockTypes.COBBLESTONE, this.cause);
						c.setBlockType(x, cy, z + i, BlockTypes.COBBLESTONE, this.cause);
						c.setBlockType(x + 7, cy, z + i, BlockTypes.COBBLESTONE, this.cause);
					}
				}

				for (int dx = 0; dx < 8; dx++) {
					for (int dz = 0; dz < 8; dz++) {
						if (r.nextFloat() < 0.9f || (dx == 0 || dx == 7 || dz == 0 || dz == 7))
							c.setBlockType(x + dx, ground + 3, z + dz, BlockTypes.STONEBRICK, this.cause);
					}
				}

				for (int dz = 1; dz < 7; dz++) {
					c.setBlockType(x + 2, ground + 2, z + dz, BlockTypes.PLANKS, this.cause);
					c.setBlockType(x + 5, ground + 2, z + dz, BlockTypes.PLANKS, this.cause);
				}

				c.setBlockType(x, ground, z + 2, BlockTypes.FENCE, this.cause);
				c.setBlockType(x, ground, z + 5, BlockTypes.FENCE, this.cause);
				c.setBlockType(x, ground + 1, z + 2, BlockTypes.FENCE, this.cause);
				c.setBlockType(x, ground + 1, z + 5, BlockTypes.FENCE, this.cause);
				c.setBlockType(x, ground, z + 3, BlockTypes.AIR, this.cause);
				c.setBlockType(x, ground, z + 4, BlockTypes.AIR, this.cause);
				c.setBlockType(x, ground + 1, z + 3, BlockTypes.AIR, this.cause);
				c.setBlockType(x, ground + 1, z + 4, BlockTypes.AIR, this.cause);
				for (int dz = 2; dz < 6; dz++)
					c.setBlockType(x, ground + 2, z + dz, BlockTypes.PLANKS, this.cause);
				c.setBlock(x - 1, ground + 1, z + 1, TORCH_WEST, this.cause);
				c.setBlock(x - 1, ground + 1, z + 6, TORCH_WEST, this.cause);
				break;
			case 1:
				while (!isGround(c.getBlockType(x + 3, ground, z + 7)))
					ground--;
				ground++;

				for (int dx = 0; dx < 8; dx++) {
					for (int dz = 0; dz < 8; dz++) {
						BlockType type = dx == 0 || dx == 7 || dz == 0 || dz == 7 ? BlockTypes.STONEBRICK : BlockTypes.AIR;
						for (int cy = y; cy < ground; cy++)
							c.setBlockType(x + dx, cy, z + dz, type, this.cause);

						for (int cy = ground; cy < ground + 3; cy++)
							c.setBlockType(x + dx, cy, z + dz, BlockTypes.AIR, this.cause);
					}
				}

				for (int cy = y; cy < ground; cy++) {
					if (r.nextFloat() < 0.8f)
						c.setBlock(x + 3, cy, z + 6, LADDER_NORTH, this.cause);
					if (r.nextFloat() < 0.8f)
						c.setBlock(x + 4, cy, z + 6, LADDER_NORTH, this.cause);
				}

				for (int cy = ground; cy < ground + 3; cy++) {
					c.setBlockType(x, cy, z, BlockTypes.STONEBRICK, this.cause);
					c.setBlockType(x, cy, z + 7, BlockTypes.STONEBRICK, this.cause);
					c.setBlockType(x + 7, cy, z, BlockTypes.STONEBRICK, this.cause);
					c.setBlockType(x + 7, cy, z + 7, BlockTypes.STONEBRICK, this.cause);
				}

				for (int i = 1; i < 7; i++) {
					for (int cy = ground; cy < ground + 3; cy++) {
						c.setBlockType(x + i, cy, z, BlockTypes.COBBLESTONE, this.cause);
						c.setBlockType(x + i, cy, z + 7, BlockTypes.COBBLESTONE, this.cause);
						c.setBlockType(x, cy, z + i, BlockTypes.COBBLESTONE, this.cause);
						c.setBlockType(x + 7, cy, z + i, BlockTypes.COBBLESTONE, this.cause);
					}
				}

				for (int dx = 0; dx < 8; dx++) {
					for (int dz = 0; dz < 8; dz++) {
						if (r.nextFloat() < 0.9f || (dx == 0 || dx == 7 || dz == 0 || dz == 7))
							c.setBlockType(x + dx, ground + 3, z + dz, BlockTypes.STONEBRICK, this.cause);
					}
				}

				for (int dx = 1; dx < 7; dx++) {
					c.setBlockType(x + dx, ground + 2, z + 2, BlockTypes.PLANKS, this.cause);
					c.setBlockType(x + dx, ground + 2, z + 5, BlockTypes.PLANKS, this.cause);
				}

				c.setBlockType(x + 3, ground, z + 7, BlockTypes.AIR, this.cause);
				c.setBlockType(x + 4, ground, z + 7, BlockTypes.AIR, this.cause);
				c.setBlockType(x + 3, ground + 1, z + 7, BlockTypes.AIR, this.cause);
				c.setBlockType(x + 4, ground + 1, z + 7, BlockTypes.AIR, this.cause);
				c.setBlock(x + 2, ground + 1, z + 8, TORCH_SOUTH, this.cause);
				c.setBlock(x + 5, ground + 1, z + 8, TORCH_SOUTH, this.cause);
				break;
			case 2:
				while (!isGround(c.getBlockType(x + 3, ground, z + 3)))
					ground--;
				ground++;

				for (int dx = 0; dx < 8; dx++) {
					for (int dz = 0; dz < 8; dz++) {
						BlockType type = dx == 0 || dx == 7 || dz == 0 || dz == 7 ? BlockTypes.STONEBRICK : BlockTypes.AIR;
						for (int cy = y; cy < ground; cy++)
							c.setBlockType(x + dx, cy, z + dz, type, this.cause);

						for (int cy = ground; cy < ground + 3; cy++)
							c.setBlockType(x + dx, cy, z + dz, BlockTypes.AIR, this.cause);
					}
				}

				for (int cy = y; cy < ground; cy++) {
					if (r.nextFloat() < 0.8f)
						c.setBlock(x + 1, cy, z + 3, LADDER_EAST, this.cause);
					if (r.nextFloat() < 0.8f)
						c.setBlock(x + 1, cy, z + 4, LADDER_EAST, this.cause);
				}

				for (int cy = ground; cy < ground + 3; cy++) {
					c.setBlockType(x, cy, z, BlockTypes.STONEBRICK, this.cause);
					c.setBlockType(x, cy, z + 7, BlockTypes.STONEBRICK, this.cause);
					c.setBlockType(x + 7, cy, z, BlockTypes.STONEBRICK, this.cause);
					c.setBlockType(x + 7, cy, z + 7, BlockTypes.STONEBRICK, this.cause);
				}

				for (int dx = 0; dx < 8; dx++) {
					for (int dz = 0; dz < 8; dz++) {
						if (dx == 0 || dx == 7 || dz == 0 || dz == 7)
							c.setBlockType(x + dx, ground + 3, z + dz, BlockTypes.NETHER_BRICK, this.cause);
						else if (r.nextFloat() < 0.95f)
							c.setBlock(x + dx, ground + 3, z + dz, STONEBRICK_SLAB, this.cause);
					}
				}

				for (int dx = 1; dx < 7; dx++) {
					c.setBlockType(x + dx, ground + 3, z + 2, BlockTypes.NETHER_BRICK, this.cause);
					c.setBlockType(x + dx, ground + 3, z + 5, BlockTypes.NETHER_BRICK, this.cause);
				}
				break;
			case 3:
				while (!isGround(c.getBlockType(x + 3, ground, z + 3)))
					ground--;
				ground++;

				for (int dx = 0; dx < 8; dx++) {
					for (int dz = 0; dz < 8; dz++) {
						BlockType type = dx == 0 || dx == 7 || dz == 0 || dz == 7 ? BlockTypes.STONEBRICK : BlockTypes.AIR;
						for (int cy = y; cy < ground; cy++)
							c.setBlockType(x + dx, cy, z + dz, type, this.cause);

						for (int cy = ground; cy < ground + 3; cy++)
							c.setBlockType(x + dx, cy, z + dz, BlockTypes.AIR, this.cause);
					}
				}

				if (r.nextFloat() < 0.33f) {
					for (int cy = y; cy < ground; cy++) {
						if (r.nextFloat() < 0.8f)
							c.setBlock(x + 3, cy, z + 6, LADDER_NORTH, this.cause);
						if (r.nextFloat() < 0.8f)
							c.setBlock(x + 4, cy, z + 6, LADDER_NORTH, this.cause);
					}
				} else {
					for (int cy = y; cy < ground; cy++) {
						if (r.nextFloat() < 0.6f)
							c.setBlock(x + 3, cy, z + 1, VINE_NORTH, this.cause);
						if (r.nextFloat() < 0.6f)
							c.setBlock(x + 4, cy, z + 1, VINE_NORTH, this.cause);
					}
				}

				c.setBlockType(x, ground, z, BlockTypes.TORCH, this.cause);
				c.setBlockType(x, ground, z + 7, BlockTypes.TORCH, this.cause);
				c.setBlockType(x + 7, ground, z, BlockTypes.TORCH, this.cause);
				c.setBlockType(x + 7, ground, z + 7, BlockTypes.TORCH, this.cause);
				break;
		}
	}

	private static boolean isGround(BlockType type) {
		return type != BlockTypes.AIR && (type == BlockTypes.GRASS || type == BlockTypes.MYCELIUM || type == BlockTypes.SAND || type == BlockTypes.DIRT);
	}
}
