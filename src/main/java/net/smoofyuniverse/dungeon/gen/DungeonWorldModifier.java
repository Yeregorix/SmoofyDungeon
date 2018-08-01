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
import net.smoofyuniverse.dungeon.gen.populator.DungeonParentPopulator;
import net.smoofyuniverse.dungeon.gen.populator.core.DungeonPopulator;
import net.smoofyuniverse.dungeon.gen.populator.core.WrappedPopulator;
import net.smoofyuniverse.dungeon.gen.populator.decoration.*;
import net.smoofyuniverse.dungeon.gen.populator.spawner.*;
import net.smoofyuniverse.dungeon.gen.populator.structure.*;
import net.smoofyuniverse.dungeon.util.random.ModifiedAmount;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.world.biome.BiomeGenerationSettings;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.gen.Populator;
import org.spongepowered.api.world.gen.WorldGenerator;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;
import org.spongepowered.api.world.gen.populator.Forest;
import org.spongepowered.api.world.gen.populator.Ore;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DungeonWorldModifier implements WorldGeneratorModifier {
	public static final List<DungeonPopulator> POPULATORS;

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
			set = WorldConfig.of(name).getPopulators();
		} catch (Exception e) {
			SmoofyDungeon.LOGGER.error("Failed to load configuration for world '" + name + "'", e);
			set = WorldConfig.POPULATORS;
		}

		worldGen.setBaseGenerationPopulator(new DungeonTerrainGenerator(30, 7));
		worldGen.getGenerationPopulators().clear();

		DungeonParentPopulator parent = new DungeonParentPopulator(30, 7);

		boolean keepForests = set.contains("forest"), keepOres = set.contains("ore_vein");
		for (BiomeType type : Sponge.getRegistry().getAllOf(BiomeType.class)) {
			BiomeGenerationSettings biome = worldGen.getBiomeSettings(type);

			// We already generated the ground cover in our base generator
			biome.getGroundCoverLayers().clear();

			Iterator<Populator> it = biome.getPopulators().iterator();
			while (it.hasNext()) {
				Populator pop = it.next();
				if (pop instanceof Ore) {
					if (keepOres) {
						Ore ore = (Ore) pop; // Adapt the parameters
						ore.setHeight(new ModifiedAmount(ore.getHeight(), 4d, 0.5d));
						ore.setDepositsPerChunk(new ModifiedAmount(ore.getDepositsPerChunk(), 0d, 0.5d));
					} else
						it.remove();
				} else if (pop instanceof Forest) {
					it.remove();
					if (keepForests) // Prevent forest from generating in flagged chunks (like oasis)
						parent.getBiomePopulators(type).add(new WrappedPopulator("forest", pop));
				}
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

		for (DungeonPopulator pop : POPULATORS) {
			if (set.contains(pop.getName()))
				parent.getGlobalPopulators().add(pop);
		}

		if (animalPop != null)
			parent.getGlobalPopulators().add(new WrappedPopulator("animal", animalPop));

		pops.add(parent);
	}

	static {
		try {
			animalPopulatorClass = Class.forName("org.spongepowered.common.world.gen.populators.AnimalPopulator");
		} catch (Exception e) {
			animalPopulatorClass = null;
		}

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
