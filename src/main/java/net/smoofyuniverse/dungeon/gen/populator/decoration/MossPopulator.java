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

package net.smoofyuniverse.dungeon.gen.populator.decoration;

import com.flowpowered.math.vector.Vector3i;
import net.smoofyuniverse.dungeon.gen.populator.LayerPopulator;
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
		layerIterationChance(0.75f, 0f);
	}

	@Override
	public boolean populateLayer(World w, Extent c, Random r, int layer, int y) {
		Vector3i min = c.getBlockMin();
		int x = min.getX() + r.nextInt(16), z = min.getZ() + r.nextInt(16);
		y += r.nextInt(7);

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
