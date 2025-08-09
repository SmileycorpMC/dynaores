package net.smileycorp.dynaores.common.data;

import com.google.common.io.Files;
import net.minecraft.item.ItemStack;
import net.smileycorp.dynaores.common.DynaOresLogger;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.Scanner;

public class OreCacheLoader {

    public static final OreCacheLoader INSTANCE = new OreCacheLoader();

    private OreCacheLoader() {}

    private final File file = Paths.get("dynaores_cache").toFile();
    private boolean loaded = false;

    public boolean clearCache() {
        if (!cacheExists()) return false;
        file.delete();
        DynaOresLogger.logInfo("Deleted cache file " +  file.getAbsolutePath());
        return true;
    }

    public boolean exportCache() {
        if (cacheExists()) {
            File backup = Paths.get("dynaores_cache_backup").toFile();
            try {
                backup.createNewFile();
                Files.copy(file, backup);
                file.delete();
            } catch (Exception e) {
                DynaOresLogger.logError("Failed to backup old cache file", e);
                return false;
            }
        }
        try {
            file.createNewFile();
            try(FileWriter output = new FileWriter(file)) {
                for (OreEntry entry : OreHandler.INSTANCE.getOres()) {
                    Color colour = new Color(entry.getColour());
                    StringBuilder builder = new StringBuilder(entry.getName() + "-" + "0x" + String.format("%H", ((colour.getRed() & 0xFF) << 16)
                            | ((colour.getGreen() & 0xFF) << 8) | ((colour.getBlue() & 0xFF))));
                    ItemStack material = entry.getMaterial();
                    if (!material.isEmpty()) {
                        builder.append("-" + material.getItem().getRegistryName() + ":" + material.getMetadata());
                        if (material.hasTagCompound()) builder.append(material.getTagCompound());
                    }
                    output.append(builder.append(System.lineSeparator()));
                }
                DynaOresLogger.logInfo("Exported cache file to " +  file.getAbsolutePath());
            }
        } catch (Exception e) {
            DynaOresLogger.logError("Failed to export ores cache", e);
        }
        return true;
    }

    public void tryLoadCache() {
        if (!cacheExists()) return;
        DynaOresLogger.logInfo("Ore cache exists, loading from file " +  file.getAbsolutePath());
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNext()) OreHandler.INSTANCE.tryRegisterCustom(scanner.nextLine());
            loaded = true;
            DynaOresLogger.logInfo("Loaded ore cache");
        } catch (Exception e) {
            DynaOresLogger.logError("Failed to read ores cache", e);
        }
    }

    private boolean cacheExists() {
        return file.exists();
    }

    public boolean isActive() {
        return loaded;
    }

}
