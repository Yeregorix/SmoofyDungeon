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

package net.smoofyuniverse.dungeon;

import com.google.inject.Inject;
import net.smoofyuniverse.dungeon.gen.DungeonWorldModifier;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;

@Plugin(id = "smoofydungeon", name = "SmoofyDungeon", version = "1.0.0", authors = "Yeregorix", description = "An advanced dungeon generator")
public final class SmoofyDungeon {
	private static SmoofyDungeon instance;

	public SmoofyDungeon() {
		if (instance != null)
			throw new IllegalStateException();
		instance = this;
	}

	@Inject
	private PluginContainer container;

	@Listener
	public void onRegister(GameRegistryEvent.Register<WorldGeneratorModifier> e) {
		e.register(new DungeonWorldModifier());
	}

	public PluginContainer getContainer() {
		return this.container;
	}

	public static SmoofyDungeon get() {
		if (instance == null)
			throw new IllegalStateException("Instance not available");
		return instance;
	}
}
