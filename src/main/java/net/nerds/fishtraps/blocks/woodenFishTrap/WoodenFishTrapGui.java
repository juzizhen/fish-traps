package net.nerds.fishtraps.blocks.woodenFishTrap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.nerds.fishtraps.Fishtraps;

@Environment(EnvType.CLIENT)
public class WoodenFishTrapGui extends HandledScreen<WoodenFishTrapContainer> {

    private static final Identifier TEXTURE = new Identifier(Fishtraps.MODID, "textures/gui/fish_trap_gui1.png");

    public WoodenFishTrapGui(WoodenFishTrapContainer container, PlayerInventory playerInventory, Text title) {
        super(container, playerInventory, title);
        this.backgroundHeight = 133 + 5 * 18;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
        this.titleX = 8;
        this.titleY = 6;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(this.textRenderer, this.title, this.titleX, this.titleY, 0x404040, false);
    }
}