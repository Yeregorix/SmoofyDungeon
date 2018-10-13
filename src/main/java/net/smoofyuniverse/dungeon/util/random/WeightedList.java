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

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class WeightedList<T> {
	private final T[] objects;
	private final double[] weights;
	private final double total;
	private final int size;

	private WeightedList(T[] objects, double[] weights, double total) {
		this.objects = objects;
		this.weights = weights;
		this.total = total;
		this.size = objects.length;
	}

	public int getSize() {
		return this.size;
	}

	public T get(Random r) {
		double d = r.nextDouble() * this.total;
		int i = -1;
		while (d >= 0) {
			i++;
			d -= this.weights[i];
		}
		return this.objects[i];
	}

	public static <T> Builder<T> builder() {
		return new Builder<>();
	}

	@SuppressWarnings("unchecked")
	public static <T> WeightedList<T> of(Iterable<Entry<T>> it) {
		List<Entry<T>> list = ImmutableList.sortedCopyOf(it);
		if (list.isEmpty())
			throw new IllegalArgumentException("Empty iterator");

		T[] objects = (T[]) new Object[list.size()];
		double[] weights = new double[list.size()];

		int i = 0;
		double total = 0;
		for (Entry<T> e : list) {
			objects[i] = e.object;
			weights[i] = e.weight;
			total += e.weight;
			i++;
		}

		return new WeightedList<>(objects, weights, total);
	}

	public static class Builder<T> {
		private List<Entry<T>> list = new ArrayList<>();

		private Builder() {}

		public Builder<T> reset() {
			this.list.clear();
			return this;
		}

		public Builder<T> add(T object, double weight) {
			this.list.add(new Entry<>(object, weight));
			return this;
		}

		public Builder<T> add(Entry<T> entry) {
			this.list.add(entry);
			return this;
		}

		public WeightedList<T> build() {
			return of(this.list);
		}
	}

	public static final class Entry<T> implements Comparable<Entry<T>> {
		public final T object;
		public final double weight;

		public Entry(T object, double weight) {
			if (Double.isNaN(weight) || weight <= 0)
				throw new IllegalArgumentException("Weight");
			this.object = object;
			this.weight = weight;
		}

		@Override
		public int compareTo(Entry<T> o) {
			return Double.compare(o.weight, this.weight);
		}
	}
}
