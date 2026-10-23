package omtteam.ompd.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    private static GuiHandler instance;

    private GuiHandler() {
    }

    public static GuiHandler getInstance() {
        if (instance == null) {
            instance = new GuiHandler();
        }
        return instance;
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        // OMPD has no GUIs of its own (the camouflage menu is OMLib's)
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }
}