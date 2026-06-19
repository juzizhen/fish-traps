package net.nerds.fishtraps.blocks.ironFishTrap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.nerds.fishtraps.Fishtraps;

@Environment(EnvType.CLIENT)
public class IronFishTrapGui extends AbstractContainerScreen<IronFishTrapContainer> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(Fishtraps.MODID, "textures/gui/fish_trap_gui1.png");

    public IronFishTrapGui(IronFishTrapContainer container, Inventory playerInventory, Component title) {
        super(container, playerInventory, title, 176, 133 + 5 * 18);
        this.inventoryLabelY = this.imageHeight + 100;
        this.titleLabelX = 8;
        this.titleLabelY = 6;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        super.extractBackground(context, mouseX, mouseY, delta);
        context.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
    }
}