package omtteam.ompd.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;


/**
 * Created by Keridos on 24/11/16.
 * This Class
 */
public class ModSounds {
    public static SoundEvent turretDeploySound;
    public static SoundEvent turretRetractSound;
    public static SoundEvent bulletHitSound;
    public static SoundEvent railGunHitSound;
    public static SoundEvent laserHitSound;
    public static SoundEvent disposableLaunchSound;
    public static SoundEvent grenadeLaunchSound;
    public static SoundEvent machinegunLaunchSound;
    public static SoundEvent incendiaryLaunchSound;
    public static SoundEvent laserLaunchSound;
    public static SoundEvent potatoLaunchSound;
    public static SoundEvent railgunLaunchSound;
    public static SoundEvent relativisticLaunchSound;
    public static SoundEvent rocketLaunchSound;
    public static SoundEvent teleportLaunchSound;
    public static SoundEvent turretWarnSound;


    public static void init(){
        turretDeploySound = new SoundEvent(new ResourceLocation("omtteam/ompd", "turretdeploy"));
        turretRetractSound = new SoundEvent(new ResourceLocation("omtteam/ompd", "turretretract"));
        bulletHitSound = new SoundEvent(new ResourceLocation("omtteam/ompd", "bullethit"));
        railGunHitSound = new SoundEvent(new ResourceLocation("omtteam/ompd", "railgunhit"));
        laserHitSound = new SoundEvent(new ResourceLocation("omtteam/ompd", "laserhit"));
        disposableLaunchSound = new SoundEvent(new ResourceLocation("omtteam/ompd", "disposable"));
        grenadeLaunchSound = new SoundEvent(new ResourceLocation("omtteam/ompd", "grenade"));
        machinegunLaunchSound = new SoundEvent(new ResourceLocation("omtteam/ompd", "machinegun"));
        incendiaryLaunchSound = new SoundEvent(new ResourceLocation("omtteam/ompd", "incendiary"));
        laserLaunchSound = new SoundEvent(new ResourceLocation("omtteam/ompd", "laser"));
        potatoLaunchSound = new SoundEvent(new ResourceLocation("omtteam/ompd", "potato"));
        railgunLaunchSound = new SoundEvent(new ResourceLocation("omtteam/ompd", "railgun"));
        relativisticLaunchSound = new SoundEvent(new ResourceLocation("omtteam/ompd", "relativistic"));
        rocketLaunchSound = new SoundEvent(new ResourceLocation("omtteam/ompd", "rocket"));
        teleportLaunchSound = new SoundEvent(new ResourceLocation("omtteam/ompd", "teleport"));
        teleportLaunchSound = new SoundEvent(new ResourceLocation("omtteam/ompd", "teleport"));
    }
}
