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

package net.smoofyuniverse.dungeon.gen.populator.structure;

import com.flowpowered.math.vector.Vector2i;
import com.google.common.collect.ImmutableSet;
import net.smoofyuniverse.dungeon.gen.populator.api.info.RoomInfo;
import net.smoofyuniverse.dungeon.gen.populator.core.RoomPopulator;
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
	public static final ImmutableSet BIOME_BLACKLIST = OasisPopulator.BIOME_BLACKLIST;

	public static final BlockState VINE_NORTH = BlockTypes.VINE.getDefaultState().withTrait(BooleanTraits.VINE_NORTH, true).get(),
			LADDER_EAST = BlockTypes.LADDER.getDefaultState().with(Keys.DIRECTION, Direction.EAST).get(),
			LADDER_NORTH = BlockTypes.LADDER.getDefaultState().with(Keys.DIRECTION, Direction.NORTH).get(),
			STONEBRICK_SLAB = BlockTypes.STONE_SLAB.getDefaultState().withTrait(EnumTraits.STONE_SLAB_VARIANT, SlabTypes.SMOOTH_BRICK).get(),
			TORCH_WEST = BlockTypes.TORCH.getDefaultState().with(Keys.DIRECTION, Direction.WEST).get(),
			TORCH_SOUTH = BlockTypes.TORCH.getDefaultState().with(Keys.DIRECTION, Direction.SOUTH).get();

	public EntrancePopulator() {
		super("entrance");
		roomChance(0.004f);
	}

	@Override
	protected Vector2i getLayers(int layersCount) {
		return new Vector2i(layersCount - 1, layersCount - 1);
	}

	@Override
	public boolean populateRoom(RoomInfo info, World w, Extent c, Random r) {
		int x = info.minX, y = info.minY, z = info.minZ;
		if (BIOME_BLACKLIST.contains(w.getBiome(x + 3, 0, z + 3)))
			return false;

		info.flag = true;

		int ground;
		switch (r.nextInt(4)) {
			case 0:
				ground = c.getHighestYAt(x, z + 3);

				for (int dx = 0; dx < 8; dx++) {
					for (int dz = 0; dz < 8; dz++) {
						BlockType type = dx == 0 || dx == 7 || dz == 0 || dz == 7 ? BlockTypes.STONEBRICK : BlockTypes.AIR;
						for (int cy = y; cy < ground; cy++)
							c.setBlockType(x + dx, cy, z + dz, type);

						for (int cy = ground; cy < ground + 3; cy++)
							c.setBlockType(x + dx, cy, z + dz, BlockTypes.AIR);
					}
				}

				for (int cy = y; cy < ground; cy++) {
					if (r.nextFloat() < 0.8f)
						c.setBlock(x + 1, cy, z + 3, LADDER_EAST);
					if (r.nextFloat() < 0.8f)
						c.setBlock(x + 1, cy, z + 4, LADDER_EAST);
				}

				int floorY = y - 6 + info.layer.getRelative(-1).getRoom(info.index).floorOffset;

				if (c.getBlockType(x + 1, floorY, z + 1) == BlockTypes.AIR) {
					c.setBlockType(x + 1, floorY, z + 1, BlockTypes.PLANKS);
					c.setBlockType(x + 1, floorY, z + 6, BlockTypes.PLANKS);
					c.setBlockType(x + 6, floorY, z + 1, BlockTypes.PLANKS);
					c.setBlockType(x + 6, floorY, z + 6, BlockTypes.PLANKS);
				}

				for (int cy = floorY + 1; cy < ground + 3; cy++) {
					c.setBlockType(x + 1, cy, z + 1, BlockTypes.PLANKS);
					c.setBlockType(x + 1, cy, z + 6, BlockTypes.PLANKS);
					c.setBlockType(x + 6, cy, z + 1, BlockTypes.PLANKS);
					c.setBlockType(x + 6, cy, z + 6, BlockTypes.PLANKS);
				}

				for (int cy = ground; cy < ground + 3; cy++) {
					c.setBlockType(x, cy, z, BlockTypes.STONEBRICK);
					c.setBlockType(x, cy, z + 7, BlockTypes.STONEBRICK);
					c.setBlockType(x + 7, cy, z, BlockTypes.STONEBRICK);
					c.setBlockType(x + 7, cy, z + 7, BlockTypes.STONEBRICK);
				}

				for (int i = 1; i < 7; i++) {
					for (int cy = ground; cy < ground + 3; cy++) {
						c.setBlockType(x + i, cy, z, BlockTypes.COBBLESTONE);
						c.setBlockType(x + i, cy, z + 7, BlockTypes.COBBLESTONE);
						c.setBlockType(x, cy, z + i, BlockTypes.COBBLESTONE);
						c.setBlockType(x + 7, cy, z + i, BlockTypes.COBBLESTONE);
					}
				}

				for (int dx = 0; dx < 8; dx++) {
					for (int dz = 0; dz < 8; dz++) {
						if (r.nextFloat() < 0.9f || (dx == 0 || dx == 7 || dz == 0 || dz == 7))
							c.setBlockType(x + dx, ground + 3, z + dz, BlockTypes.STONEBRICK);
					}
				}

				for (int dz = 1; dz < 7; dz++) {
					c.setBlockType(x + 2, ground + 2, z + dz, BlockTypes.PLANKS);
					c.setBlockType(x + 5, ground + 2, z + dz, BlockTypes.PLANKS);
				}

				c.setBlockType(x, ground, z + 2, BlockTypes.FENCE);
				c.setBlockType(x, ground, z + 5, BlockTypes.FENCE);
				c.setBlockType(x, ground + 1, z + 2, BlockTypes.FENCE);
				c.setBlockType(x, ground + 1, z + 5, BlockTypes.FENCE);
				c.setBlockType(x, ground, z + 3, BlockTypes.AIR);
				c.setBlockType(x, ground, z + 4, BlockTypes.AIR);
				c.setBlockType(x, ground + 1, z + 3, BlockTypes.AIR);
				c.setBlockType(x, ground + 1, z + 4, BlockTypes.AIR);
				for (int dz = 2; dz < 6; dz++)
					c.setBlockType(x, ground + 2, z + dz, BlockTypes.PLANKS);
				w.setBlock(x - 1, ground + 1, z + 1, TORCH_WEST);
				w.setBlock(x - 1, ground + 1, z + 6, TORCH_WEST);

				return true;
			case 1:
				ground = c.getHighestYAt(x + 3, z + 7);

				for (int dx = 0; dx < 8; dx++) {
					for (int dz = 0; dz < 8; dz++) {
						BlockType type = dx == 0 || dx == 7 || dz == 0 || dz == 7 ? BlockTypes.STONEBRICK : BlockTypes.AIR;
						for (int cy = y; cy < ground; cy++)
							c.setBlockType(x + dx, cy, z + dz, type);

						for (int cy = ground; cy < ground + 3; cy++)
							c.setBlockType(x + dx, cy, z + dz, BlockTypes.AIR);
					}
				}

				for (int cy = y; cy < ground; cy++) {
					if (r.nextFloat() < 0.8f)
						c.setBlock(x + 3, cy, z + 6, LADDER_NORTH);
					if (r.nextFloat() < 0.8f)
						c.setBlock(x + 4, cy, z + 6, LADDER_NORTH);
				}

				for (int cy = ground; cy < ground + 3; cy++) {
					c.setBlockType(x, cy, z, BlockTypes.STONEBRICK);
					c.setBlockType(x, cy, z + 7, BlockTypes.STONEBRICK);
					c.setBlockType(x + 7, cy, z, BlockTypes.STONEBRICK);
					c.setBlockType(x + 7, cy, z + 7, BlockTypes.STONEBRICK);
				}

				for (int i = 1; i < 7; i++) {
					for (int cy = ground; cy < ground + 3; cy++) {
						c.setBlockType(x + i, cy, z, BlockTypes.COBBLESTONE);
						c.setBlockType(x + i, cy, z + 7, BlockTypes.COBBLESTONE);
						c.setBlockType(x, cy, z + i, BlockTypes.COBBLESTONE);
						c.setBlockType(x + 7, cy, z + i, BlockTypes.COBBLESTONE);
					}
				}

				for (int dx = 0; dx < 8; dx++) {
					for (int dz = 0; dz < 8; dz++) {
						if (r.nextFloat() < 0.9f || (dx == 0 || dx == 7 || dz == 0 || dz == 7))
							c.setBlockType(x + dx, ground + 3, z + dz, BlockTypes.STONEBRICK);
					}
				}

				for (int dx = 1; dx < 7; dx++) {
					c.setBlockType(x + dx, ground + 2, z + 2, BlockTypes.PLANKS);
					c.setBlockType(x + dx, ground + 2, z + 5, BlockTypes.PLANKS);
				}

				c.setBlockType(x + 3, ground, z + 7, BlockTypes.AIR);
				c.setBlockType(x + 4, ground, z + 7, BlockTypes.AIR);
				c.setBlockType(x + 3, ground + 1, z + 7, BlockTypes.AIR);
				c.setBlockType(x + 4, ground + 1, z + 7, BlockTypes.AIR);
				c.setBlock(x + 2, ground + 1, z + 8, TORCH_SOUTH);
				c.setBlock(x + 5, ground + 1, z + 8, TORCH_SOUTH);

				return true;
			case 2:
				ground = c.getHighestYAt(x + 3, z + 3);

				for (int dx = 0; dx < 8; dx++) {
					for (int dz = 0; dz < 8; dz++) {
						BlockType type = dx == 0 || dx == 7 || dz == 0 || dz == 7 ? BlockTypes.STONEBRICK : BlockTypes.AIR;
						for (int cy = y; cy < ground; cy++)
							c.setBlockType(x + dx, cy, z + dz, type);

						for (int cy = ground; cy < ground + 3; cy++)
							c.setBlockType(x + dx, cy, z + dz, BlockTypes.AIR);
					}
				}

				for (int cy = y; cy < ground; cy++) {
					if (r.nextFloat() < 0.8f)
						c.setBlock(x + 1, cy, z + 3, LADDER_EAST);
					if (r.nextFloat() < 0.8f)
						c.setBlock(x + 1, cy, z + 4, LADDER_EAST);
				}

				for (int cy = ground; cy < ground + 3; cy++) {
					c.setBlockType(x, cy, z, BlockTypes.STONEBRICK);
					c.setBlockType(x, cy, z + 7, BlockTypes.STONEBRICK);
					c.setBlockType(x + 7, cy, z, BlockTypes.STONEBRICK);
					c.setBlockType(x + 7, cy, z + 7, BlockTypes.STONEBRICK);
				}

				for (int dx = 0; dx < 8; dx++) {
					for (int dz = 0; dz < 8; dz++) {
						if (dx == 0 || dx == 7 || dz == 0 || dz == 7)
							c.setBlockType(x + dx, ground + 3, z + dz, BlockTypes.NETHER_BRICK);
						else if (r.nextFloat() < 0.95f)
							c.setBlock(x + dx, ground + 3, z + dz, STONEBRICK_SLAB);
					}
				}

				for (int dx = 1; dx < 7; dx++) {
					c.setBlockType(x + dx, ground + 3, z + 2, BlockTypes.NETHER_BRICK);
					c.setBlockType(x + dx, ground + 3, z + 5, BlockTypes.NETHER_BRICK);
				}

				return true;
			case 3:
				ground = c.getHighestYAt(x + 3, z + 3);

				for (int dx = 0; dx < 8; dx++) {
					for (int dz = 0; dz < 8; dz++) {
						BlockType type = dx == 0 || dx == 7 || dz == 0 || dz == 7 ? BlockTypes.STONEBRICK : BlockTypes.AIR;
						for (int cy = y; cy < ground; cy++)
							c.setBlockType(x + dx, cy, z + dz, type);

						for (int cy = ground; cy < ground + 3; cy++)
							c.setBlockType(x + dx, cy, z + dz, BlockTypes.AIR);
					}
				}

				if (r.nextFloat() < 0.33f) {
					for (int cy = y; cy < ground; cy++) {
						if (r.nextFloat() < 0.8f)
							c.setBlock(x + 3, cy, z + 6, LADDER_NORTH);
						if (r.nextFloat() < 0.8f)
							c.setBlock(x + 4, cy, z + 6, LADDER_NORTH);
					}
				} else {
					for (int cy = y; cy < ground; cy++) {
						if (r.nextFloat() < 0.6f)
							c.setBlock(x + 3, cy, z + 1, VINE_NORTH);
						if (r.nextFloat() < 0.6f)
							c.setBlock(x + 4, cy, z + 1, VINE_NORTH);
					}
				}

				c.setBlockType(x, ground, z, BlockTypes.TORCH);
				c.setBlockType(x, ground, z + 7, BlockTypes.TORCH);
				c.setBlockType(x + 7, ground, z, BlockTypes.TORCH);
				c.setBlockType(x + 7, ground, z + 7, BlockTypes.TORCH);

				return true;
			default:
				return false;
		}
	}
}
