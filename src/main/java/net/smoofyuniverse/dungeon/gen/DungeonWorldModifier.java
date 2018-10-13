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

package net.smoofyuniverse.dungeon.gen;

import net.smoofyuniverse.dungeon.SmoofyDungeon;
import net.smoofyuniverse.dungeon.config.world.WorldConfig;
import net.smoofyuniverse.dungeon.gen.offset.GenerationPopulatorAdapter;
import net.smoofyuniverse.dungeon.gen.offset.VariableAmountAdapter;
import net.smoofyuniverse.dungeon.gen.populator.DungeonParentPopulator;
import net.smoofyuniverse.dungeon.gen.populator.core.WrappedPopulator;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.world.biome.BiomeGenerationSettings;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.biome.GroundCoverLayer;
import org.spongepowered.api.world.gen.*;
import org.spongepowered.api.world.gen.populator.Forest;
import org.spongepowered.api.world.gen.populator.Ore;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DungeonWorldModifier implements WorldGeneratorModifier {
	private static final List<PopulatorType> blacklist = new ArrayList<>();

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

		WorldConfig config;
		try {
			config = WorldConfig.load(name);
		} catch (Exception e) {
			SmoofyDungeon.LOGGER.error("Failed to load configuration for world '" + name + "'", e);
			config = WorldConfig.DEFAULT_CONFIG;
		}

		DungeonTerrainGenerator dungeonGenerator = new DungeonTerrainGenerator(worldGen.getBaseGenerationPopulator(), 40, config.layersCount);
		GroundCoverLayerPopulator groundCoverPopulator = new GroundCoverLayerPopulator();
		GenerationPopulatorAdapter genPopulatorAdapter = new GenerationPopulatorAdapter(dungeonGenerator.surfaceOffsetY, dungeonGenerator.surfaceMinY);
		DungeonParentPopulator dungeonPopulator = new DungeonParentPopulator(dungeonGenerator.layersCount);

		// 1: BaseGenerationPopulator
		worldGen.setBaseGenerationPopulator(dungeonGenerator);

		genPopulatorAdapter.getPopulators().add(groundCoverPopulator);

		// 3: GenerationPopulators
		List<GenerationPopulator> genPops = worldGen.getGenerationPopulators();
		for (GenerationPopulator pop : genPops) {
			if (!isBlacklisted(pop))
				genPopulatorAdapter.getPopulators().add(pop);
		}
		genPops.clear();
		genPops.add(genPopulatorAdapter);

		for (BiomeType biomeType : Sponge.getRegistry().getAllOf(BiomeType.class)) {
			BiomeGenerationSettings biomeSettings = worldGen.getBiomeSettings(biomeType);

			// 2: GroundCoverLayers (per biome)
			List<GroundCoverLayer> biomeLayers = biomeSettings.getGroundCoverLayers();
			groundCoverPopulator.getBiomeLayers(biomeType).addAll(biomeLayers);
			biomeLayers.clear();

			// 4: GenerationPopulators (per biome)
			List<GenerationPopulator> biomeGenPops = biomeSettings.getGenerationPopulators();
			if (!biomeGenPops.isEmpty()) {
				GenerationPopulatorAdapter biomeGenPopulatorAdapter = new GenerationPopulatorAdapter(dungeonGenerator.surfaceOffsetY, dungeonGenerator.surfaceMinY);
				biomeGenPopulatorAdapter.getPopulators().addAll(biomeGenPops);
				biomeGenPops.clear();
				biomeGenPops.add(biomeGenPopulatorAdapter);
			}

			// 6: Populators (per biome)
			Iterator<Populator> it = biomeSettings.getPopulators().iterator();
			while (it.hasNext()) {
				Populator pop = it.next();
				if (pop instanceof Ore) {
					Ore ore = (Ore) pop;
					ore.setHeight(new VariableAmountAdapter(ore.getHeight(), dungeonGenerator.surfaceOffsetY));
				} else if (pop instanceof Forest) {
					it.remove();
					dungeonPopulator.getBiomePopulators(biomeType).add(new WrappedPopulator("forest", pop));
				}
			}
		}

		// 5: Populators
		List<Populator> pops = worldGen.getPopulators();

		Iterator<Populator> it = pops.iterator();
		while (it.hasNext()) {
			if (isBlacklisted(it.next()))
				it.remove();
		}

		dungeonPopulator.getGlobalPopulators().addAll(config.populators);

		pops.add(0, dungeonPopulator);
	}

	public static boolean isBlacklisted(Object obj) {
		return obj instanceof Populator && blacklist.contains(((Populator) obj).getType());
	}

	static {
		GameRegistry registry = Sponge.getRegistry();
		registry.getType(PopulatorType.class, "minecraft:structure").ifPresent(blacklist::add);
		registry.getType(PopulatorType.class, "minecraft:dungeon").ifPresent(blacklist::add);
	}
}
