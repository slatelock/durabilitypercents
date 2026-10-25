package io.github.slatelock.durabilitypercents.client;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public final class BlocksLeftLayer implements LayeredDraw.Layer {

    private static final int TEXT_OFFSET_Y = 14;

    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null || mc.options.hideGui
                || !mc.options.getCameraType().isFirstPerson()
                || mc.player.getAbilities().instabuild) {
            return;
        }

        if (!(mc.hitResult instanceof BlockHitResult hit) || hit.getType() != HitResult.Type.BLOCK) {
            return;
        }

        ItemStack stack = mc.player.getMainHandItem();
        if (!stack.isDamageableItem()) {
            return;
        }

        Tool tool = stack.get(DataComponents.TOOL);
        if (tool == null || tool.damagePerBlock() <= 0) {
            return;
        }

        BlockPos pos = hit.getBlockPos();
        BlockState state = mc.level.getBlockState(pos);
        if (state.getDestroySpeed(mc.level, pos) == 0.0f) {
            return;
        }

        int remaining = stack.getMaxDamage() - stack.getDamageValue();
        int unbreaking = getUnbreakingLevel(mc, stack);
        long blocks = (long) remaining * (unbreaking + 1) / tool.damagePerBlock();

        String text = "~" + blocks + " blocks";
        graphics.drawCenteredString(mc.font, text,
                graphics.guiWidth() / 2, graphics.guiHeight() / 2 + TEXT_OFFSET_Y, 0xFFFFFFFF);
    }

    private static int getUnbreakingLevel(Minecraft mc, ItemStack stack) {
        Holder<Enchantment> unbreaking = mc.level.registryAccess()
                .registryOrThrow(Registries.ENCHANTMENT)
                .getHolderOrThrow(Enchantments.UNBREAKING);
        return stack.getEnchantmentLevel(unbreaking);    }
}