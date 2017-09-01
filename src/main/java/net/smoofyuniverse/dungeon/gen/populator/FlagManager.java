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

package net.smoofyuniverse.dungeon.gen.populator;

import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.world.extent.Extent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class FlagManager {
	private static final Map<UUID, FlagManager> worlds = new HashMap<>();

	private Map<Long, ChunkInfo> chunks = new HashMap(); // TODO use fastutil
	private UUID worldId;

	private FlagManager(UUID worldId) {
		this.worldId = worldId;
	}

	public UUID getWorldId() {
		return this.worldId;
	}

	public ChunkInfo getChunk(Extent chunk) {
		Vector3i pos = chunk.getBlockMin();
		return getChunk(pos.getX() >> 4, pos.getZ() >> 4);
	}

	public ChunkInfo getChunk(int x, int z) {
		long pos = (long) x & 4294967295L | ((long) z & 4294967295L) << 32;
		ChunkInfo c = this.chunks.get(pos);
		if (c == null) {
			c = new ChunkInfo(x, z);
			this.chunks.put(pos, c);
		}
		return c;
	}

	public static FlagManager of(Extent e) {
		return of(e.getUniqueId());
	}

	public static FlagManager of(UUID worldId) {
		FlagManager m = worlds.get(worldId);
		if (m == null) {
			m = new FlagManager(worldId);
			worlds.put(worldId, m);
		}
		return m;
	}

	public static final class ChunkInfo {
		public final int x, z;
		private boolean chunk = false;
		private boolean[] layers = new boolean[7];
		private boolean[] rooms = new boolean[28];

		private ChunkInfo(int x, int z) {
			this.x = x;
			this.z = z;
		}

		public boolean getFlag() {
			return this.chunk;
		}

		public void setFlag(boolean value) {
			this.chunk = value;
		}

		public boolean getFlag(int layer) {
			if (layer < 0 || layer > 6)
				throw new IllegalArgumentException("layer");
			return this.layers[layer];
		}

		public void setFlag(int layer, boolean value) {
			if (layer < 0 || layer > 6)
				throw new IllegalArgumentException("layer");
			this.layers[layer] = value;
		}

		public boolean getFlag(int layer, int room) {
			if (layer < 0 || layer > 6)
				throw new IllegalArgumentException("layer");
			if (room < 0 || room > 3)
				throw new IllegalArgumentException("room");
			return this.rooms[layer * 4 + room];
		}

		public void setFlag(int layer, int room, boolean value) {
			if (layer < 0 || layer > 6)
				throw new IllegalArgumentException("layer");
			if (room < 0 || room > 3)
				throw new IllegalArgumentException("room");
			this.rooms[layer * 4 + room] = value;
		}
	}
}
