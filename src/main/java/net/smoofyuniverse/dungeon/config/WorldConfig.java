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

package net.smoofyuniverse.dungeon.config;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import net.smoofyuniverse.dungeon.SmoofyDungeon;
import net.smoofyuniverse.dungeon.gen.DungeonWorldModifier;
import net.smoofyuniverse.dungeon.gen.populator.ChunkPopulator;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class WorldConfig {
	public static final int CURRENT_CONFIG_VERSION = 1, MINIMUM_CONFIG_VERSION = 1;
	public static final Set<String> POPULATORS;

	private static final Map<String, WorldConfig> worlds = new HashMap<>();

	private final String worldName;
	private final Path file;
	private final ConfigurationLoader<CommentedConfigurationNode> loader;

	private WorldConfig(String worldName) {
		this.worldName = worldName;
		this.file = SmoofyDungeon.get().getWorldConfigsDirectory().resolve(worldName + ".conf");
		this.loader = SmoofyDungeon.get().createConfigLoader(this.file);
	}

	public String getWorldName() {
		return this.worldName;
	}

	public boolean exists() {
		return Files.exists(this.file);
	}

	public boolean delete() throws IOException {
		return Files.deleteIfExists(this.file);
	}

	public Set<String> getPopulators() throws IOException {
		CommentedConfigurationNode root = this.loader.load();

		int version = root.getNode("Version").getInt();
		if ((version > CURRENT_CONFIG_VERSION || version < MINIMUM_CONFIG_VERSION) && SmoofyDungeon.get().backupFile(this.file)) {
			SmoofyDungeon.LOGGER.info("Your config version is not supported. A new one will be generated.");
			setPopulators(POPULATORS);
			return new LinkedHashSet<>(POPULATORS);
		}

		List<String> list;
		try {
			list = root.getNode("Populators").getList(TypeToken.of(String.class));
		} catch (ObjectMappingException e) {
			throw new IOException(e);
		}

		if (list.isEmpty()) {
			setPopulators(POPULATORS);
			return new LinkedHashSet<>(POPULATORS);
		}

		Set<String> set = new LinkedHashSet<>(list);
		set.retainAll(POPULATORS);

		if (set.size() != list.size())
			setPopulators(set);

		return set;
	}

	public void setPopulators(Set<String> set) throws IOException {
		CommentedConfigurationNode root = this.loader.createEmptyNode();

		root.getNode("Version").setValue(CURRENT_CONFIG_VERSION);
		root.getNode("Populators").setValue(set);

		this.loader.save(root);
	}

	public static WorldConfig of(String worldName) {
		worldName = worldName.toLowerCase();
		WorldConfig cfg = worlds.get(worldName);
		if (cfg == null) {
			cfg = new WorldConfig(worldName);
			worlds.put(worldName, cfg);
		}
		return cfg;
	}

	static {
		ImmutableSet.Builder<String> b = ImmutableSet.builder();

		for (ChunkPopulator pop : DungeonWorldModifier.POPULATORS)
			b.add(pop.getName());
		b.add("forest");
		b.add("animal");

		POPULATORS = b.build();
	}
}
