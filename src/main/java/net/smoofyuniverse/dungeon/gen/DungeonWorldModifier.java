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
import net.smoofyuniverse.dungeon.SmoofyDungeon;
import net.smoofyuniverse.dungeon.config.world.WorldConfig;
import net.smoofyuniverse.dungeon.gen.offset.GenerationPopulatorAdapter;
import net.smoofyuniverse.dungeon.gen.offset.VariableAmountAdapter;
import net.smoofyuniverse.dungeon.gen.populator.DungeonParentPopulator;
import net.smoofyuniverse.dungeon.gen.populator.core.DungeonPopulator;
import net.smoofyuniverse.dungeon.gen.populator.core.WrappedPopulator;
import net.smoofyuniverse.dungeon.gen.populator.decoration.*;
import net.smoofyuniverse.dungeon.gen.populator.spawner.*;
import net.smoofyuniverse.dungeon.gen.populator.structure.*;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.world.biome.BiomeGenerationSettings;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.biome.GroundCoverLayer;
import org.spongepowered.api.world.gen.GenerationPopulator;
import org.spongepowered.api.world.gen.Populator;
import org.spongepowered.api.world.gen.WorldGenerator;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;
import org.spongepowered.api.world.gen.populator.Forest;
import org.spongepowered.api.world.gen.populator.Ore;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DungeonWorldModifier implements WorldGeneratorModifier {
	public static final List<DungeonPopulator> POPULATORS;
	private static final List<String> classesBlacklist = new ArrayList<>();

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
			set = WorldConfig.of(name).getPopulators();
		} catch (Exception e) {
			SmoofyDungeon.LOGGER.error("Failed to load configuration for world '" + name + "'", e);
			set = WorldConfig.POPULATORS;
		}

		DungeonTerrainGenerator dungeonGenerator = new DungeonTerrainGenerator(worldGen.getBaseGenerationPopulator(), 40, 8);
		GroundCoverLayerPopulator groundCoverPopulator = new GroundCoverLayerPopulator();
		GenerationPopulatorAdapter genPopulatorAdapter = new GenerationPopulatorAdapter(dungeonGenerator.surfaceOffsetY, dungeonGenerator.surfaceMinY);
		DungeonParentPopulator dungeonPopulator = new DungeonParentPopulator(dungeonGenerator.layersCount);

		// 1: BaseGenerationPopulator
		worldGen.setBaseGenerationPopulator(dungeonGenerator);

		genPopulatorAdapter.getPopulators().add(groundCoverPopulator);

		// 3: GenerationPopulators
		List<GenerationPopulator> genPops = worldGen.getGenerationPopulators();
		for (GenerationPopulator pop : genPops) {
			if (!isBlacklisted(pop.getClass().getSimpleName()))
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
			if (isBlacklisted(it.next().getClass().getSimpleName()))
				it.remove();
		}

		for (DungeonPopulator pop : POPULATORS) {
			if (set.contains(pop.getName()))
				dungeonPopulator.getGlobalPopulators().add(pop);
		}

		pops.add(0, dungeonPopulator);
	}

	private static boolean isBlacklisted(String className) {
		for (String seq : classesBlacklist) {
			if (className.contains(seq))
				return true;
		}
		return false;
	}

	static {
		classesBlacklist.add("Stronghold");
		classesBlacklist.add("OceanMonument");
		classesBlacklist.add("Mineshaft");
		classesBlacklist.add("Dungeons");

		ImmutableList.Builder<DungeonPopulator> b = ImmutableList.builder();

		// Structures
		b.add(new OasisPopulator());
		b.add(new HighRoomPopulator());
		b.add(new FurnaceRoomPopulator());
		b.add(new ArmoryRoomPopulator());
		b.add(new CastleRoomPopulator());
		b.add(new SanctuaryPopulator());
		b.add(new LavaPoolPopulator());
		b.add(new WaterPoolPopulator());
		b.add(new EntrancePopulator());
		b.add(new StoneRoomPopulator());
		b.add(new LibraryPopulator());
		b.add(new RailPopulator());
		b.add(new RuinPopulator());
		b.add(new SandPopulator());
		b.add(new GravelPopulator());
		b.add(new StairsPopulator());
		b.add(new StrutPopulator());

		// Spawners
		b.add(new BossRoomHardPopulator());
		b.add(new BossRoomInsanePopulator());
		b.add(new SimpleSpawnerPopulator());
		b.add(new CeilingSpawnerPopulator());
		b.add(new SilverfishBlockPopulator());
		b.add(new CreeperRoomPopulator());
		b.add(new BlazeRoomPopulator());
		b.add(new BossRoomEasyPopulator());

		// Decorators
		b.add(new RedstonePopulator());
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
		b.add(new IronBarsPopulator());
		b.add(new LavaInWallPopulator());
		b.add(new WaterInWallPopulator());
		b.add(new WebPopulator());
		b.add(new LanternPopulator());
		b.add(new TorchPopulator());
		b.add(new ExplosionPopulator());
		b.add(new RiftPopulator());

		POPULATORS = b.build();
	}
}
