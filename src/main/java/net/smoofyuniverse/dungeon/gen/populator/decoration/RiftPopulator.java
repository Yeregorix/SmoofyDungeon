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

import com.flowpowered.math.vector.Vector3f;
import com.flowpowered.math.vector.Vector3i;
import net.smoofyuniverse.dungeon.gen.populator.api.info.ChunkInfo;
import net.smoofyuniverse.dungeon.gen.populator.core.ChunkPopulator;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import static com.flowpowered.math.GenericMath.floor;
import static com.flowpowered.math.TrigMath.*;

public class RiftPopulator extends ChunkPopulator {

	public RiftPopulator() {
		super("rift");
		chunkChance(0.1f);
	}

	@Override
	public boolean populateChunk(ChunkInfo info, World w, Extent c, Random r) {
		Vector3f min = new Vector3f(r.nextFloat() * 16f, 10 + r.nextFloat() * 10f, r.nextFloat() * 16f),
				max = new Vector3f(r.nextFloat() * 16f, info.topY - 10 + r.nextFloat() * 30f, r.nextFloat() * 16f),
				axis = max.sub(min);
		int points = (int) (axis.length() * 1.5f);

		float maxSize = 4f + r.nextFloat() * 2f;
		float angle = r.nextFloat() * (float) TWO_PI;
		float dirX = cos(angle), dirZ = sin(angle);

		Set<Vector3i> set = new LinkedHashSet<>();
		for (int i = 0; i <= points; i++) {
			float progress = i / (float) points;
			float size = maxSize * sin(progress * PI) * (0.6f + r.nextFloat() * 0.4f);

			Vector3f center = min.add(axis.mul(progress));
			float cX = center.getX(), cZ = center.getZ();
			int cY = floor(center.getY());

			int points2 = (int) (size * 1.5f);
			for (int j = -points2; j <= points2; j++) {
				float p = (j / (float) points2) * size;
				set.add(new Vector3i(floor(cX + dirX * p), cY, floor(cZ + dirZ * p)));
			}
		}

		for (Vector3i pos : set) {
			int x = info.minX + pos.getX(), y = pos.getY(), z = info.minZ + pos.getZ();
			if (r.nextBoolean())
				w.setBlock(x, y - 1, z, w.getBlock(x, y, z));
			w.setBlockType(x, y, z, BlockTypes.AIR);
		}

		return true;
	}
}
