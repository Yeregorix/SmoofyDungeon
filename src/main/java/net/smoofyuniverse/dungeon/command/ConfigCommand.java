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

package net.smoofyuniverse.dungeon.command;

import net.smoofyuniverse.dungeon.config.world.WorldConfig;
import net.smoofyuniverse.dungeon.gen.populator.api.DungeonPopulator;
import net.smoofyuniverse.dungeon.gen.populator.api.DungeonPopulators;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.smoofyuniverse.dungeon.util.Texts.*;

public class ConfigCommand {
	private static final Text world = Text.of("world"), populator = Text.of("populator"), layers = Text.of("layers");
	private static final Map<String, DungeonPopulator> choices = new HashMap<>();

	public static CommandSpec createMainSpec(String perm) {
		return CommandSpec.builder()
				.description(Text.of("Allows you to change the configuration of a dungeon world, even if it does not exist yet."))
				.permission(perm)
				.child(createInfoSpec(), "info")
				.child(createEnableSpec(), "enable")
				.child(createDisableSpec(), "disable")
				.child(createLayersSpec(), "layers")
				.child(createResetSpec(), "reset")
				.build();
	}

	public static CommandSpec createInfoSpec() {
		return CommandSpec.builder()
				.arguments(GenericArguments.remainingJoinedStrings(world))
				.executor((cs, args) -> {
					String worldName = args.<String>getOne(world).get().toLowerCase();

					if (WorldConfig.exists(worldName)) {
						WorldConfig config = load(worldName);

						List<Text> texts = new ArrayList<>();
						for (DungeonPopulator pop : config.populators)
							texts.add(aqua(pop.getName()));

						cs.sendMessage(Text.join(gold("World"), Text.of(": " + worldName)));
						cs.sendMessage(Text.join(gold("Loaded"), Text.of(": "), isWorldLoaded(worldName) ? green("Yes") : red("No")));
						cs.sendMessage(Text.join(gold("Layers"), Text.of(": " + config.layersCount)));
						cs.sendMessage(Text.join(gold("Populators"), Text.of(" ("), gray(Integer.toString(texts.size())), Text.of("): ["), Text.joinWith(Text.of(", "), texts), Text.of("]")));
					} else {
						cs.sendMessage(red("The configuration for world '" + worldName + "' is not initialized."));
					}

					return CommandResult.empty();
				}).build();
	}

	public static CommandSpec createEnableSpec() {
		return CommandSpec.builder()
				.arguments(GenericArguments.choices(populator, choices), GenericArguments.remainingJoinedStrings(world))
				.executor((cs, args) -> {
					String worldName = args.<String>getOne(world).get().toLowerCase();

					if (WorldConfig.exists(worldName)) {
						WorldConfig config = load(worldName);
						List<DungeonPopulator> pops = new ArrayList<>(config.populators);
						DungeonPopulator pop = args.<DungeonPopulator>getOne(populator).get();

						if (pops.contains(pop)) {
							cs.sendMessage(red("Populator '" + pop.getName() + "' is already enabled in world '" + worldName + "'."));
						} else {
							pops.add(pop);
							save(new WorldConfig(pops, config.layersCount), worldName);
							cs.sendMessage(green("Populator '" + pop.getName() + "' has been enabled in world '" + worldName + "'."));
							warnIfWorldLoaded(cs, worldName);
						}
					} else {
						cs.sendMessage(red("All populators are already enabled because the configuration is not initialized."));
					}

					return CommandResult.empty();
				}).build();
	}

	public static CommandSpec createDisableSpec() {
		return CommandSpec.builder()
				.arguments(GenericArguments.choices(populator, choices), GenericArguments.remainingJoinedStrings(world))
				.executor((cs, args) -> {
					String worldName = args.<String>getOne(world).get().toLowerCase();
					boolean exists = WorldConfig.exists(worldName);

					WorldConfig config = exists ? load(worldName) : WorldConfig.DEFAULT_CONFIG;
					List<DungeonPopulator> pops = new ArrayList<>(config.populators);
					DungeonPopulator pop = args.<DungeonPopulator>getOne(populator).get();

					if (pops.remove(pop)) {
						if (!exists)
							cs.sendMessage(green("Initializing a new configuration for world '" + worldName + "' .."));

						save(new WorldConfig(pops, config.layersCount), worldName);
						cs.sendMessage(green("Populator '" + pop.getName() + "' has been disabled in world '" + worldName + "'."));
						warnIfWorldLoaded(cs, worldName);
					} else {
						cs.sendMessage(red("Populator '" + pop.getName() + "' is already disabled in world '" + worldName + "'."));
					}

					return CommandResult.empty();
				}).build();
	}

	public static CommandSpec createLayersSpec() {
		return CommandSpec.builder()
				.arguments(GenericArguments.integer(layers), GenericArguments.remainingJoinedStrings(world))
				.executor((cs, args) -> {
					String worldName = args.<String>getOne(world).get().toLowerCase();
					boolean exists = WorldConfig.exists(worldName);

					WorldConfig config = exists ? load(worldName) : WorldConfig.DEFAULT_CONFIG;
					int layersCount = args.<Integer>getOne(layers).get();

					if (layersCount < 6)
						throw new CommandException(red("A dungeon world must contains at least 6 layers."));
					if (layersCount > 30)
						throw new CommandException(red("A dungeon world can contains up to 30 layers."));

					if (config.layersCount == layersCount) {
						cs.sendMessage(red("The world '" + worldName + "' already has " + layersCount + " layers."));
					} else {
						if (!exists)
							cs.sendMessage(green("Initializing a new configuration for world '" + worldName + "' .."));

						save(new WorldConfig(config.populators, layersCount), worldName);
						cs.sendMessage(green("The world '" + worldName + "' now has " + layersCount + " layers."));
						warnIfWorldLoaded(cs, worldName);
					}

					return CommandResult.empty();
				}).build();
	}

	public static CommandSpec createResetSpec() {
		return CommandSpec.builder()
				.arguments(GenericArguments.remainingJoinedStrings(world))
				.executor((cs, args) -> {
					String worldName = args.<String>getOne(world).get().toLowerCase();

					if (delete(worldName)) {
						cs.sendMessage(green("Configuration for world '" + worldName + "' has been deleted."));
						warnIfWorldLoaded(cs, worldName);
					} else {
						cs.sendMessage(red("Configuration for world '" + worldName + "' is not initialized."));
					}

					return CommandResult.empty();
				}).build();
	}

	private static WorldConfig load(String worldName) throws CommandException {
		try {
			return WorldConfig.load(worldName);
		} catch (Exception e) {
			throw new CommandException(red("Failed to load configuration for world '" + worldName + "'"), e);
		}
	}

	private static void save(WorldConfig config, String worldName) throws CommandException {
		try {
			config.save(worldName);
		} catch (Exception e) {
			throw new CommandException(red("Failed to save configuration for world '" + worldName + "'"), e);
		}
	}

	private static boolean delete(String worldName) throws CommandException {
		try {
			return WorldConfig.delete(worldName);
		} catch (Exception e) {
			throw new CommandException(red("Failed to delete configuration for world '" + worldName + "'"), e);
		}
	}

	private static void warnIfWorldLoaded(CommandSource cs, String worldName) {
		if (isWorldLoaded(worldName))
			cs.sendMessage(red("This world is already loaded. You must reload it to apply the changes."));
	}

	// case-insensitive
	private static boolean isWorldLoaded(String worldName) {
		for (World w : Sponge.getServer().getWorlds()) {
			if (w.getName().toLowerCase().equals(worldName))
				return true;
		}
		return false;
	}

	static {
		for (DungeonPopulator pop : DungeonPopulators.all())
			choices.put(pop.getName().toLowerCase(), pop);
	}
}
