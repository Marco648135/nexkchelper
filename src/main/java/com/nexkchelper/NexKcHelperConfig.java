package com.nexkchelper;

import net.runelite.client.config.*;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

@ConfigGroup("nexkchelper")
public interface NexKcHelperConfig extends Config
{
	@ConfigItem(
			position = 0,
			keyName = "hotkey",
			name = "Hotkey",
			description = "Press this key to mark all newly spawned NPCs in range."
	)
	default Keybind keybind()
	{
		return new Keybind(KeyEvent.VK_UNDEFINED, InputEvent.CTRL_DOWN_MASK);
	}

	@ConfigItem(
			position = 1,
			keyName = "excludeRangeMelee",
			name = "Exclude warriors and rangers",
			description = "Will exclude spiritual warriors and rangers from being marked."
	)
	default boolean excludeRangeMelee() { return true; }

	enum Outline {
		CLICKBOX,
		MODEL,
	}

	@ConfigItem(
			position = 2,
			keyName = "outlineStyle",
			name = "Outline Style",
			description = "The style of the outline around the NPCs."
	)
	default Outline outline() { return Outline.CLICKBOX; }



	@Alpha
	@ConfigItem(
			position = 3,
			keyName = "outlineColor",
			name = "Outline Color",
			description = "Configures the outline colour."
	)
	default Color outlineColor()
	{
		return Color.WHITE;
	}

	@Alpha
	@ConfigItem(
			position = 4,
			keyName = "fillColor",
			name = "Fill Color",
			description = "Configures the fill colour for hull style."
	)
	default Color fillColor()
	{
		return new Color(255, 255, 255, 60);
	}


	@ConfigItem(
			position = 5,
			keyName = "outlineWidth",
			name = "Outline Width",
			description = "The width of the outline around the NPCs (model outline)."
	)
	default int outlineWidth() { return 2; }

	@ConfigItem(
			position = 6,
			keyName = "outlineFeather",
			name = "Outline Feather",
			description = "The feather of the outline around the NPCs (model outline)."
	)
	default int outlineFeather() { return 2; }
}

