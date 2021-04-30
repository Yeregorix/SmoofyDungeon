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

package net.smoofyuniverse.dungeon.config.world;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import net.smoofyuniverse.dungeon.SmoofyDungeon;
import net.smoofyuniverse.dungeon.gen.populator.api.DungeonPopulator;
import net.smoofyuniverse.dungeon.gen.populator.api.DungeonPopulators;
import net.smoofyuniverse.dungeon.util.IOUtil;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.function.UnaryOperator;

import static net.smoofyuniverse.dungeon.util.MathUtil.clamp;

public final class WorldConfig {
	public static final int CURRENT_VERSION = 2, MINIMUM_VERSION = 1;
	public static final WorldConfig DEFAULT_CONFIG = new WorldConfig(DungeonPopulators.all(), 8);

	public final Set<DungeonPopulator> populators;
	public final int layersCount;

	public WorldConfig(Collection<DungeonPopulator> populators, int layersCount) {
		if (populators == null)
			throw new IllegalArgumentException("populators");
		if (layersCount < 6 || layersCount > 30)
			throw new IllegalArgumentException("layersCount");

		this.populators = ImmutableSet.copyOf(populators);
		this.layersCount = layersCount;
	}

	public void save(String worldName) throws IOException {
		save(SmoofyDungeon.get().createConfigLoader(getConfigFile(worldName)));
	}

	private void save(ConfigurationLoader<CommentedConfigurationNode> loader) throws IOException {
		ConfigurationNode node = loader.createEmptyNode();

		node.getNode("Version").setValue(CURRENT_VERSION);
		node.getNode("Populators").setValue(DungeonPopulators.toStringList(this.populators));
		node.getNode("Layers").setValue(this.layersCount);

		loader.save(node);
	}

	private static Path getConfigFile(String worldName) {
		return SmoofyDungeon.get().getWorldConfigsDirectory().resolve(worldName.toLowerCase() + ".conf");
	}

	public static boolean exists(String worldName) {
		return Files.exists(getConfigFile(worldName));
	}

	public static boolean delete(String worldName) throws IOException {
		return Files.deleteIfExists(getConfigFile(worldName));
	}

	public static WorldConfig load(String worldName) throws IOException, ObjectMappingException {
		Path file = getConfigFile(worldName);
		ConfigurationLoader<CommentedConfigurationNode> loader = SmoofyDungeon.get().createConfigLoader(file);
		CommentedConfigurationNode node = loader.load();

		int version = node.getNode("Version").getInt();
		if ((version > CURRENT_VERSION || version < MINIMUM_VERSION) && IOUtil.backupFile(file)) {
			SmoofyDungeon.LOGGER.info("Your config version is not supported. A new one will be generated.");
			DEFAULT_CONFIG.save(loader);
			return DEFAULT_CONFIG;
		}

		List<String> list = node.getNode("Populators").getList(TypeToken.of(String.class));

		if (version == 1)
			update(list, n -> n.equals("random_spawner") ? "simple_spawner" : null);

		Collection<DungeonPopulator> pops;
		if (list.isEmpty())
			pops = DungeonPopulators.all();
		else
			pops = DungeonPopulators.fromStringList(list);

		int layersCount = clamp(node.getNode("Layers").getInt(8), 6, 30);

		WorldConfig config = new WorldConfig(pops, layersCount);
		config.save(loader);
		return config;
	}

	private static void update(List<String> list, UnaryOperator<String> operator) {
		ListIterator<String> it = list.listIterator();
		while (it.hasNext()) {
			String r = operator.apply(it.next());
			if (r != null)
				it.set(r);
		}
	}
}
