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

package net.smoofyuniverse.dungeon.gen.populator.decoration;

import net.smoofyuniverse.dungeon.gen.populator.api.info.LayerInfo;
import net.smoofyuniverse.dungeon.gen.populator.core.LayerPopulator;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.trait.EnumTraits;
import org.spongepowered.api.data.type.BrickTypes;
import org.spongepowered.api.data.type.WallTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class MossPopulator extends LayerPopulator {
	public static final BlockState MOSSY_STONEBRICK = BlockTypes.STONEBRICK.getDefaultState().withTrait(EnumTraits.STONEBRICK_VARIANT, BrickTypes.MOSSY).get(),
			MOSSY_COBBLESTONE_WALL = BlockTypes.COBBLESTONE_WALL.getDefaultState().withTrait(EnumTraits.COBBLESTONE_WALL_VARIANT, WallTypes.MOSSY).get();

	public MossPopulator() {
		super("moss");
		layerIterations(320, 0);
		layerIterationChance(0.75f);
	}

	@Override
	public boolean populateLayer(LayerInfo info, World w, Extent c, Random r, float layerFactor) {
		int x = info.minX + r.nextInt(16), z = info.minZ + r.nextInt(16);
		int y = info.minY + r.nextInt(7);

		BlockType type = c.getBlockType(x, y, z);
		if (type == BlockTypes.COBBLESTONE)
			c.setBlockType(x, y, z, BlockTypes.MOSSY_COBBLESTONE);
		else if (type == BlockTypes.STONEBRICK)
			c.setBlock(x, y, z, MOSSY_STONEBRICK);
		else if (type == BlockTypes.COBBLESTONE_WALL)
			c.setBlock(x, y, z, MOSSY_COBBLESTONE_WALL);

		return true;
	}
}
