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
import net.smoofyuniverse.dungeon.gen.loot.FurnaceContentGenerator;
import net.smoofyuniverse.dungeon.gen.populator.api.info.RoomInfo;
import net.smoofyuniverse.dungeon.gen.populator.core.RoomPopulator;
import net.smoofyuniverse.dungeon.gen.populator.decoration.ChestPopulator;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.trait.IntegerTraits;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

import static com.flowpowered.math.GenericMath.floor;
import static java.lang.Math.max;

public class CastleRoomPopulator extends RoomPopulator {
	public static final ChestContentGenerator CHEST_GENERATOR = ChestPopulator.CHEST_GENERATOR;
	public static final FurnaceContentGenerator FURNACE_GENERATOR = FurnaceRoomPopulator.FURNACE_GENERATOR;

	public CastleRoomPopulator() {
		super("castle_room");
		roomChance(0.001f);
	}

	@Override
	protected Vector2i getLayers(int layersCount) {
		return new Vector2i(max(floor(layersCount * 0.1), 1), layersCount - 2);
	}

	@Override
	public boolean populateRoom(RoomInfo info, World w, Extent c, Random r) {
		info.flag = true;
		int x = info.minX, z = info.minZ;
		int floorY = info.minY + info.floorOffset + 1, ceilingY = info.minY + info.ceilingOffset + 6;

		for (int i = 1; i < 7; i++) {
			for (int y = floorY; y < ceilingY; y++) {
				c.setBlockType(x + i, y, z, BlockTypes.AIR);
				c.setBlockType(x + i, y, z + 7, BlockTypes.AIR);
				c.setBlockType(x, y, z + i, BlockTypes.AIR);
				c.setBlockType(x + 7, y, z + i, BlockTypes.AIR);
			}

			c.setBlockType(x + i, floorY, z + 1, BlockTypes.STONEBRICK);
			c.setBlockType(x + i, floorY, z + 6, BlockTypes.STONEBRICK);
			c.setBlockType(x + 1, floorY, z + i, BlockTypes.STONEBRICK);
			c.setBlockType(x + 6, floorY, z + i, BlockTypes.STONEBRICK);

			c.setBlockType(x + i, floorY + 1, z + 1, BlockTypes.STONEBRICK);
			c.setBlockType(x + i, floorY + 1, z + 6, BlockTypes.STONEBRICK);
			c.setBlockType(x + 1, floorY + 1, z + i, BlockTypes.STONEBRICK);
			c.setBlockType(x + 6, floorY + 1, z + i, BlockTypes.STONEBRICK);

			c.setBlockType(x + i, floorY + 2, z, BlockTypes.STONEBRICK);
			c.setBlockType(x + i, floorY + 2, z + 7, BlockTypes.STONEBRICK);
			c.setBlockType(x, floorY + 2, z + i, BlockTypes.STONEBRICK);
			c.setBlockType(x + 7, floorY + 2, z + i, BlockTypes.STONEBRICK);
		}

		c.setBlockType(x, floorY + 3, z + 1, BlockTypes.STONE_SLAB);
		c.setBlockType(x, floorY + 3, z + 1, BlockTypes.STONE_SLAB);
		c.setBlockType(x, floorY + 3, z + 3, BlockTypes.STONE_SLAB);
		c.setBlockType(x, floorY + 3, z + 3, BlockTypes.STONE_SLAB);
		c.setBlockType(x, floorY + 3, z + 5, BlockTypes.STONE_SLAB);
		c.setBlockType(x, floorY + 3, z + 5, BlockTypes.STONE_SLAB);

		c.setBlockType(x + 7, floorY + 3, z + 2, BlockTypes.STONE_SLAB);
		c.setBlockType(x + 7, floorY + 3, z + 2, BlockTypes.STONE_SLAB);
		c.setBlockType(x + 7, floorY + 3, z + 4, BlockTypes.STONE_SLAB);
		c.setBlockType(x + 7, floorY + 3, z + 4, BlockTypes.STONE_SLAB);
		c.setBlockType(x + 7, floorY + 3, z + 6, BlockTypes.STONE_SLAB);
		c.setBlockType(x + 7, floorY + 3, z + 6, BlockTypes.STONE_SLAB);

		c.setBlockType(x + 1, floorY + 3, z, BlockTypes.STONE_SLAB);
		c.setBlockType(x + 1, floorY + 3, z, BlockTypes.STONE_SLAB);
		c.setBlockType(x + 3, floorY + 3, z, BlockTypes.STONE_SLAB);
		c.setBlockType(x + 3, floorY + 3, z, BlockTypes.STONE_SLAB);
		c.setBlockType(x + 5, floorY + 3, z, BlockTypes.STONE_SLAB);
		c.setBlockType(x + 5, floorY + 3, z, BlockTypes.STONE_SLAB);

		c.setBlockType(x + 2, floorY + 3, z + 7, BlockTypes.STONE_SLAB);
		c.setBlockType(x + 2, floorY + 3, z + 7, BlockTypes.STONE_SLAB);
		c.setBlockType(x + 4, floorY + 3, z + 7, BlockTypes.STONE_SLAB);
		c.setBlockType(x + 4, floorY + 3, z + 7, BlockTypes.STONE_SLAB);
		c.setBlockType(x + 6, floorY + 3, z + 7, BlockTypes.STONE_SLAB);
		c.setBlockType(x + 6, floorY + 3, z + 7, BlockTypes.STONE_SLAB);

		c.setBlockType(x + 1, floorY + 2, z + 1, BlockTypes.TORCH);
		c.setBlockType(x + 1, floorY + 2, z + 6, BlockTypes.TORCH);
		c.setBlockType(x + 6, floorY + 2, z + 1, BlockTypes.TORCH);
		c.setBlockType(x + 6, floorY + 2, z + 6, BlockTypes.TORCH);

		c.setBlockType(x + 2, floorY, z + 5, BlockTypes.LADDER);
		c.setBlockType(x + 2, floorY + 1, z + 5, BlockTypes.LADDER);

		c.setBlockType(x + 2, floorY, z + 2, BlockTypes.CRAFTING_TABLE);

		CHEST_GENERATOR.generateBlock(c, x + 5, floorY, z + 2, r, Direction.WEST);
		CHEST_GENERATOR.generateBlock(c, x + 5, floorY, z + 3, r, Direction.WEST);

		FURNACE_GENERATOR.generateBlock(c, x + 5, floorY, z + 4, r, Direction.WEST);
		FURNACE_GENERATOR.generateBlock(c, x + 5, floorY, z + 5, r, Direction.WEST);

		c.setBlock(x + 5, floorY + 1, z + 5, BlockTypes.CAKE.getDefaultState().withTrait(IntegerTraits.CAKE_BITES, r.nextInt(4)).get());

		return true;
	}
}
