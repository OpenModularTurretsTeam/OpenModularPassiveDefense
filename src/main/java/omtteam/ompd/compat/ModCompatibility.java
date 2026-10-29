package omtteam.ompd.compat;

import omtteam.omlib.compat.OMLibModCompatibility;

/**
 * Created by Keridos on 23/01/2015. This Class
 */
public class ModCompatibility {
    public static void performModCompat() {
        // through OMLib's Hwyla plugin, as OMT: Hwyla's mod id is "waila", so IMC to "Waila" never arrived
        if (OMLibModCompatibility.WailaLoaded) {
            OMPDWailaWrapper.register();
        }
    }
}
