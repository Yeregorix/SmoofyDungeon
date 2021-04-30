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

package net.smoofyuniverse.dungeon.gen.populator.api.info;

public final class RoomInfo {
	public final LayerInfo layer;
	public final int minX, minY, minZ;
	public final int index;

	public int floorOffset, ceilingOffset;
	public boolean flag;

	public RoomInfo(LayerInfo layer, int index) {
		if (layer == null)
			throw new IllegalArgumentException("layer");
		if (index < 0 || index > 3)
			throw new IllegalArgumentException("index");

		this.layer = layer;

		this.minX = layer.minX + (index / 2 == 0 ? 0 : 8);
		this.minY = layer.minY;
		this.minZ = layer.minZ + (index % 2 == 0 ? 0 : 8);

		this.index = index;
	}
}
