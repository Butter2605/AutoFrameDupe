package me.butter.addon;

import com.mojang.logging.LogUtils;
import me.butter.addon.modules.AutoFrameDupe;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class AutoFrameDupeAddon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        LOG.info("Buttering the game");

        // Modules
        Modules.get().add(new AutoFrameDupe());
    }

    @Override
    public String getPackage() {
        return "me.butter.addon";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("Butter", "autoframedupe");
    }
}
