/*
 * Copyright (c) 2017, 2018 Hugo Dupanloup (Yeregorix)
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

package net.smoofyuniverse.dungeon.util.random;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomQueue<T> {
	private Random random;
	private T[] values;
	private int remainingSize;

	public RandomQueue(T[] values, Random r) {
		this.values = values;
		this.random = r;
	}

	public T next() {
		if (this.remainingSize == 0)
			this.remainingSize = this.values.length;
		int index = this.random.nextInt(this.remainingSize);
		T value = this.values[index];
		this.remainingSize--;
		this.values[index] = this.values[this.remainingSize];
		this.values[this.remainingSize] = value;
		return value;
	}

	public static <T> RandomQueue<T> of(T[] values, Random r) {
		return new RandomQueue(Arrays.copyOf(values, values.length), r);
	}

	public static <T> RandomQueue<T> of(List<T> values, Random r) {
		return new RandomQueue(values.toArray(new Object[0]), r);
	}
}
