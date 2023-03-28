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
        Color outlineColor = config.outlineColor();
        Color fillColor = config.fillColor();
        for (NPC npc: plugin.getNpcs()) {
            {
                if (config.outline() == NexKcHelperConfig.Outline.CLICKBOX)
                    hullOutline(npc, graphics, outlineColor, fillColor);
                else
                    modelOutlineRenderer.drawOutline(npc, config.outlineWidth(), outlineColor, config.outlineFeather());
            }
        }

        return null;
    }


    private void hullOutline(NPC npc, Graphics2D graphics, Color outlineColor, Color fillColor)
    {
        NPCComposition npcComposition = npc.getTransformedComposition();

        if (npcComposition != null)
        {
            Shape objectClickbox = npc.getConvexHull();
            if (objectClickbox != null)
            {
                renderPoly(graphics, outlineColor, fillColor, objectClickbox);
            }
        }
    }

    private void renderPoly(Graphics2D graphics, Color outlineColor, Color fillColor, Shape polygon)
    {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(outlineColor);
        graphics.setStroke(new BasicStroke((float) 2));
        graphics.draw(polygon);
        graphics.setColor(fillColor);
        graphics.fill(polygon);
    }
}
