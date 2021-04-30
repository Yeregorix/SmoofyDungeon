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

package net.smoofyuniverse.dungeon;

import com.flowpowered.math.vector.Vector3i;
import com.google.inject.Inject;
import net.smoofyuniverse.dungeon.command.ConfigCommand;
import net.smoofyuniverse.dungeon.gen.DungeonWorldModifier;
import net.smoofyuniverse.ore.update.UpdateChecker;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.world.extent.BlockVolume;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Plugin(id = "smoofydungeon", name = "SmoofyDungeon", version = "1.2.6", authors = "Yeregorix", description = "An advanced dungeon generator")
public final class SmoofyDungeon {
	public static final Logger LOGGER = LoggerFactory.getLogger("SmoofyDungeon");
	public static final Vector3i CHUNK_SIZE = new Vector3i(16, 256, 16);

	private static SmoofyDungeon instance;

	@Inject
	private Game game;
	@Inject
	@ConfigDir(sharedRoot = false)
	private Path configDir;
	@Inject
	private PluginContainer container;

	private Path worldConfigsDir;

	public SmoofyDungeon() {
		if (instance != null)
			throw new IllegalStateException();
		instance = this;
	}

	@Listener
	public void onGamePreInit(GamePreInitializationEvent e) {
		this.worldConfigsDir = this.configDir.resolve("worlds");
		try {
			Files.createDirectories(this.worldConfigsDir);
		} catch (IOException ignored) {
		}

		this.game.getEventManager().registerListeners(this, new UpdateChecker(LOGGER, this.container,
				createConfigLoader(this.configDir.resolve("update.conf")), "Yeregorix", "SmoofyDungeon"));
	}

	@Listener
	public void onServerStarting(GameStartingServerEvent e) {
		this.game.getCommandManager().register(this, ConfigCommand.createMainSpec("smoofydungeon.config"), "sdcfg", "sdconfig", "smoofydungeonconfig");
	}

	public ConfigurationLoader<CommentedConfigurationNode> createConfigLoader(Path file) {
		return HoconConfigurationLoader.builder().setPath(file).build();
	}

	@Listener
	public void onServerStarted(GameStartedServerEvent e) {
		LOGGER.info("SmoofyDungeon " + this.container.getVersion().orElse("?") + " was loaded successfully.");
	}

	@Listener
	public void onRegister(GameRegistryEvent.Register<WorldGeneratorModifier> e) {
		e.register(new DungeonWorldModifier());
	}

	public Path getWorldConfigsDirectory() {
		return this.worldConfigsDir;
	}

	public PluginContainer getContainer() {
		return this.container;
	}

	public static SmoofyDungeon get() {
		if (instance == null)
			throw new IllegalStateException("Instance not available");
		return instance;
	}

	public static void validateChunkSize(BlockVolume volume) {
		if (!volume.getBlockSize().equals(SmoofyDungeon.CHUNK_SIZE))
			throw new UnsupportedOperationException();
	}
}
