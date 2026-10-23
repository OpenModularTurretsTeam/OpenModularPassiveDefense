package omtteam.ompd.fixes;

import net.minecraft.nbt.NBTTagCompound;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** OMPD before 3.x registered its tile entities in the minecraft namespace. */
class TENameFixTest {
    private static String fix(String id) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("id", id);
        tag.setInteger("tier", 3);
        NBTTagCompound fixed = new TENameFix().fixTagCompound(tag);
        assertEquals(3, fixed.getInteger("tier"), "the other keys stay");
        return fixed.getString("id");
    }

    @Test
    void oldIdsMoveToTheModNamespace() {
        assertEquals("ompd:ompd_owned_block", fix("minecraft:ompd_owned_block"));
        assertEquals("ompd:camo_trap", fix("minecraft:camo_trap"));
    }

    @Test
    void otherTileEntitiesStay() {
        assertEquals("ompd:camo_trap", fix("ompd:camo_trap"));
        assertEquals("minecraft:chest", fix("minecraft:chest"));
    }
}
