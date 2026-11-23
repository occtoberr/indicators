
package com.rooxchicken.truehealth;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrueHealth implements ModInitializer {

    public static final String MOD_ID = "indicators";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static boolean renderTrueHealth = true;
    public static boolean renderTrueSaturation = true;
    public static boolean addAbsorption = true;

    @Override
    public void onInitialize() {
        LOGGER.info("Indicators initialized!");
    }
}
