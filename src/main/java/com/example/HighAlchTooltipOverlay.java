package com.example;

import javax.inject.Inject;
import java.awt.Dimension;
import java.awt.Graphics2D;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;

public class HighAlchTooltipOverlay extends Overlay
{
    private final Client client;
    private final ItemManager itemManager;
    private final TooltipManager tooltipManager;

    @Inject
    public HighAlchTooltipOverlay(Client client, ItemManager itemManager, TooltipManager tooltipManager)
    {
        setPosition(OverlayPosition.DYNAMIC);
        this.client = client;
        this.itemManager = itemManager;
        this.tooltipManager = tooltipManager;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        MenuEntry[] menuEntries = client.getMenuEntries();
        if (menuEntries.length == 0)
        {
            return null;
        }

        MenuEntry topEntry = menuEntries[menuEntries.length - 1];

        // 1. Check if the active action is targeting an item with a selected spell
        if (topEntry.getType() != MenuAction.WIDGET_TARGET_ON_WIDGET)
        {
            return null;
        }

        // 2. Verify the selected spell is High Level Alchemy via target/option text
        String targetText = topEntry.getTarget();
        String optionText = topEntry.getOption();

        boolean isHighAlch = (targetText != null && targetText.contains("High Level Alchemy"))
                || (optionText != null && optionText.contains("High Level Alchemy"));

        if (!isHighAlch)
        {
            return null;
        }

        // 3. Extract hovered item ID
        int itemId = topEntry.getItemId();
        if (itemId <= 0)
        {
            return null;
        }

        // 4. Calculate prices
        int alchValue = itemManager.getItemComposition(itemId).getHaPrice();
        int gePrice = itemManager.getItemPrice(itemId);
        int profit = alchValue - gePrice;

        // 5. Append tooltip
        String tooltipText = String.format(
                "High Alch Value: %,d gp<br>GE Price: %,d gp<br>Est. Profit: %,d gp",
                alchValue, gePrice, profit
        );

        tooltipManager.add(new Tooltip(tooltipText));

        return null;
    }
}