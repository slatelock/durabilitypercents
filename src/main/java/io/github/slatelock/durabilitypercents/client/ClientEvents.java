package io.github.slatelock.durabilitypercents.client;

import io.github.slatelock.durabilitypercents.DurabilityPercents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(modid = DurabilityPercents.MODID, value = Dist.CLIENT)public final class ClientEvents {

    private static final DurabilityDecorator DECORATOR = new DurabilityDecorator();

    @SubscribeEvent
    public static void registerItemDecorations(RegisterItemDecorationsEvent event) {
        for (Item item : BuiltInRegistries.ITEM) {
            if (item.getDefaultInstance().isDamageableItem()) {
                event.register(item, DECORATOR);
            }
        }
    }
    @SubscribeEvent
    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.CROSSHAIR,
                ResourceLocation.fromNamespaceAndPath(DurabilityPercents.MODID, "blocks_left"),
                new BlocksLeftLayer());
    }
}