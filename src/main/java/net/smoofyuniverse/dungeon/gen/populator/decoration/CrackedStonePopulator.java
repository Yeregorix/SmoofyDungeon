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
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.block.trait.EnumTraits;
import org.spongepowered.api.data.type.BrickTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

public class CrackedStonePopulator extends LayerPopulator {
	public static final BlockState CRACKED_STONEBRICK = BlockTypes.STONEBRICK.getDefaultState().withTrait(EnumTraits.STONEBRICK_VARIANT, BrickTypes.CRACKED).get();

	public CrackedStonePopulator() {
		super("cracked_stone");
	}

	@Override
	public int getLayerIterations() {
		return 240;
	}

	@Override
	public float getLayerIterationChance() {
		return 0.7f;
	}

	@Override
	public void populateLayer(World w, Extent c, Random r, int layer, int y) {
		Vector3i min = c.getBlockMin();
		int x = min.getX() + r.nextInt(16), z = min.getZ() + r.nextInt(16);
		y += r.nextInt(7);

		if (c.getBlockType(x, y, z) == BlockTypes.STONEBRICK)
			c.setBlock(x, y, z, CRACKED_STONEBRICK);
	}
}
