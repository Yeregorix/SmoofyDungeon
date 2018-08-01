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
import net.smoofyuniverse.dungeon.gen.populator.core.RoomPopulator;
import net.smoofyuniverse.dungeon.gen.populator.core.info.RoomInfo;
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

import static com.flowpowered.math.GenericMath.floor;
import static java.lang.Math.max;

public class SilverfishBlockPopulator extends RoomPopulator {
	public static final BlockState STONEBRICK_EGG = BlockTypes.MONSTER_EGG.getDefaultState().withTrait(EnumTraits.MONSTER_EGG_VARIANT, DisguisedBlockTypes.STONEBRICK).get(),
			MOSSY_STONEBRICK_EGG = BlockTypes.MONSTER_EGG.getDefaultState().withTrait(EnumTraits.MONSTER_EGG_VARIANT, DisguisedBlockTypes.MOSSY_STONEBRICK).get(),
			CRACKED_STONEBRICK_EGG = BlockTypes.MONSTER_EGG.getDefaultState().withTrait(EnumTraits.MONSTER_EGG_VARIANT, DisguisedBlockTypes.CRACKED_STONEBRICK).get(),
			COBBLESTONE_EGG = BlockTypes.MONSTER_EGG.getDefaultState().withTrait(EnumTraits.MONSTER_EGG_VARIANT, DisguisedBlockTypes.COBBLESTONE).get(),
			STONE_EGG = BlockTypes.MONSTER_EGG.getDefaultState().withTrait(EnumTraits.MONSTER_EGG_VARIANT, DisguisedBlockTypes.STONE).get();

	public SilverfishBlockPopulator() {
		super("silverfish_block");
		roomChance(0.33f);
		roomIterations(10, 0);
		roomIterationChance(0.8f, 0.5f);
	}

	@Override
	protected Vector2i getLayers(int layersCount) {
		return new Vector2i(max(floor(layersCount * 0.3), 1), layersCount - 1);
	}

	@Override
	public boolean populateRoom(RoomInfo info, World w, Extent c, Random r) {
		int x = info.minX + r.nextInt(8), z = info.minZ + r.nextInt(8);
		int y = info.minY + r.nextInt(4 - info.floorOffset) + 1 + info.floorOffset;

		BlockState state = c.getBlock(x, y, z);
		BlockType type = state.getType();
		if (type == BlockTypes.STONEBRICK) {
			BrickType bType = ((Optional<BrickType>) state.getTraitValue(EnumTraits.STONEBRICK_VARIANT)).orElse(BrickTypes.DEFAULT);
			if (bType == BrickTypes.DEFAULT)
				c.setBlock(x, y, z, STONEBRICK_EGG);
			else if (bType == BrickTypes.MOSSY)
				c.setBlock(x, y, z, MOSSY_STONEBRICK_EGG);
			else if (bType == BrickTypes.CRACKED)
				c.setBlock(x, y, z, CRACKED_STONEBRICK_EGG);
			else
				return false;
		} else if (type == BlockTypes.COBBLESTONE)
			c.setBlock(x, y, z, COBBLESTONE_EGG);
		else if (type == BlockTypes.STONE)
			c.setBlock(x, y, z, STONE_EGG);
		else
			return false;

		return true;
	}
}
