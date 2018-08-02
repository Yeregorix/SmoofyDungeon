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

package net.smoofyuniverse.dungeon.gen.populator.core.info;

public final class LayerInfo {
	public final ChunkInfo chunk;
	public final int minX, minZ;
	public final int index, minY;
	public boolean flag;
	private RoomInfo[] rooms;

	public LayerInfo(ChunkInfo chunk, int index) {
		if (chunk == null)
			throw new IllegalArgumentException("chunk");
		if (index < 0)
			throw new IllegalArgumentException("index");

		this.chunk = chunk;

		this.minX = chunk.minX;
		this.minZ = chunk.minZ;

		this.index = index;
		this.minY = 4 + index * 6;

		this.rooms = new RoomInfo[4];
		for (int i = 0; i < 4; i++)
			this.rooms[i] = new RoomInfo(this, i);
	}

	public RoomInfo getRoom(int index) {
		return this.rooms[index];
	}

	public LayerInfo getRelative(int rel) {
		return this.chunk.getLayer(this.index + rel);
	}
}
