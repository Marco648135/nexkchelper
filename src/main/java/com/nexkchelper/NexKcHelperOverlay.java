package com.nexkchelper;

import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.client.game.NpcUtil;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

import javax.inject.Inject;
import java.awt.*;
public class NexKcHelperOverlay extends Overlay
{
    private final NexKcHelperPlugin plugin;
    private final NexKcHelperConfig config;

    @Inject
    private Client client;

    @Inject
    private ModelOutlineRenderer modelOutlineRenderer;

    @Inject
    private NexKcHelperOverlay(NexKcHelperPlugin plugin, NexKcHelperConfig config)
    {
        this.plugin = plugin;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {

        for (NPC npc : plugin.getNpcs())
        {
            Color outlineColor = config.outlineColor();
            modelOutlineRenderer.drawOutline(npc, config.outlineWidth(), outlineColor, config.outlineFeather());
        }
        return null;
    }


    private void renderPoly(Graphics2D graphics, Color outlineColor, Color fillColor, Shape polygon)
    {
        System.out.println("TRYING TO RENDER POLY");
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(outlineColor);
        graphics.setStroke(new BasicStroke((float) 2));
        graphics.draw(polygon);
        graphics.setColor(fillColor);
        graphics.fill(polygon);
    }
}
