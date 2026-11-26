package com.echobell.echobell;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.item.CreativeModeTabs;

/**
 * 回响之铃 / Echo Bell
 *
 * 一个很小的原版向模组：右键摇铃，让周围生物短暂发光，方便在黑暗里找怪。
 */
@Mod(EchoBell.MODID)
public class EchoBell {
    public static final String MODID = "echobell";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<Item> ECHO_BELL = ITEMS.register("echo_bell",
            () -> new EchoBellItem(new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.UNCOMMON)));

    public EchoBell(IEventBus modEventBus, ModContainer modContainer) {
        ITEMS.register(modEventBus);
        modEventBus.addListener(this::addCreative);
        LOGGER.info("Echo Bell loaded");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ECHO_BELL);
        }
    }
}
