package omtteam.ompd.compat;

import net.minecraftforge.fml.common.event.FMLInterModComms;

/**
 * Created by Keridos on 23/01/2015. This Class
 */
public class ModCompatibility {
    public static void performModCompat() {
        FMLInterModComms.sendMessage("Waila", "register",
                                     "omtteam.ompd.compat.WailaTileHandler.callbackRegister");
    }
}
