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

import net.smoofyuniverse.dungeon.gen.populator.FlagManager.ChunkInfo;
import net.smoofyuniverse.dungeon.gen.populator.RoomPopulator;
import net.smoofyuniverse.dungeon.util.WeightedList;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.PortionTypes;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class CeilingSpawnerPopulator extends RoomPopulator {
	public static final WeightedList<EntityType> ENTITIES = SimpleSpawnerPopulator.ENTITIES;

	private static final BlockType[] ORES = {BlockTypes.COAL_ORE, BlockTypes.DIAMOND_ORE, BlockTypes.EMERALD_ORE,
			BlockTypes.GOLD_ORE, BlockTypes.IRON_ORE, BlockTypes.LAPIS_ORE, BlockTypes.REDSTONE_ORE};

	private static final BlockState TOP_STAIRS = BlockTypes.STONE_STAIRS.getDefaultState().with(Keys.PORTION_TYPE, PortionTypes.TOP).get(),
			NORTH_STAIRS = TOP_STAIRS.with(Keys.DIRECTION, Direction.NORTH).get(),
			SOUTH_STAIRS = TOP_STAIRS.with(Keys.DIRECTION, Direction.SOUTH).get(),
			EAST_STAIRS = TOP_STAIRS.with(Keys.DIRECTION, Direction.EAST).get(),
			WEST_STAIRS = TOP_STAIRS.with(Keys.DIRECTION, Direction.WEST).get();

	public CeilingSpawnerPopulator() {
		super("ceiling_spawner");
		layers(0, 4);
		roomChance(0.002f, -0.0002f);
	}

	@Override
	public boolean populateRoom(ChunkInfo info, World w, Extent c, Random r, int layer, int room, int x, int y, int z) {
		int ceilingY = y + getCeilingOffset(c, x, y, z) + 6;

		c.setBlockType(x + 3, ceilingY - 2, z + 3, BlockTypes.COBBLESTONE);
		c.setBlockType(x + 3, ceilingY - 2, z + 4, BlockTypes.COBBLESTONE);
		c.setBlockType(x + 4, ceilingY - 2, z + 3, BlockTypes.COBBLESTONE);
		c.setBlockType(x + 4, ceilingY - 2, z + 4, BlockTypes.COBBLESTONE);

		c.setBlockType(x + 2, ceilingY - 1, z + 3, BlockTypes.COBBLESTONE_WALL);
		c.setBlockType(x + 2, ceilingY - 1, z + 4, BlockTypes.COBBLESTONE_WALL);
		c.setBlock(x + 2, ceilingY - 2, z + 3, EAST_STAIRS);
		c.setBlock(x + 2, ceilingY - 2, z + 4, EAST_STAIRS);

		c.setBlockType(x + 5, ceilingY - 1, z + 3, BlockTypes.COBBLESTONE_WALL);
		c.setBlockType(x + 5, ceilingY - 1, z + 4, BlockTypes.COBBLESTONE_WALL);
		c.setBlock(x + 5, ceilingY - 2, z + 3, WEST_STAIRS);
		c.setBlock(x + 5, ceilingY - 2, z + 4, WEST_STAIRS);

		c.setBlockType(x + 3, ceilingY - 1, z + 2, BlockTypes.COBBLESTONE_WALL);
		c.setBlockType(x + 4, ceilingY - 1, z + 2, BlockTypes.COBBLESTONE_WALL);
		c.setBlock(x + 3, ceilingY - 2, z + 2, SOUTH_STAIRS);
		c.setBlock(x + 4, ceilingY - 2, z + 2, SOUTH_STAIRS);

		c.setBlockType(x + 3, ceilingY - 1, z + 5, BlockTypes.COBBLESTONE_WALL);
		c.setBlockType(x + 4, ceilingY - 1, z + 5, BlockTypes.COBBLESTONE_WALL);
		c.setBlock(x + 3, ceilingY - 2, z + 5, NORTH_STAIRS);
		c.setBlock(x + 4, ceilingY - 2, z + 5, NORTH_STAIRS);

		c.setBlockType(x + 3, ceilingY - 1, z + 3, ORES[r.nextInt(ORES.length)]);
		c.setBlockType(x + 4, ceilingY - 1, z + 4, ORES[r.nextInt(ORES.length)]);
		generateSpawner(c, x + 3, ceilingY - 1, z + 4, ENTITIES.get(r));
		generateSpawner(c, x + 4, ceilingY - 1, z + 3, ENTITIES.get(r));

		return true;
	}
}
