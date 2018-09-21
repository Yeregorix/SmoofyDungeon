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

package net.smoofyuniverse.dungeon.gen.populator.api;

import net.smoofyuniverse.dungeon.gen.populator.decoration.*;
import net.smoofyuniverse.dungeon.gen.populator.spawner.*;
import net.smoofyuniverse.dungeon.gen.populator.structure.*;

import java.util.*;

public class DungeonPopulators {
	private static final Map<String, DungeonPopulator> map = new LinkedHashMap<>();
	private static final Collection<DungeonPopulator> unmodValues = Collections.unmodifiableCollection(map.values());

	public static void register(DungeonPopulator populator) {
		String name = populator.getName().toLowerCase();
		if (map.containsKey(name))
			throw new IllegalArgumentException("A populator is already registered with the same name");
		map.put(name, populator);
	}

	public static Optional<DungeonPopulator> get(String name) {
		return Optional.ofNullable(map.get(name.toLowerCase()));
	}

	public static Optional<DungeonPopulator> remove(String name) {
		return Optional.ofNullable(map.remove(name.toLowerCase()));
	}

	public static boolean contains(String name) {
		return map.containsKey(name.toLowerCase());
	}

	public static Collection<DungeonPopulator> all() {
		return unmodValues;
	}

	public static List<String> toStringList(Collection<DungeonPopulator> col) {
		List<String> l = new ArrayList<>(col.size());
		for (DungeonPopulator pop : col)
			l.add(pop.getName().toLowerCase());
		return l;
	}

	public static List<DungeonPopulator> fromStringList(Collection<String> col) {
		List<DungeonPopulator> l = new ArrayList<>(col.size());
		for (String name : col) {
			DungeonPopulator pop = map.get(name.toLowerCase());
			if (pop != null)
				l.add(pop);
		}
		return l;
	}

	private static void add(DungeonPopulator populator) {
		map.put(populator.getName().toLowerCase(), populator);
	}

	static {
		// Structures
		add(new OasisPopulator());
		add(new HighRoomPopulator());
		add(new FurnaceRoomPopulator());
		add(new ArmoryRoomPopulator());
		add(new CastleRoomPopulator());
		add(new EndRoomPopulator());
		add(new SanctuaryPopulator());
		add(new LavaPoolPopulator());
		add(new WaterPoolPopulator());
		add(new EntrancePopulator());
		add(new StoneRoomPopulator());
		add(new LibraryPopulator());
		add(new RailPopulator());
		add(new RuinPopulator());
		add(new SandPopulator());
		add(new GravelPopulator());
		add(new StairsPopulator());
		add(new StrutPopulator());

		// Spawners
		add(new BossRoomHardPopulator());
		add(new BossRoomInsanePopulator());
		add(new SimpleSpawnerPopulator());
		add(new CeilingSpawnerPopulator());
		add(new SilverfishBlockPopulator());
		add(new CreeperRoomPopulator());
		add(new BlazeRoomPopulator());
		add(new BossRoomEasyPopulator());

		// Decorators
		add(new RedstonePopulator());
		add(new PressurePlateTrapPopulator());
		add(new WaterWellPopulator());
		add(new BrokenWallPopulator());
		add(new NetherrackPopulator());
		add(new CoalOrePopulator());
		add(new CrackedStonePopulator());
		add(new MossPopulator());
		add(new SoulSandPopulator());
		add(new SkullPopulator());
		add(new SlabPopulator());
		add(new ChestPopulator());
		add(new LadderPopulator());
		add(new VinePopulator());
		add(new GravePopulator());
		add(new PumpkinPopulator());
		add(new IronBarsPopulator());
		add(new LavaInWallPopulator());
		add(new WaterInWallPopulator());
		add(new WebPopulator());
		add(new LanternPopulator());
		add(new TorchPopulator());
		add(new ExplosionPopulator());
		add(new RiftPopulator());
	}
}
