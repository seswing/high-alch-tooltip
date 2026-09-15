package com.example;

import javax.inject.Inject;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
        name = "High Alch Value Tooltip",
        description = "Displays the High Alchemy value and profit when hovering over items with the High Alchemy spell.",
        tags = {"magic", "alchemy", "alch", "overlay", "tooltip"}
)
public class HighAlchTooltipPlugin extends Plugin
{
    @Inject
    private OverlayManager overlayManager;

    @Inject
    private HighAlchTooltipOverlay overlay;

    @Override
    protected void startUp() throws Exception
    {
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown() throws Exception
    {
        overlayManager.remove(overlay);
    }
}