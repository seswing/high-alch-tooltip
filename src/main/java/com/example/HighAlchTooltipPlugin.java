package com.example;

import javax.inject.Inject;
import com.google.inject.Provides;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
        name = "High Alch Value Tooltip",
        description = "Displays High Alch prices, GE prices, and profit margins on item tooltips.",
        tags = {"high", "alch", "profit", "magic", "tooltip"}
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

    @Provides
    HighAlchTooltipConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(HighAlchTooltipConfig.class);
    }
}