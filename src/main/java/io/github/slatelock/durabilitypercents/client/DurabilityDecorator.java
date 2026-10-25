package io.github.slatelock.durabilitypercents.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;

public final class DurabilityDecorator implements IItemDecorator {

    private static final float SCALE = 0.5f;

    @Override
    public boolean render(GuiGraphics graphics, Font font, ItemStack stack, int x, int y) {
        if (!stack.isDamageableItem() || !stack.isDamaged()) {
            return false;
        }

        int max = stack.getMaxDamage();
        int remaining = max - stack.getDamageValue();
        int percent = Math.round(remaining * 100f / max);
        String text = percent + "%";

        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(x, y, 200);
        pose.scale(SCALE, SCALE, 1f);
        graphics.drawString(font, text, 1, 1, 0xFF000000 | stack.getBarColor(), true);
        pose.popPose();

        return false;
    }
}