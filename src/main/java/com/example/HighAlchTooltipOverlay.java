package com.example;

import javax.inject.Inject;
import java.awt.Color;
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
import net.runelite.client.util.ColorUtil;

public class HighAlchTooltipOverlay extends Overlay
{
    private static final int NATURE_RUNE_ITEM_ID = 561;

    private final Client client;
    private final ItemManager itemManager;
    private final TooltipManager tooltipManager;
    private final HighAlchTooltipConfig config;

    @Inject
    public HighAlchTooltipOverlay(Client client, ItemManager itemManager, TooltipManager tooltipManager, HighAlchTooltipConfig config)
    {
        setPosition(OverlayPosition.DYNAMIC);
        this.client = client;
        this.itemManager = itemManager;
        this.tooltipManager = tooltipManager;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        MenuEntry topEntry = client.getMenu().getMenuEntries().length > 0
                ? client.getMenu().getMenuEntries()[client.getMenu().getMenuEntries().length - 1]
                : null;

        if (topEntry == null || topEntry.getType() != MenuAction.WIDGET_TARGET_ON_WIDGET)
        {
            return null;
        }

        String targetText = topEntry.getTarget();
        String optionText = topEntry.getOption();

        boolean isHighAlch = (targetText != null && targetText.contains("High Level Alchemy"))
                || (optionText != null && optionText.contains("High Level Alchemy"));

        if (!isHighAlch)
        {
            return null;
        }

        int itemId = topEntry.getItemId();
        if (itemId <= 0)
        {
            return null;
        }

        // 1. Calculate values
        int alchValue = itemManager.getItemComposition(itemId).getHaPrice();
        int itemGePrice = itemManager.getItemPrice(itemId);
        int natRunePrice = itemManager.getItemPrice(NATURE_RUNE_ITEM_ID);

        int totalCost = itemGePrice + natRunePrice;
        int profit = alchValue - totalCost;

        // 2. Select overall color based on profitability
        Color overallColor = profit >= 0 ? Color.GREEN : Color.RED;

        StringBuilder sb = new StringBuilder();

        // 3. Add optional warning header
        if (config.showLossWarning() && profit < 0)
        {
            sb.append("⚠️ WARNING: NEGATIVE PROFIT! ⚠️<br>");
        }

        // 4. Build text body
        sb.append(String.format(
                "High Alch Value: %,d gp<br>" +
                        "GE Price: %,d gp<br>" +
                        "Nat Rune Cost: %,d gp<br>" +
                        "Est. Profit: %,d gp",
                alchValue, itemGePrice, natRunePrice, profit
        ));

        // 5. Wrap the entire tooltip output in the chosen color
        String coloredTooltip = ColorUtil.wrapWithColorTag(sb.toString(), overallColor);

        tooltipManager.add(new Tooltip(coloredTooltip));

        return null;
    }
}