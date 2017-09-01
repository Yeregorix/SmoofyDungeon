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

import net.smoofyuniverse.dungeon.gen.populator.WrappedPopulator;
import net.smoofyuniverse.dungeon.gen.populator.decoration.*;
import net.smoofyuniverse.dungeon.gen.populator.spawner.*;
import net.smoofyuniverse.dungeon.gen.populator.structure.*;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.world.biome.BiomeGenerationSettings;
import org.spongepowered.api.world.biome.BiomeType;
import org.spongepowered.api.world.gen.Populator;
import org.spongepowered.api.world.gen.WorldGenerator;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;
import org.spongepowered.api.world.gen.populator.Forest;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.List;

public class DungeonWorldModifier implements WorldGeneratorModifier {
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
		worldGen.setBaseGenerationPopulator(new DungeonTerrainGenerator());
		worldGen.getGenerationPopulators().clear();

		for (BiomeType type : Sponge.getRegistry().getAllOf(BiomeType.class)) {
			BiomeGenerationSettings biome = worldGen.getBiomeSettings(type);

			// We already generated the ground cover in our base generator
			biome.getGroundCoverLayers().clear();

			// Prevent forest from generating in flagged chunks (like oasis)
			List<Populator> bPops = biome.getPopulators();
			for (int i = 0; i < bPops.size(); i++) {
				Populator pop = bPops.get(i);
				if (pop instanceof Forest)
					bPops.set(i, new WrappedPopulator(pop));
			}
		}

		List<Populator> pops = worldGen.getPopulators();

		Populator animalPop = null;
		if (animalPopulatorClass != null) {
			for (Populator pop : pops) {
				if (animalPopulatorClass.isInstance(pop)) {
					animalPop = pop;
					break;
				}
			}
		}

		pops.clear();

		// 1- CHUNKS
		// Structures
		pops.add(new OasisPopulator());
		pops.add(new HighRoomPopulator());
		pops.add(new PoolPopulator());
		pops.add(new SanctuaryPopulator());
		pops.add(new EntrancePopulator());
		pops.add(new RailPopulator());
		pops.add(new RuinsPopulator());
		pops.add(new SandPopulator());
		pops.add(new GravelPopulator());

		// 2- LAYERS
		// Spawners
		pops.add(new BossRoomHardPopulator());
		pops.add(new BossRoomInsanePopulator());

		// 3- ROOMS
		// Spawners
		pops.add(new RandomSpawnerPopulator());
		pops.add(new SilverfishBlockPopulator());
		pops.add(new CreeperRoomPopulator());
		pops.add(new BlazeRoomPopulator());
		pops.add(new BossRoomEasyPopulator());

		// Decorators
		pops.add(new BrokenWallPopulator());
		pops.add(new NetherrackPopulator());
		pops.add(new CoalOrePopulator());
		pops.add(new CrackedStonePopulator());
		pops.add(new MossPopulator());
		pops.add(new SoulSandPopulator());
		pops.add(new SkullPopulator());
		pops.add(new SlabPopulator());
		pops.add(new ChestPopulator());
		pops.add(new LadderPopulator());
		pops.add(new VinePopulator());
		pops.add(new GravePopulator());
		pops.add(new PumpkinPopulator());
		pops.add(new LavaInWallPopulator());
		pops.add(new WaterInWallPopulator());
		pops.add(new WebPopulator());
		pops.add(new LanternPopulator());
		pops.add(new TorchPopulator());

		// 4- OTHERS
		pops.add(new ExplosionPopulator());
		if (animalPop != null)
			pops.add(new WrappedPopulator(animalPop));
	}

	static {
		try {
			animalPopulatorClass = Class.forName("org.spongepowered.common.world.gen.populators.AnimalPopulator");
		} catch (Exception e) {
			animalPopulatorClass = null;
		}
	}
}
