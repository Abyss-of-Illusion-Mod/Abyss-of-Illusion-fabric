package com.aoimod.hud;

import com.aoimod.AbyssOfIllusionMod;
import com.aoimod.customvalues.IThirsty;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

public class ThirstHudOverlay implements HudRenderCallback {
    private static final Identifier THIRST_ICONS = Identifier.of(
            AbyssOfIllusionMod.MOD_ID,
            "textures/hud/filled_thirst.png");
    private static final Identifier EMPTY_ICONS = Identifier.of(
            AbyssOfIllusionMod.MOD_ID,
            "textures/hud/empty_thirst.png");

    private static final int EMPTY_U = 0;
    private static final int ICON_SIZE = 9;

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && (client.player.isCreative() || client.player.isSpectator()))
            return;

        IThirsty thirsty = (IThirsty) client.player;
        float thirstLevel = thirsty.getThirsty().getThirst();
        int x = 0;
        int y = 0;
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();
        x = width / 2;
        y = height;

        for (int i = 0; i < 10; i++) {
            drawContext.drawTexture(
                    RenderLayer::getGuiTextured,
                    EMPTY_ICONS,
                    x + 8 + (i * 8),
                    y - 49,
                    EMPTY_U,
                    0,
                    ICON_SIZE,
                    ICON_SIZE,
                    ICON_SIZE,
                    ICON_SIZE);
        }
        for (int i = 0; i < thirstLevel; i++) {
            if (thirstLevel / 2 > i) {
                drawContext.drawTexture(
                        RenderLayer::getGuiTextured,
                        THIRST_ICONS,
                        x + 8 + (i * 8),
                        y - 49,
                        EMPTY_U,
                        0,
                        ICON_SIZE,
                        ICON_SIZE,
                        ICON_SIZE,
                        ICON_SIZE);
            } else {
                break;
            }
        }
    }
}