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

package net.smoofyuniverse.dungeon.command;

import net.smoofyuniverse.dungeon.config.WorldConfig;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.util.*;

import static net.smoofyuniverse.dungeon.util.Texts.*;

public class ConfigCommand {
	private static final Text world = Text.of("world"), populator = Text.of("populator");
	private static final Map<String, String> choices = new HashMap<>();

	public static CommandSpec createMainSpec(String perm) {
		return CommandSpec.builder()
				.description(Text.of("Allows you to change the configuration of a dungeon world, even if it does not exist yet."))
				.permission(perm)
				.child(createInfoSpec(), "info")
				.child(createEnableSpec(), "enable")
				.child(createDisableSpec(), "disable")
				.child(createResetSpec(), "reset")
				.build();
	}

	public static CommandSpec createInfoSpec() {
		return CommandSpec.builder()
				.arguments(GenericArguments.remainingJoinedStrings(world))
				.executor((cs, args) -> {
					WorldConfig cfg = WorldConfig.of(args.<String>getOne(world).get());
					cs.sendMessage(Text.join(gold("World"), Text.of(": " + cfg.getWorldName())));

					if (cfg.exists()) {
						List<Text> texts = new ArrayList<>();
						for (String pop : getPopulators(cfg))
							texts.add(aqua(pop));

						cs.sendMessage(Text.join(gold("Populators"), Text.of(" ("), gray(Integer.toString(texts.size())), Text.of("): ["), Text.joinWith(Text.of(", "), texts), Text.of("]")));
					} else {
						cs.sendMessage(Text.join(gold("Populators"), Text.of(": "), gray("[Configuration not initialized]")));
					}

					return CommandResult.empty();
				}).build();
	}

	public static CommandSpec createEnableSpec() {
		return CommandSpec.builder()
				.arguments(GenericArguments.choices(populator, choices), GenericArguments.remainingJoinedStrings(world))
				.executor((cs, args) -> {
					WorldConfig cfg = WorldConfig.of(args.<String>getOne(world).get());

					if (cfg.exists()) {
						Set<String> pops = getPopulators(cfg);
						String pop = args.<String>getOne(populator).get();

						if (pops.add(pop)) {
							setPopulators(cfg, pops);
							cs.sendMessage(green("Populator '" + pop + "' has been enabled."));
						} else {
							cs.sendMessage(red("Populator '" + pop + "' is already enabled."));
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
					WorldConfig cfg = WorldConfig.of(args.<String>getOne(world).get());

					Set<String> pops = cfg.exists() ? new LinkedHashSet<>(WorldConfig.POPULATORS) : getPopulators(cfg);
					String pop = args.<String>getOne(populator).get();

					if (pops.remove(pop)) {
						if (!cfg.exists())
							cs.sendMessage(green("Initializing a new configuration for world '" + cfg.getWorldName() + "' .."));
						setPopulators(cfg, pops);
						cs.sendMessage(green("Populator '" + pop + "' has been disabled."));
					} else {
						cs.sendMessage(red("Populator '" + pop + "' is already disabled."));
					}

					return CommandResult.empty();
				}).build();
	}

	public static CommandSpec createResetSpec() {
		return CommandSpec.builder()
				.arguments(GenericArguments.remainingJoinedStrings(world))
				.executor((cs, args) -> {
					WorldConfig cfg = WorldConfig.of(args.<String>getOne(world).get());

					if (delete(cfg)) {
						cs.sendMessage(green("Configuration for world '" + cfg.getWorldName() + "' has been deleted."));
					} else {
						cs.sendMessage(red("Configuration for world '" + cfg.getWorldName() + "' is not initialized."));
					}

					return CommandResult.empty();
				}).build();
	}

	private static Set<String> getPopulators(WorldConfig cfg) throws CommandException {
		try {
			return cfg.getPopulators();
		} catch (IOException e) {
			throw new CommandException(red("Failed to load configuration for world '" + cfg.getWorldName() + "'"), e);
		}
	}

	private static void setPopulators(WorldConfig cfg, Set<String> pops) throws CommandException {
		try {
			cfg.setPopulators(pops);
		} catch (IOException e) {
			throw new CommandException(red("Failed to save configuration for world '" + cfg.getWorldName() + "'"), e);
		}
	}

	private static boolean delete(WorldConfig cfg) throws CommandException {
		try {
			return cfg.delete();
		} catch (IOException e) {
			throw new CommandException(red("Failed to delete configuration for world '" + cfg.getWorldName() + "'"), e);
		}
	}

	static {
		for (String pop : WorldConfig.POPULATORS)
			choices.put(pop, pop);
	}
}
