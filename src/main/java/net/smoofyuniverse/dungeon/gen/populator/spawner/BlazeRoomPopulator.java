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

import com.flowpowered.math.vector.Vector2i;
import net.smoofyuniverse.dungeon.gen.loot.ChestContentGenerator;
import net.smoofyuniverse.dungeon.gen.populator.api.info.RoomInfo;
import net.smoofyuniverse.dungeon.gen.populator.core.RoomPopulator;
import net.smoofyuniverse.dungeon.gen.populator.decoration.ChestPopulator;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

import static com.flowpowered.math.GenericMath.floor;

public class BlazeRoomPopulator extends RoomPopulator {
	public static final BlockState NORTH_STAIRS = BlockTypes.NETHER_BRICK_STAIRS.getDefaultState().with(Keys.DIRECTION, Direction.NORTH).get(),
			SOUTH_STAIRS = BlockTypes.NETHER_BRICK_STAIRS.getDefaultState().with(Keys.DIRECTION, Direction.SOUTH).get(),
			EAST_STAIRS = BlockTypes.NETHER_BRICK_STAIRS.getDefaultState().with(Keys.DIRECTION, Direction.EAST).get(),
			WEST_STAIRS = BlockTypes.NETHER_BRICK_STAIRS.getDefaultState().with(Keys.DIRECTION, Direction.WEST).get();

	public static final ChestContentGenerator CHEST_GENERATOR = ChestPopulator.CHEST_GENERATOR;

	public BlazeRoomPopulator() {
		super("blaze_room");
		roomChance(0.002f, 0.0015f);
	}

	@Override
	protected Vector2i getLayers(int layersCount) {
		return new Vector2i(0, floor(layersCount * 0.5));
	}

	@Override
	public boolean populateRoom(RoomInfo info, World w, Extent c, Random r) {
		info.flag = true;
		int x = info.minX, z = info.minZ;
		int floorY = info.minY + info.floorOffset, ceilingY = info.minY + info.ceilingOffset + 6;

		for (int dx = 0; dx < 8; dx++)
			for (int dz = 0; dz < 8; dz++) {
				c.setBlockType(x + dx, floorY, z + dz, BlockTypes.NETHER_BRICK);
				c.setBlockType(x + dx, floorY - 1, z + dz, BlockTypes.COBBLESTONE);
				for (int y2 = floorY + 1; y2 < ceilingY; y2++)
					c.setBlockType(x + dx, y2, z + dz, BlockTypes.AIR);
			}

		for (int y = floorY + 1; y < ceilingY; y++) {
			c.setBlockType(x, y, z, BlockTypes.NETHER_BRICK);
			c.setBlockType(x + 7, y, z, BlockTypes.NETHER_BRICK);
			c.setBlockType(x, y, z + 7, BlockTypes.NETHER_BRICK);
			c.setBlockType(x + 7, y, z + 7, BlockTypes.NETHER_BRICK);

			c.setBlockType(x + 1, y, z, BlockTypes.NETHER_BRICK_FENCE);
			c.setBlockType(x, y, z + 1, BlockTypes.NETHER_BRICK_FENCE);
			c.setBlockType(x + 6, y, z, BlockTypes.NETHER_BRICK_FENCE);
			c.setBlockType(x, y, z + 6, BlockTypes.NETHER_BRICK_FENCE);
			c.setBlockType(x + 7, y, z + 1, BlockTypes.NETHER_BRICK_FENCE);
			c.setBlockType(x + 1, y, z + 7, BlockTypes.NETHER_BRICK_FENCE);
			c.setBlockType(x + 7, y, z + 6, BlockTypes.NETHER_BRICK_FENCE);
			c.setBlockType(x + 6, y, z + 7, BlockTypes.NETHER_BRICK_FENCE);
		}

		for (int dx = 2; dx < 6; dx++)
			for (int dz = 2; dz < 6; dz++)
				c.setBlockType(x + dx, floorY + 1, z + dz, BlockTypes.NETHER_BRICK);

		c.setBlock(x + 3, floorY + 1, z + 2, SOUTH_STAIRS);
		c.setBlock(x + 4, floorY + 1, z + 2, SOUTH_STAIRS);

		c.setBlock(x + 3, floorY + 1, z + 5, NORTH_STAIRS);
		c.setBlock(x + 4, floorY + 1, z + 5, NORTH_STAIRS);

		c.setBlock(x + 2, floorY + 1, z + 3, EAST_STAIRS);
		c.setBlock(x + 2, floorY + 1, z + 4, EAST_STAIRS);

		c.setBlock(x + 5, floorY + 1, z + 3, WEST_STAIRS);
		c.setBlock(x + 5, floorY + 1, z + 4, WEST_STAIRS);

		c.setBlockType(x + 2, floorY + 2, z + 2, BlockTypes.NETHER_BRICK_FENCE);
		c.setBlockType(x + 5, floorY + 2, z + 2, BlockTypes.NETHER_BRICK_FENCE);
		c.setBlockType(x + 2, floorY + 2, z + 5, BlockTypes.NETHER_BRICK_FENCE);
		c.setBlockType(x + 5, floorY + 2, z + 5, BlockTypes.NETHER_BRICK_FENCE);

		generateSpawner(c, x + 3, floorY + 2, z + 3, EntityTypes.BLAZE);

		CHEST_GENERATOR.generateBlock(c, x + 3, floorY, z + 3, r);
		CHEST_GENERATOR.generateBlock(c, x + 4, floorY, z + 4, r);

		return true;
	}
}
