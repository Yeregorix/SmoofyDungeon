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

import net.smoofyuniverse.dungeon.gen.populator.RoomPopulator;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.trait.EnumTraits;
import org.spongepowered.api.data.type.BrickType;
import org.spongepowered.api.data.type.BrickTypes;
import org.spongepowered.api.data.type.DisguisedBlockTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Optional;
import java.util.Random;

public class SilverfishBlockPopulator extends RoomPopulator {
	public static final BlockState STONEBRICK_EGG = BlockTypes.MONSTER_EGG.getDefaultState().withTrait(EnumTraits.MONSTER_EGG_VARIANT, DisguisedBlockTypes.STONEBRICK).get(),
			MOSSY_STONEBRICK_EGG = BlockTypes.MONSTER_EGG.getDefaultState().withTrait(EnumTraits.MONSTER_EGG_VARIANT, DisguisedBlockTypes.MOSSY_STONEBRICK).get(),
			CRACKED_STONEBRICK_EGG = BlockTypes.MONSTER_EGG.getDefaultState().withTrait(EnumTraits.MONSTER_EGG_VARIANT, DisguisedBlockTypes.CRACKED_STONEBRICK).get(),
			COBBLESTONE_EGG = BlockTypes.MONSTER_EGG.getDefaultState().withTrait(EnumTraits.MONSTER_EGG_VARIANT, DisguisedBlockTypes.COBBLESTONE).get(),
			STONE_EGG = BlockTypes.MONSTER_EGG.getDefaultState().withTrait(EnumTraits.MONSTER_EGG_VARIANT, DisguisedBlockTypes.STONE).get();

	@Override
	public int getMinimumLayer() {
		return 2;
	}

	@Override
	public float getRoomIterationChance() {
		return 0.75f;
	}

	@Override
	public float getRoomIterationChanceAdditionPerLayer() {
		return -0.04f;
	}

	@Override
	public int getRoomIterations() {
		return 8;
	}

	@Override
	public void populateRoom(World w, Extent c, Random r, int layer, int room, int x, int y, int z, int floorOffset, int ceilingOffset) {
		x += r.nextInt(8);
		y += r.nextInt(4 - floorOffset) + 1 + floorOffset;
		z += r.nextInt(8);

		BlockState state = c.getBlock(x, y, z);
		BlockType type = state.getType();
		if (type == BlockTypes.STONEBRICK) {
			BrickType bType = ((Optional<BrickType>) state.getTraitValue(EnumTraits.STONEBRICK_VARIANT)).orElse(BrickTypes.DEFAULT);
			if (bType == BrickTypes.DEFAULT)
				c.setBlock(x, y, z, STONEBRICK_EGG, this.cause);
			else if (bType == BrickTypes.MOSSY)
				c.setBlock(x, y, z, MOSSY_STONEBRICK_EGG, this.cause);
			else if (bType == BrickTypes.CRACKED)
				c.setBlock(x, y, z, CRACKED_STONEBRICK_EGG, this.cause);
		} else if (type == BlockTypes.COBBLESTONE)
			c.setBlock(x, y, z, COBBLESTONE_EGG, this.cause);
		else if (type == BlockTypes.STONE)
			c.setBlock(x, y, z, STONE_EGG, this.cause);
	}
}
