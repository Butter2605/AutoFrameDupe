package me.butter.framedupe;

import me.butter.framedupe.modules.FrameDupe;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Modules;

public class FrameDupeAddon extends MeteorAddon {

    @Override
    public void onInitialize() {

        // Modules
        Modules.get().add(new FrameDupe());
    }

    @Override
    public String getPackage() {
        return "me.butter.framedupe";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("MeteorDevelopment", "butter-framedupe");
    }
}
