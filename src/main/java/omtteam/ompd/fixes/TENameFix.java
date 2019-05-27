package omtteam.ompd.fixes;

import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IFixableData;
import omtteam.ompd.OpenModularPassiveDefense;
import omtteam.ompd.reference.OMPDNames;
import omtteam.ompd.reference.Reference;

import javax.annotation.Nonnull;
import java.util.Map;

public class TENameFix implements IFixableData {

    private static final Map<String, String> NAME_MAP;

    static {
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        builder.put("minecraft:" + OMPDNames.Blocks.ownedBlock, Reference.MOD_ID + ":" + OMPDNames.Blocks.ownedBlock);
        builder.put("minecraft:" + OMPDNames.Blocks.camoTrap, Reference.MOD_ID + ":" + OMPDNames.Blocks.camoTrap);
        NAME_MAP = builder.build();
    }

    @Override
    public int getFixVersion() {
        return 1;
    }

    @Nonnull
    @Override
    public NBTTagCompound fixTagCompound(@Nonnull NBTTagCompound compound) {
        String oldId = compound.getString("id");
        if (NAME_MAP.containsKey(oldId)) {
            OpenModularPassiveDefense.getLogger().info("Fixed TE from {} to {}", oldId, NAME_MAP.get(oldId));
            compound.setString("id", NAME_MAP.get(oldId));
        }
        return compound;
    }
}
