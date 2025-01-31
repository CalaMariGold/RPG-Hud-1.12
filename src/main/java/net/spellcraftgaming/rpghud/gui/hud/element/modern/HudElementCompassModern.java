package net.spellcraftgaming.rpghud.gui.hud.element.modern;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.spellcraftgaming.lib.GameData;
import net.spellcraftgaming.rpghud.gui.hud.element.HudElement;
import net.spellcraftgaming.rpghud.gui.hud.element.HudElementType;
import net.spellcraftgaming.rpghud.settings.Settings;

public class HudElementCompassModern extends HudElement {

    public HudElementCompassModern() {
        super(HudElementType.COMPASS, 0, 0, 0, 0, true);
    }

    @Override
    public boolean checkConditions() {
        return this.settings.getBoolValue(Settings.enable_compass) && !this.mc.gameSettings.showDebugInfo
                && (this.settings.getBoolValue(Settings.enable_immersive_compass) ? GameData.hasPlayerCompass() : true);
    }

    @Override
    public void drawElement(Gui gui, float zLevel, float partialTicks, int scaledWidth, int scaledHeight) {
        int width = (scaledWidth / 2) + this.settings.getPositionValue(Settings.compass_position)[0];
        int posY = this.settings.getPositionValue(Settings.compass_position)[1];
        int swapSides = this.settings.getBoolValue(Settings.invert_compass) ? -1 : 1;

        int rotation = Math.round(((GameData.getRotationYaw() % 360) / 360) * 200);
        if(rotation < 0)
            rotation = 200 + rotation;
        drawRect(width - 50, posY + 2, 100, 6, 0xAA000000);

        gui.drawTexturedModalRect(width - 56, 0, 34, 234, 112, 9);
        if(rotation > 0 && rotation <= 100) {
            gui.drawCenteredString(GameData.getFontRenderer(), "W", width + (50 * swapSides) - (rotation * swapSides), posY + 1, -1);
        }

        if(rotation > 25 && rotation <= 125) {
            gui.drawCenteredString(GameData.getFontRenderer(), ".", width + (75 * swapSides) - (rotation * swapSides), posY - 2, -1);
        }

        if(rotation > 50 && rotation <= 150) {
            gui.drawCenteredString(GameData.getFontRenderer(), "N", width + (100 * swapSides) - (rotation * swapSides), posY + 1,
                    this.settings.getBoolValue(Settings.enable_compass_color) ? 0xE60909 : -1);
        }

        if(rotation > 75 && rotation <= 175) {
            gui.drawCenteredString(GameData.getFontRenderer(), ".", width + (125 * swapSides) - (rotation * swapSides), posY - 2, -1);
        }

        if(rotation > 100 && rotation <= 200) {
            gui.drawCenteredString(GameData.getFontRenderer(), "E", width + (150 * swapSides) - (rotation * swapSides), posY + 1, -1);
        }

        if(rotation >= 125) {
            gui.drawCenteredString(GameData.getFontRenderer(), ".", width + (175 * swapSides) - (rotation * swapSides), posY - 2, -1);
        } else if(rotation <= 25) {
            gui.drawCenteredString(GameData.getFontRenderer(), ".", width - (25 * swapSides) - (rotation * swapSides), posY - 2, -1);
        }

        if(rotation >= 150) {
            gui.drawCenteredString(GameData.getFontRenderer(), "S", width + (200 * swapSides) - (rotation * swapSides), posY + 1, -1);
        } else if(rotation <= 50) {
            gui.drawCenteredString(GameData.getFontRenderer(), "S", width - (rotation * swapSides), posY + 1, -1);
        }

        if(rotation >= 175) {
            gui.drawCenteredString(GameData.getFontRenderer(), ".", width + (225 * swapSides) - (rotation * swapSides), posY - 2, -1);
        } else if(rotation <= 75) {
            gui.drawCenteredString(GameData.getFontRenderer(), ".", width + (25 * swapSides) - (rotation * swapSides), posY - 2, -1);
        }

        if(this.settings.getBoolValue(Settings.enable_compass_coordinates)) {
            int[] pos = GameData.getPlayerPos();
            drawRect(width - 50, posY + 11, GameData.getFontRenderer().getStringWidth(String.valueOf(pos[0])) / 2 + 4, 6, 0xA0000000);
            drawRect((int) (width - ((float) GameData.getFontRenderer().getStringWidth(String.valueOf(pos[1])) / 4) - 2), posY + 11,
                    GameData.getFontRenderer().getStringWidth(String.valueOf(pos[1])) / 2 + 4, 6, 0xA0000000);
            drawRect((width + 48) - (GameData.getFontRenderer().getStringWidth(String.valueOf(pos[2])) / 2) - 2, posY + 11,
                    GameData.getFontRenderer().getStringWidth(String.valueOf(pos[2])) / 2 + 4, 6, 0xA0000000);

            GlStateManager.scale(0.5D, 0.5D, 0.5D);
            gui.drawString(GameData.getFontRenderer(), String.valueOf(pos[0]), (width - 48) * 2, (posY + 12) * 2, -1);
            gui.drawCenteredString(GameData.getFontRenderer(), String.valueOf(pos[1]), width * 2, (posY + 12) * 2, -1);
            gui.drawString(GameData.getFontRenderer(), String.valueOf(pos[2]), (width + 48) * 2 - GameData.getFontRenderer().getStringWidth(String.valueOf(pos[2])),
                    (posY + 12) * 2, -1);
            GlStateManager.scale(2D, 2D, 2D);
        }
    }

}
