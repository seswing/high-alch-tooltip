package com.example;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("highalchtooltip")
public interface HighAlchTooltipConfig extends Config
{
    @ConfigItem(
        keyName = "showLossWarning",
        name = "Highlight Loss Warning",
        description = "Adds a prominent WARNING text on tooltips for non-profitable items."
    )
    default boolean showLossWarning()
    {
        return true;
    }
}