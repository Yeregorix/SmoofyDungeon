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

package net.smoofyuniverse.dungeon.gen;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import net.smoofyuniverse.dungeon.SmoofyDungeon;
import net.smoofyuniverse.dungeon.gen.populator.ChunkPopulator;
import net.smoofyuniverse.dungeon.gen.populator.WrappedPopulator;
import net.smoofyuniverse.dungeon.gen.populator.decoration.*;
import net.smoofyuniverse.dungeon.gen.populator.spawner.*;
import net.smoofyuniverse.dungeon.gen.populator.structure.*;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.world.biome.BiomeGenerationSettings;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.gen.Populator;
import org.spongepowered.api.world.gen.WorldGenerator;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;
import org.spongepowered.api.world.gen.populator.Forest;
import org.spongepowered.api.world.storage.WorldProperties;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DungeonWorldModifier implements WorldGeneratorModifier {
	public static final int CURRENT_CONFIG_VERSION = 1, MINIMUM_CONFIG_VERSION = 1;
	public static final List<ChunkPopulator> POPULATORS;

	private static final Set<String> allConfigPopulators;
	private static Class<?> animalPopulatorClass;

	@Override
	public String getId() {
		return "smoofydungeon:default";
	}

	@Override
	public String getName() {
		return "SmoofyDungeon Default";
	}

	@Override
	public void modifyWorldGenerator(WorldProperties world, DataContainer settings, WorldGenerator worldGen) {
		String name = world.getWorldName();
		SmoofyDungeon.LOGGER.info("Loading configuration for world " + name + " ..");

		Set<String> set;
		try {
			set = loadConfig(name);
		} catch (Exception e) {
			SmoofyDungeon.LOGGER.error("Failed to load configuration for world " + name, e);
			set = allConfigPopulators;
		}

		worldGen.setBaseGenerationPopulator(new DungeonTerrainGenerator());
		worldGen.getGenerationPopulators().clear();

		for (BiomeType type : Sponge.getRegistry().getAllOf(BiomeType.class)) {
			BiomeGenerationSettings biome = worldGen.getBiomeSettings(type);

			// We already generated the ground cover in our base generator
			biome.getGroundCoverLayers().clear();

			if (set.contains("forest")) {
				// Prevent forest from generating in flagged chunks (like oasis)
				List<Populator> bPops = biome.getPopulators();
				for (int i = 0; i < bPops.size(); i++) {
					Populator pop = bPops.get(i);
					if (pop instanceof Forest)
						bPops.set(i, new WrappedPopulator(pop));
				}
			} else {
				biome.getPopulators().removeIf(pop -> pop instanceof Forest);
			}
		}

		List<Populator> pops = worldGen.getPopulators();

		Populator animalPop = null;
		if (set.contains("animal") && animalPopulatorClass != null) {
			for (Populator pop : pops) {
				if (animalPopulatorClass.isInstance(pop)) {
					animalPop = pop;
					break;
				}
			}
		}

		pops.clear();

		for (ChunkPopulator pop : POPULATORS) {
			if (set.contains(pop.getName()))
				pops.add(pop);
		}

		if (animalPop != null)
			pops.add(new WrappedPopulator(animalPop));
	}

	public static Set<String> loadConfig(String worldName) throws IOException, ObjectMappingException {
		Path file = SmoofyDungeon.get().getWorldConfigsDirectory().resolve(worldName + ".conf");
		ConfigurationLoader<CommentedConfigurationNode> loader = SmoofyDungeon.get().createConfigLoader(file);

		CommentedConfigurationNode root = loader.load();
		int version = root.getNode("Version").getInt();
		if ((version > CURRENT_CONFIG_VERSION || version < MINIMUM_CONFIG_VERSION) && SmoofyDungeon.get().backupFile(file)) {
			SmoofyDungeon.LOGGER.info("Your config version is not supported. A new one will be generated.");
			root = loader.createEmptyNode();
		}

		ConfigurationNode cfgNode = root.getNode("Populators");
		List<String> list = cfgNode.getList(TypeToken.of(String.class));
		Set<String> set;

		if (list.isEmpty()) {
			set = new LinkedHashSet<>(allConfigPopulators);
		} else {
			set = new LinkedHashSet<>(list);
			set.retainAll(allConfigPopulators);
		}

		version = CURRENT_CONFIG_VERSION;
		root.getNode("Version").setValue(version);
		cfgNode.setValue(set);
		loader.save(root);

		return set;
	}

	static {
		try {
			animalPopulatorClass = Class.forName("org.spongepowered.common.world.gen.populators.AnimalPopulator");
		} catch (Exception e) {
			animalPopulatorClass = null;
		}

		ImmutableList.Builder<ChunkPopulator> b = ImmutableList.builder();

		// Structures
		b.add(new OasisPopulator());
		b.add(new HighRoomPopulator());
		b.add(new FurnaceRoomPopulator());
		b.add(new SanctuaryPopulator());
		b.add(new StoneRoomPopulator());
		b.add(new LavaPoolPopulator());
		b.add(new WaterPoolPopulator());
		b.add(new EntrancePopulator());
		b.add(new RailPopulator());
		b.add(new RuinPopulator());
		b.add(new SandPopulator());
		b.add(new GravelPopulator());
		b.add(new StairsPopulator());
		b.add(new StrutPopulator());

		// Spawners
		b.add(new BossRoomHardPopulator());
		b.add(new BossRoomInsanePopulator());
		b.add(new RandomSpawnerPopulator());
		b.add(new SilverfishBlockPopulator());
		b.add(new CreeperRoomPopulator());
		b.add(new BlazeRoomPopulator());
		b.add(new BossRoomEasyPopulator());

		// Decorators
		b.add(new WaterWellPopulator());
		b.add(new BrokenWallPopulator());
		b.add(new NetherrackPopulator());
		b.add(new CoalOrePopulator());
		b.add(new CrackedStonePopulator());
		b.add(new MossPopulator());
		b.add(new SoulSandPopulator());
		b.add(new SkullPopulator());
		b.add(new SlabPopulator());
		b.add(new ChestPopulator());
		b.add(new LadderPopulator());
		b.add(new VinePopulator());
		b.add(new GravePopulator());
		b.add(new PumpkinPopulator());
		b.add(new LavaInWallPopulator());
		b.add(new WaterInWallPopulator());
		b.add(new WebPopulator());
		b.add(new LanternPopulator());
		b.add(new TorchPopulator());
		b.add(new ExplosionPopulator());

		POPULATORS = b.build();

		allConfigPopulators = new LinkedHashSet<>();
		for (ChunkPopulator pop : POPULATORS)
			allConfigPopulators.add(pop.getName());
		allConfigPopulators.add("forest");
		allConfigPopulators.add("animal");
	}
}
