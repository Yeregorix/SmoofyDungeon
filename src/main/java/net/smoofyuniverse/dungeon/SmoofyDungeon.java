/*
 * Copyright (c) 2017-2019 Hugo Dupanloup (Yeregorix)
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
import net.smoofyuniverse.dungeon.config.global.GlobalConfig;
import net.smoofyuniverse.dungeon.event.PlayerEventListener;
import net.smoofyuniverse.dungeon.gen.DungeonWorldModifier;
import net.smoofyuniverse.dungeon.util.IOUtil;
import net.smoofyuniverse.ore.OreAPI;
import net.smoofyuniverse.ore.project.OreProject;
import net.smoofyuniverse.ore.project.OreVersion;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.GuiceObjectMapperFactory;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
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
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.extent.BlockVolume;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.max;
import static net.smoofyuniverse.dungeon.util.MathUtil.clamp;

@Plugin(id = "smoofydungeon", name = "SmoofyDungeon", version = "1.2.5", authors = "Yeregorix", description = "An advanced dungeon generator")
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
	@Inject
	private GuiceObjectMapperFactory factory;

	private ConfigurationOptions configOptions;
	private Path worldConfigsDir;

	private GlobalConfig.Immutable globalConfig;

	private OreAPI oreAPI;
	private OreProject oreProject;
	private Text[] updateMessages = new Text[0];

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
		this.configOptions = ConfigurationOptions.defaults().setObjectMapperFactory(this.factory);

		LOGGER.info("Loading global configuration ..");
		try {
			loadGlobalConfig();
		} catch (Exception ex) {
			LOGGER.error("Failed to load global configuration", ex);
		}

		this.game.getEventManager().registerListeners(this, new PlayerEventListener());
	}

	public void loadGlobalConfig() throws IOException, ObjectMappingException {
		if (this.globalConfig != null)
			throw new IllegalStateException("Config already loaded");

		Path file = this.configDir.resolve("global.conf");
		ConfigurationLoader<CommentedConfigurationNode> loader = createConfigLoader(file);

		CommentedConfigurationNode root = loader.load();
		int version = root.getNode("Version").getInt();
		if ((version > GlobalConfig.CURRENT_VERSION || version < GlobalConfig.MINIMUM_VERSION) && IOUtil.backupFile(file)) {
			LOGGER.info("Your global config version is not supported. A new one will be generated.");
			root = loader.createEmptyNode();
		}

		ConfigurationNode cfgNode = root.getNode("Config");
		GlobalConfig cfg = cfgNode.getValue(GlobalConfig.TOKEN, new GlobalConfig());

		cfg.updateCheck.repetitionInterval = max(cfg.updateCheck.repetitionInterval, 0);
		cfg.updateCheck.consoleDelay = clamp(cfg.updateCheck.consoleDelay, -1, 100);
		cfg.updateCheck.playerDelay = clamp(cfg.updateCheck.playerDelay, -1, 100);

		if (cfg.updateCheck.consoleDelay == -1 && cfg.updateCheck.playerDelay == -1)
			cfg.updateCheck.enabled = false;

		version = GlobalConfig.CURRENT_VERSION;
		root.getNode("Version").setValue(version);
		cfgNode.setValue(GlobalConfig.TOKEN, cfg);
		loader.save(root);

		this.globalConfig = cfg.toImmutable();
	}

	@Listener
	public void onServerStarting(GameStartingServerEvent e) {
		this.game.getCommandManager().register(this, ConfigCommand.createMainSpec("smoofydungeon.config"), "sdcfg", "sdconfig", "smoofydungeonconfig");
	}

	public ConfigurationLoader<CommentedConfigurationNode> createConfigLoader(Path file) {
		return HoconConfigurationLoader.builder().setPath(file).setDefaultOptions(this.configOptions).build();
	}

	@Listener
	public void onServerStarted(GameStartedServerEvent e) {
		LOGGER.info("SmoofyDungeon " + this.container.getVersion().orElse("?") + " was loaded successfully.");

		if (this.globalConfig.updateCheck.enabled) {
			this.oreAPI = new OreAPI();
			this.oreProject = new OreProject("smoofydungeon");
			this.oreProject.setNamespace("Yeregorix", "SmoofyDungeon");
			Task.builder().async().interval(this.globalConfig.updateCheck.repetitionInterval, TimeUnit.HOURS).execute(this::checkForUpdate).submit(this);
		}
	}

	@Listener
	public void onRegister(GameRegistryEvent.Register<WorldGeneratorModifier> e) {
		e.register(new DungeonWorldModifier());
	}

	public void checkForUpdate() {
		String version = this.container.getVersion().orElse(null);
		if (version == null)
			return;

		LOGGER.debug("Checking for update ..");

		OreVersion latestVersion = null;
		try {
			latestVersion = OreVersion.getLatest(this.oreProject.getVersions(this.oreAPI), v -> v.apiVersion.charAt(0) == '7').orElse(null);
		} catch (Exception e) {
			LOGGER.info("Failed to check for update", e);
		}

		if (latestVersion != null && !latestVersion.name.equals(version)) {
			Text msg1 = Text.join(Text.of("A new version of SmoofyDungeon is available: "),
					Text.builder(latestVersion.name).color(TextColors.AQUA).build(),
					Text.of(". You're currently using version: "),
					Text.builder(version).color(TextColors.AQUA).build(),
					Text.of("."));

			Text msg2;
			try {
				msg2 = Text.builder("Click here to open the download page.").color(TextColors.GOLD)
						.onClick(TextActions.openUrl(new URL(latestVersion.getPage()))).build();
			} catch (MalformedURLException e) {
				msg2 = null;
			}

			if (this.globalConfig.updateCheck.consoleDelay != -1) {
				Task.builder().delayTicks(this.globalConfig.updateCheck.consoleDelay)
						.execute(() -> this.game.getServer().getConsole().sendMessage(msg1)).submit(this);
			}

			if (this.globalConfig.updateCheck.playerDelay != -1)
				this.updateMessages = msg2 == null ? new Text[]{msg1} : new Text[]{msg1, msg2};
		}
	}

	public Path getWorldConfigsDirectory() {
		return this.worldConfigsDir;
	}

	public GlobalConfig.Immutable getGlobalConfig() {
		if (this.globalConfig == null)
			throw new IllegalStateException("Config not loaded");
		return this.globalConfig;
	}

	public Text[] getUpdateMessages() {
		return this.updateMessages;
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
