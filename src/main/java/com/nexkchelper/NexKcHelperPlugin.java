package com.nexkchelper;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.NpcUtil;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.infobox.InfoBox;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import net.runelite.client.util.HotkeyListener;
import net.runelite.client.util.ImageUtil;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import static net.runelite.api.HitsplatID.VENOM;

@Slf4j
@PluginDescriptor(
	name = "Nex Kc Helper"
)
public class NexKcHelperPlugin extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private NexKcHelperConfig config;
	@Inject
	private NexKcHelperOverlay npcMarkerOverlay;
	@Inject
	private NpcUtil npcUtil;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private ConfigManager configManager;
	@Inject
	private KeyManager keyManager;
	@Inject
	private InfoBoxManager infoBoxManager;

	private final BufferedImage image = ImageUtil.loadImageResource(NexKcHelperPlugin.class, "/util/nexling.png");

	private final InfoBox activeBox = new InfoBox(image, this) {
		@Override
		public String getText() {
			return "";
		}

		@Override
		public Color getTextColor() {
			return Color.CYAN;
		}
		@Override
		public String getTooltip() {
			return "Nex Kc Helper";
		}
	};

	private boolean active;
	@Getter(AccessLevel.PACKAGE)
	private final ArrayList<NPC> npcs = new ArrayList<>();

	private final HotkeyListener hotkeyListener = new HotkeyListener(() -> config.keybind()) {
		@Override
		public void keyPressed(KeyEvent e)
		{
			if (config.keybind().matches(e))
			{
				if (active)
				{
					reset();
				}
				else
				{
					infoBoxManager.addInfoBox(activeBox);
					active = true;
				}
			}
		}
	};

	@Override
	protected void startUp() throws Exception
	{
		reset();
		overlayManager.add(npcMarkerOverlay);
		keyManager.registerKeyListener(hotkeyListener);
	}

	@Override
	protected void shutDown() throws Exception
	{
		reset();
		overlayManager.remove(npcMarkerOverlay);
		keyManager.unregisterKeyListener(hotkeyListener);
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned npcSpawned)
	{
		if (active)
		{
			final NPC npc = npcSpawned.getNpc();
			final String npcName = npc.getName();
			if (npcName != null || !npcs.contains(npc))
			{
				if (config.excludeRangeMelee() && (npcName.equals("Spiritual Warrior") || npcName.equals("Spiritual Ranger")))
				{
					return;
				}
				npcs.add(npc);
			}
		}
	}

	@Subscribe
	public void onHitsplatApplied(HitsplatApplied hitsplatApplied)
	{
		if (active)
		{
			if (hitsplatApplied.getHitsplat().getHitsplatType() == VENOM )
			{
				if (hitsplatApplied.getActor() instanceof NPC)
				{
					npcs.remove((NPC) hitsplatApplied.getActor());
				}
			}
		}
	}

	private void reset()
	{
		active = false;
		if (!infoBoxManager.getInfoBoxes().isEmpty()) {
			infoBoxManager.removeInfoBox(activeBox);
		}
		npcs.clear();
	}


	@Provides
	NexKcHelperConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(NexKcHelperConfig.class);
	}
}
