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

package net.smoofyuniverse.dungeon.gen.populator.decoration;

import com.flowpowered.math.vector.Vector3i;
import net.smoofyuniverse.dungeon.gen.populator.api.info.ChunkInfo;
import net.smoofyuniverse.dungeon.gen.populator.core.ChunkPopulator;
import net.smoofyuniverse.dungeon.util.ResourceUtil;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;
import java.util.Set;

public class ExplosionPopulator extends ChunkPopulator {

	public ExplosionPopulator() {
		super("explosion");
		chunkChance(0.8f);
	}

	@Override
	public boolean populateChunk(ChunkInfo info, World w, Extent c, Random r) {

		int count = 0, min = 3 + info.layersCount;
		while (count < min) {
			Set<Vector3i> blocks = ResourceUtil.simulateExplosion(w, r, info.minX + (r.nextDouble() * 16d), 4 + (r.nextDouble() * (info.topY - 4)), info.minZ + (r.nextDouble() * 16d), 2f + (r.nextFloat() * 2f));
			for (Vector3i b : blocks)
				w.setBlockType(b, BlockTypes.AIR);

			count += blocks.size();
		}

		return true;
	}
}
