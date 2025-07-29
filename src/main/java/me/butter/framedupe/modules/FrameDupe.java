package me.butter.framedupe.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class FrameDupe extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    private final Setting<Boolean> shulkersonly = sgGeneral.add(new BoolSetting.Builder()
        .name("shulkers-only")
        .defaultValue(true)
        .build()
    );

    private final Setting<Integer> range = sgGeneral.add(new IntSetting.Builder()
        .name("range")
        .defaultValue(5)
        .min(1)
        .max(6)
        .sliderMax(6)
        .build()
    );

    private final Setting<Integer> turns = sgGeneral.add(new IntSetting.Builder()
        .name("turns")
        .defaultValue(1)
        .min(1)
        .max(3)
        .sliderMax(3)
        .build()
    );

    private final Setting<Integer> ticks = sgGeneral.add(new IntSetting.Builder()
        .name("ticks")
        .defaultValue(10)
        .min(1)
        .max(20)
        .sliderMax(20)
        .build()
    );

    private int timeoutTicks = 0;

    public FrameDupe() {
        super(Categories.Player, "AutoFrameDupe", "LeuxBackDoor");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (shulkersonly.get()) {
            int shulkerSlot = findShulkerSlot();
            if (shulkerSlot != -1) {
                InvUtils.swap(shulkerSlot, false);
            }
        }

        assert mc.world != null;
        assert mc.player != null;
        for (Entity entity : mc.world.getEntities()) {
            if (!(entity instanceof ItemFrameEntity frame)) continue;

            if (mc.player.distanceTo(frame) > range.get()) continue;

            if (timeoutTicks >= ticks.get()) {
                Item displayedItem = frame.getHeldItemStack().getItem();
                if (displayedItem == Items.AIR && !mc.player.getMainHandStack().isEmpty()) {
                    assert mc.interactionManager != null;
                    mc.interactionManager.interactEntity(mc.player, frame, Hand.MAIN_HAND);
                } else if (displayedItem != Items.AIR) {
                    for (int i = 0; i < turns.get(); i++) {
                        assert mc.interactionManager != null;
                        mc.interactionManager.interactEntity(mc.player, frame, Hand.MAIN_HAND);
                    }
                }
                assert mc.interactionManager != null;
                mc.interactionManager.attackEntity(mc.player, frame);
                timeoutTicks = 0;
            }
            timeoutTicks++;
        }
    }

    private int findShulkerSlot() {
        for (int i = 0; i < 9; i++) {
            var stack = mc.player.getInventory().getStack(i);
            var item = stack.getItem();
            String transKey = item.getTranslationKey();

            if (transKey.contains("shulker_box")) return i;
        }
        return -1;
    }

}
