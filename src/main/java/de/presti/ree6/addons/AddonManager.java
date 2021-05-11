package de.presti.ree6.addons;

import de.presti.ree6.bot.BotInfo;
import de.presti.ree6.utils.Logger;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

public class AddonManager {

    public ArrayList<Addon> addons = new ArrayList<>();

    public AddonManager() {
    }

    public void reload() {
        stopAddons();

        addons.clear();

        AddonLoader.loadAllAddons();

        startAddons();
    }

    public void startAddon(Addon addon) {
        if (!addon.getRee6ver().equalsIgnoreCase(BotInfo.build)) {
            Logger.log("AddonManager", "The Addon " + addon.getName() + " by " + addon.getAuthor() + " has been developed for Ree6 b" + addon.getRee6ver() + " but you have a newer Version so becarefull!");
        }

        try {

            Class urlcl = new URLClassLoader(new URL[]{addon.getFile().toURI().toURL()}).loadClass(addon.getMainpath());

            boolean valid = false;


            Class[] ifs = urlcl.getInterfaces();

            for (int i = 0; i < ifs.length && !valid; i++) {
                if (ifs[i].getName().equalsIgnoreCase("de.presti.ree6.addons.AddonInterface")) {
                    valid = true;
                }
            }

            if (valid) {
                AddonInterface inf = (AddonInterface) urlcl.newInstance();
                inf.onEnable();
            } else {
                Logger.log("AddonManager", "Couldnt start the Addon " + addon.getName() + "(" + addon.getAddonver() + ") by " + addon.getAuthor());
                Logger.log("AddonManager", "It doesnt implement the AddonInterface!");
            }


        } catch (Exception ex) {
            Logger.log("AddonManager", "Couldnt start the Addon " + addon.getName() + "(" + addon.getAddonver() + ") by " + addon.getAuthor());
            Logger.log("AddonManager", "Infos: " + addon.getMainpath() + ", " + addon.getAddonver() + ", " + addon.getRee6ver());
            Logger.log("AddonManager", "Exception: " + ex.getCause().getMessage());
        }
    }

    public void startAddons() {
        for (Addon addon : addons) {
            startAddon(addon);
        }
    }

    public void stopAddon(Addon addon) {
        try {
            Class urlcl = new URLClassLoader(new URL[]{addon.getFile().toURI().toURL()}).loadClass(addon.getMainpath());

            boolean valid = false;


            Class[] ifs = urlcl.getInterfaces();

            for (int i = 0; i < ifs.length && !valid; i++) {
                if (ifs[i].getName().equalsIgnoreCase("de.presti.ree6.addons.AddonInterface")) {
                    valid = true;
                }
            }

            if (valid) {
                AddonInterface inf = (AddonInterface) urlcl.newInstance();
                inf.onDisable();
            } else {
                Logger.log("AddonManager", "Couldnt start the Addon " + addon.getName() + "(" + addon.getAddonver() + ") by " + addon.getAuthor());
                Logger.log("AddonManager", "It doesnt implement the AddonInterface!");
            }
        } catch (Exception ex) {
            Logger.log("AddonManager", "Couldnt stop the Addon " + addon.getName() + "(" + addon.getAddonver() + ") by " + addon.getAuthor() + "\nException: " + ex.getCause().getMessage());
        }
    }

    public void stopAddons() {
        for (Addon addon : addons) {
            stopAddon(addon);
        }
    }

    public void loadAddon(Addon addon) {
        addons.add(addon);
    }

}