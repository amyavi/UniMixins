package io.github.legacymoddingmc.unimixins.gasstation;

import cpw.mods.fml.common.versioning.ComparableVersion;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GasStationModule {

    private static final Logger LOGGER = LogManager.getLogger("unimixin-gasstation");

    public static void init() {
        if(!skipIntegrityChecks()) {
            checkComponentIntegrity();
        }
    }
    
    private static boolean skipIntegrityChecks() {
        Configuration config = new Configuration(new File(Launch.minecraftHome, "config/unimixins.cfg"));
        config.load();

        boolean disableIntegrityChecks = config.getBoolean("disableIntegrityChecks", "general", false, "Don't throw an error if an invalid combination of modules is detected. For advanced users.");

        if(config.hasChanged()) {
            config.save();
        }

        return disableIntegrityChecks;
    }

    // Logic copied from FalsePatternLib for interoperability
    private static void checkComponentIntegrity() {
        List<String> missingComponents = new ArrayList<>();

        if(!classExists("com.falsepattern.gasstation.core.GasStationCore")) {
            missingComponents.add("GasStation");
        }
        if(!classExists("makamys.mixingasm.api.TransformerInclusions")) {
            missingComponents.add("Mixingasm");
        }
        if(!classExists("ru.timeconqueror.spongemixins.core.SpongeMixinsCore")) {
            missingComponents.add("SpongeMixins");
        }
        if(!classExists("io.github.tox1cozz.mixinbooterlegacy.MixinBooterLegacyPlugin")) {
            missingComponents.add("MixinBooterLegacy");
        }
        if(!classExists("org.spongepowered.asm.lib.Opcodes") || classExists("org.spongepowered.libraries.org.objectweb.asm.Opcodes")) {
            missingComponents.add("MixinBooterLegacy");
        }
        
        if(!missingComponents.isEmpty()) {
            LOGGER.error("The following missing components were detected: " + missingComponents);
            LOGGER.error("Please obtain mods which provide them.");
            throw new RuntimeException("Missing components detected");
        }
    }

    public static boolean classExists(String string) {
        return GasStationModule.class.getResource("/" + string.replaceAll("\\.", "/") + ".class") != null;
    }
    
    

}
