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

import com.flowpowered.math.vector.Vector3i;
import net.smoofyuniverse.dungeon.gen.populator.ChunkPopulator;
import net.smoofyuniverse.dungeon.gen.populator.FlagManager.ChunkInfo;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;
import org.spongepowered.api.world.gen.PopulatorObject;
import org.spongepowered.api.world.gen.type.BiomeTreeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OasisPopulator extends ChunkPopulator {
	public static final PopulatorObject[] TREES;

	public OasisPopulator() {
		super("oasis");
	}

	@Override
	public void populateChunk(ChunkInfo info, World w, Extent c, Random r) {
		info.setFlag(true);

		Vector3i min = c.getBlockMin();
		int x = min.getX(), z = min.getZ();

		for (int dx = 0; dx < 16; dx++) {
			for (int dz = 0; dz < 16; dz++) {
				c.setBlockType(x + dx, 29, z + dz, r.nextFloat() < 0.1f ? BlockTypes.CLAY : BlockTypes.DIRT);
				c.setBlockType(x + dx, 30, z + dz, BlockTypes.GRASS);
				c.setBlockType(x + dx, 31, z + dz, r.nextFloat() < 0.1f ? BlockTypes.TALLGRASS : BlockTypes.AIR);
				for (int y = 32; y <= 100; y++)
					c.setBlockType(x + dx, y, z + dz, BlockTypes.AIR);
			}
		}

		for (int dx = 5; dx <= 11; dx++) {
			c.setBlockType(x + dx, 30, z + 5, BlockTypes.WATER);
			c.setBlockType(x + dx, 30, z + 11, BlockTypes.WATER);
		}

		for (int dz = 5; dz <= 11; dz++) {
			c.setBlockType(x + 5, 30, z + dz, BlockTypes.WATER);
			c.setBlockType(x + 11, 30, z + dz, BlockTypes.WATER);
		}

		c.setBlockType(x + 6, 31, z + 6, BlockTypes.REEDS);
		c.setBlockType(x + 6, 31, z + 10, BlockTypes.REEDS);
		c.setBlockType(x + 10, 31, z + 6, BlockTypes.REEDS);
		c.setBlockType(x + 10, 31, z + 10, BlockTypes.REEDS);

		TREES[r.nextInt(TREES.length)].placeObject(w, r, x + 8, 31, z + 8);
	}

	@Override
	public float getChunkChance() {
		return 0.003f;
	}

	static {
		List<PopulatorObject> trees = new ArrayList<>();
		for (BiomeTreeType type : Sponge.getRegistry().getAllOf(BiomeTreeType.class)) {
			trees.add(type.getPopulatorObject());
			type.getLargePopulatorObject().ifPresent(trees::add);
		}
		TREES = trees.toArray(new PopulatorObject[trees.size()]);
	}
}
