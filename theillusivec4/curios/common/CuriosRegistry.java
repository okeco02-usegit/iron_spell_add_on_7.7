/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.commands.synchronization.ArgumentTypeInfo
 *  net.minecraft.commands.synchronization.ArgumentTypeInfos
 *  net.minecraft.commands.synchronization.SingletonArgumentInfo
 *  net.minecraft.core.registries.Registries
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraftforge.common.extensions.IForgeMenuType
 *  net.minecraftforge.eventbus.api.IEventBus
 *  net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
 *  net.minecraftforge.registries.DeferredRegister
 *  net.minecraftforge.registries.RegistryObject
 */
package top.theillusivec4.curios.common;

import java.util.function.Supplier;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.curios.common.inventory.container.CuriosContainer;
import top.theillusivec4.curios.common.inventory.container.CuriosContainerV2;
import top.theillusivec4.curios.server.command.CurioArgumentType;

public class CuriosRegistry {
    private static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES = DeferredRegister.create((ResourceKey)Registries.f_256982_, (String)"curios");
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create((ResourceKey)Registries.f_256798_, (String)"curios");
    public static final RegistryObject<ArgumentTypeInfo<?, ?>> CURIO_SLOT_ARGUMENT = ARGUMENT_TYPES.register("slot_type", () -> ArgumentTypeInfos.registerByClass(CurioArgumentType.class, (ArgumentTypeInfo)SingletonArgumentInfo.m_235451_(CurioArgumentType::slot)));
    public static final RegistryObject<MenuType<CuriosContainer>> CURIO_MENU = MENU_TYPES.register("curios_container", () -> IForgeMenuType.create(CuriosContainer::new));
    public static final Supplier<MenuType<CuriosContainerV2>> CURIO_MENU_NEW = MENU_TYPES.register("curios_container_v2", () -> IForgeMenuType.create(CuriosContainerV2::new));

    public static void init() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ARGUMENT_TYPES.register(eventBus);
        MENU_TYPES.register(eventBus);
    }
}

