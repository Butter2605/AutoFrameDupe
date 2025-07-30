package me.butter.addon.modules;

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
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class AutoFrameDupe extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    private final Setting<Boolean> shulkersOnly = sgGeneral.add(new BoolSetting.Builder()
        .name("shulkers-only")
        .defaultValue(true)
        .build()
    );

    private final Setting<Integer> range = sgGeneral.add(new IntSetting.Builder()
        .name("range")
        .min(1)
        .max(6)
        .defaultValue(5)
        .sliderMax(6)
        .build()
    );

    private final Setting<Integer> turns = sgGeneral.add(new IntSetting.Builder()
        .name("turns")
        .min(1)
        .max(3)
        .defaultValue(1)
        .sliderMax(3)
        .build()
    );

    private final Setting<Integer> ticks = sgGeneral.add(new IntSetting.Builder()
        .name("ticks")
        .min(1)
        .max(20)
        .defaultValue(10)
        .sliderMax(20)
        .build()
    );

    public AutoFrameDupe() {
        super(Categories.Player, "AutoFrameDupe", "dope");
    }

    private int getShulkerSlot() {
        int shulker_slot = -1;
        for (int i=0; i<9; i++) {
            var stack = mc.player.getInventory().getStack(i);
            var item = stack.getItem();
            String key = item.getTranslationKey();

            if (key.contains("shulker_box")) shulker_slot = i;
        }
        return shulker_slot;
    }

    private int timeoutTicks = 0;

    @EventHandler
    public void onTick(TickEvent.Pre event) {
        if (mc.player == null || mc.world == null) return;

        if (shulkersOnly.get()) {
            int shulker_slot = getShulkerSlot();
            if (shulker_slot != -1) {
                InvUtils.swap(shulker_slot, false);
            }
        }

        for (Entity frame : mc.world.getEntities()) {
            if (!(frame instanceof ItemFrameEntity)) continue;
            if (mc.player.distanceTo(frame) > range.get()) continue;

            if (timeoutTicks >= ticks.get()) {
                if (((ItemFrameEntity) frame).getHeldItemStack().getItem() == Items.AIR && !mc.player.getMainHandStack().isEmpty()) {
                    mc.interactionManager.interactEntity(mc.player, frame, Hand.MAIN_HAND);
                }
                if (((ItemFrameEntity) frame).getHeldItemStack().getItem() != Items.AIR) {
                    for (int i = 0; i < turns.get(); i++) {
                        mc.interactionManager.interactEntity(mc.player, frame, Hand.MAIN_HAND);
                    }
                    mc.interactionManager.attackEntity(mc.player, frame);
                    timeoutTicks = 0;
                }
            }
            ++timeoutTicks;
        }
    }
}
