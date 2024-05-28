package net.spellcraftgaming.rpghud.gui.hud.element.modern;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLiving;
import net.spellcraftgaming.lib.GameData;
import net.spellcraftgaming.rpghud.gui.hud.element.vanilla.HudElementEntityInspectVanilla;
import net.spellcraftgaming.rpghud.settings.Settings;

import net.minecraft.entity.EntityList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.util.ResourceLocation;

public class HudElementEntityInspectModern extends HudElementEntityInspectVanilla {

    private long focusLostTime = 0L;
    private EntityLiving previousFocused;

    // Create a set of entity IDs to ignore
    private static final Set<ResourceLocation> IGNORED_ENTITY_IDS = new HashSet<>(
            Arrays.asList(
                    new ResourceLocation("eyesinthedarkness:eyes"),
                    new ResourceLocation("weeping-angels:weepingangel"),
                    new ResourceLocation("betterswim:herobrine"),
                    new ResourceLocation("minecraft:creeper"),
                    new ResourceLocation("minecraft:bat"),
                    new ResourceLocation("dimdoors:mob_monolith")

            )
    );

    @Override
    public void drawElement(Gui gui, float zLevel, float partialTicks, int scaledWidth, int scaledHeight) {
        EntityLiving focused = GameData.getFocusedEntity(GameData.getPlayer());

        // Check if the focused entity should be ignored
        if (focused != null) {
            ResourceLocation entityId = EntityList.getKey(focused);
            if (entityId != null && IGNORED_ENTITY_IDS.contains(entityId)) {
                return;
            }
        }

        if (focused != null) {
            previousFocused = focused;
            focusLostTime = 0L; // Reset focusLostTime if the entity is focused again
        } else if (previousFocused != null) {
            if (focusLostTime == 0L) {
                focusLostTime = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - focusLostTime < 1500L) { // Adjust the delay value (in milliseconds) as desired
                focused = previousFocused;
            } else {
                // Reset the focusLostTime and previousFocused after the delay
                focusLostTime = 0L;
                previousFocused = null;
                return;
            }
        } else {
            return; // Exit the method if there is no focused entity and no previousFocused entity
        }

        if (focused != null) {
            int posX = (scaledWidth / 2) + this.settings.getPositionValue(Settings.inspector_position)[0];
            int posY = this.settings.getPositionValue(Settings.inspector_position)[1];

            drawRect(posX - 62, 20 + posY, 32, 32, 0xA0000000);
            drawRect(posX - 60, 22 + posY, 28, 28, 0x20FFFFFF);
            drawRect(posX - 30, 20 + posY, 90, 12, 0xA0000000);
            drawTetragon(posX - 30, posX - 30, 32 + posY, 32 + posY, 90, 76, 10, 10, 0xA0000000);
            drawTetragon(posX - 30, posX - 30, 33 + posY, 33 + posY, 84, 74, 6, 6, 0x20FFFFFF);

            drawTetragon(posX - 30, posX - 30, 33 + posY, 33 + posY, (int) (84 * ((double) focused.getHealth() / (double) focused.getMaxHealth())),
                    (int) (84 * ((double) focused.getHealth() / (double) focused.getMaxHealth())) - 10, 6, 6, this.settings.getIntValue(Settings.color_health));

            String stringHealth = ((double) Math.round(focused.getHealth() * 10)) / 10 + "/" + ((double) Math.round(focused.getMaxHealth() * 10)) / 10;

            GlStateManager.scale(0.5, 0.5, 0.5);
            gui.drawCenteredString(GameData.getFontRenderer(), stringHealth, (posX - 29 + 44) * 2, (34 + posY) * 2, -1);
            GlStateManager.scale(2.0, 2.0, 2.0);

            int x = (posX - 29 + 44 - GameData.getFontRenderer().getStringWidth(focused.getName()) / 2);
            int y = 23 + posY;
            GameData.getFontRenderer().drawString(focused.getName(), x, y, -1);

            drawEntityOnScreen(posX - 60 + 14, 22 + 25 + posY, focused);

            if (settings.getBoolValue(Settings.show_entity_armor)) {
                int armor = focused.getTotalArmorValue();
                if (armor > 0) {
                    this.mc.getTextureManager().bindTexture(GameData.icons());
                    String value = String.valueOf(armor);
                    drawRect(posX - 30, posY + 42, 8 + (GameData.getFontRenderer().getStringWidth(value) / 2), 6, 0xA0000000);
                    GlStateManager.scale(0.5, 0.5, 0.5);
                    gui.drawTexturedModalRect((posX - 30) * 2, (posY + 42) * 2, 34, 9, 9, 9);
                    GameData.getFontRenderer().drawString(value, (posX - 24) * 2, (posY + 42) * 2 + 1, -1);
                    GlStateManager.scale(2.0, 2.0, 2.0);
                }
            }
        }
    }
}
