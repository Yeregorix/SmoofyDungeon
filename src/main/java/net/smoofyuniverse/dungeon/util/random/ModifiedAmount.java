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

package net.smoofyuniverse.dungeon.util.random;

import org.spongepowered.api.util.weighted.VariableAmount;

import java.util.Random;

public final class ModifiedAmount implements VariableAmount {
	public final VariableAmount amount;
	public final double offset, factor;

	public ModifiedAmount(VariableAmount amount, double offset, double factor) {
		this.amount = amount;
		this.offset = offset;
		this.factor = factor;
	}

	@Override
	public double getAmount(Random rand) {
		return this.amount.getAmount(rand) * this.factor + this.offset;
	}

	@Override
	public int getFlooredAmount(Random rand) {
		double valueD = getAmount(rand);
		int valueI = (int) valueD;

		double dif = valueD - (double) valueI;
		if (dif != 0f && rand.nextDouble() < dif)
			valueI++;

		return valueI;
	}
}
