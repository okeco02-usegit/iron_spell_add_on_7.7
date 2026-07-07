/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.ForgeConfigSpec
 *  net.minecraftforge.common.ForgeConfigSpec$BooleanValue
 *  net.minecraftforge.common.ForgeConfigSpec$Builder
 *  net.minecraftforge.common.ForgeConfigSpec$ConfigValue
 *  net.minecraftforge.common.ForgeConfigSpec$EnumValue
 *  net.minecraftforge.common.ForgeConfigSpec$IntValue
 *  org.apache.commons.lang3.tuple.Pair
 */
package top.theillusivec4.curios.common;

import java.util.List;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class CuriosConfig {
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final Server SERVER;
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;
    private static final String CONFIG_PREFIX = "gui.curios.config.";

    static {
        Pair specPair = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER_SPEC = (ForgeConfigSpec)specPair.getRight();
        SERVER = (Server)specPair.getLeft();
        Pair cspecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = (ForgeConfigSpec)cspecPair.getRight();
        COMMON = (Common)cspecPair.getLeft();
    }

    public static class Server {
        public ForgeConfigSpec.EnumValue<KeepCurios> keepCurios;
        public ForgeConfigSpec.BooleanValue enableLegacyMenu;
        public ForgeConfigSpec.IntValue minimumColumns;
        public ForgeConfigSpec.IntValue maxSlotsPerPage;

        public Server(ForgeConfigSpec.Builder builder) {
            this.keepCurios = builder.comment("Sets behavior for keeping Curios items on death.\nON - Curios items are kept on death\nDEFAULT - Curios items follow the keepInventory gamerule\nOFF - Curios items are dropped on death").translation("gui.curios.config.keepCurios").defineEnum("keepCurios", (Enum)KeepCurios.DEFAULT);
            builder.push("menu");
            this.enableLegacyMenu = builder.comment("Enables the old legacy Curios menu for better backwards compatibility.").translation("gui.curios.config.enableLegacyMenu").define("enableLegacyMenu", false);
            builder.push("experimental");
            this.minimumColumns = builder.comment("The minimum number of columns for the Curios menu.").translation("gui.curios.config.minimumColumns").defineInRange("minimumColumns", 1, 1, 8);
            this.maxSlotsPerPage = builder.comment("The maximum number of slots per page of the Curios menu.").translation("gui.curios.config.maxSlotsPerPage").defineInRange("maxSlotsPerPage", 48, 1, 48);
            builder.pop();
            builder.pop();
            builder.build();
        }
    }

    public static class Common {
        public ForgeConfigSpec.ConfigValue<List<? extends String>> slots;

        public Common(ForgeConfigSpec.Builder builder) {
            this.slots = builder.comment("List of slots to create or modify.\nSee documentation for syntax: https://docs.illusivesoulworks.com/curios/configuration#slot-configuration\n").translation("gui.curios.config.slots").defineList("slots", List.of(), s -> s instanceof String);
            builder.build();
        }
    }

    public static enum KeepCurios {
        ON,
        DEFAULT,
        OFF;

    }
}

