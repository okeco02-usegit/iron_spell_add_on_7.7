/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.logging.LogUtils
 *  javax.annotation.Nonnull
 *  net.minecraft.advancements.CriteriaTriggers
 *  net.minecraft.advancements.CriterionTrigger
 *  net.minecraft.client.gui.screens.MenuScreens
 *  net.minecraft.client.renderer.entity.LivingEntityRenderer
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.server.packs.resources.PreparableReloadListener
 *  net.minecraft.server.packs.resources.ResourceManager
 *  net.minecraft.server.packs.resources.SimplePreparableReloadListener
 *  net.minecraft.util.profiling.ProfilerFiller
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.client.event.EntityRenderersEvent$AddLayers
 *  net.minecraftforge.client.event.RegisterKeyMappingsEvent
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent
 *  net.minecraftforge.common.crafting.conditions.ICondition$IContext
 *  net.minecraftforge.event.AddReloadListenerEvent
 *  net.minecraftforge.event.RegisterCommandsEvent
 *  net.minecraftforge.event.server.ServerAboutToStartEvent
 *  net.minecraftforge.event.server.ServerStoppedEvent
 *  net.minecraftforge.eventbus.api.IEventBus
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.ModLoadingContext
 *  net.minecraftforge.fml.common.Mod
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 *  net.minecraftforge.fml.config.IConfigSpec
 *  net.minecraftforge.fml.config.ModConfig$Type
 *  net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
 *  net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
 *  net.minecraftforge.fml.event.lifecycle.InterModProcessEvent
 *  net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
 *  org.slf4j.Logger
 */
package top.theillusivec4.curios;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import java.util.HashSet;
import javax.annotation.Nonnull;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.client.ClientEventHandler;
import top.theillusivec4.curios.client.CuriosClientConfig;
import top.theillusivec4.curios.client.IconHelper;
import top.theillusivec4.curios.client.KeyRegistry;
import top.theillusivec4.curios.client.gui.CuriosScreen;
import top.theillusivec4.curios.client.gui.CuriosScreenV2;
import top.theillusivec4.curios.client.gui.GuiEventHandler;
import top.theillusivec4.curios.client.render.CuriosLayer;
import top.theillusivec4.curios.common.CuriosConfig;
import top.theillusivec4.curios.common.CuriosHelper;
import top.theillusivec4.curios.common.CuriosRegistry;
import top.theillusivec4.curios.common.data.CuriosEntityManager;
import top.theillusivec4.curios.common.data.CuriosSlotManager;
import top.theillusivec4.curios.common.event.CuriosEventHandler;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.slottype.LegacySlotManager;
import top.theillusivec4.curios.common.util.EquipCurioTrigger;
import top.theillusivec4.curios.common.util.SetCurioAttributesFunction;
import top.theillusivec4.curios.server.SlotHelper;
import top.theillusivec4.curios.server.command.CurioArgumentType;
import top.theillusivec4.curios.server.command.CuriosCommand;
import top.theillusivec4.curios.server.command.CuriosSelectorOptions;

@Mod(value="curios")
public class Curios {
    public static final String MODID = "curios";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Curios() {
        CuriosRegistry.init();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setup);
        eventBus.addListener(this::process);
        eventBus.addListener(this::registerCaps);
        MinecraftForge.EVENT_BUS.addListener(this::serverAboutToStart);
        MinecraftForge.EVENT_BUS.addListener(this::serverStopped);
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        MinecraftForge.EVENT_BUS.addListener(this::reload);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, (IConfigSpec)CuriosClientConfig.CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, (IConfigSpec)CuriosConfig.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, (IConfigSpec)CuriosConfig.SERVER_SPEC);
    }

    private void setup(FMLCommonSetupEvent evt) {
        CuriosApi.setCuriosHelper(new CuriosHelper());
        MinecraftForge.EVENT_BUS.register((Object)new CuriosEventHandler());
        NetworkHandler.register();
        evt.enqueueWork(() -> {
            CriteriaTriggers.m_10595_((CriterionTrigger)EquipCurioTrigger.INSTANCE);
            CuriosSelectorOptions.register();
            SetCurioAttributesFunction.register();
        });
    }

    private void registerCaps(RegisterCapabilitiesEvent evt) {
        evt.register(ICuriosItemHandler.class);
        evt.register(ICurio.class);
    }

    private void process(InterModProcessEvent evt) {
        LegacySlotManager.buildImcSlotTypes(evt.getIMCStream("register_type"::equals), evt.getIMCStream("modify_type"::equals));
    }

    private void serverAboutToStart(ServerAboutToStartEvent evt) {
        CuriosApi.setSlotHelper(new SlotHelper());
        HashSet<String> slotIds = new HashSet<String>();
        for (ISlotType value : CuriosSlotManager.SERVER.getSlots().values()) {
            CuriosApi.getSlotHelper().addSlotType(value);
            slotIds.add(value.getIdentifier());
        }
        CurioArgumentType.slotIds = slotIds;
    }

    private void serverStopped(ServerStoppedEvent evt) {
        CuriosApi.setSlotHelper(null);
    }

    private void registerCommands(RegisterCommandsEvent evt) {
        CuriosCommand.register((CommandDispatcher<CommandSourceStack>)evt.getDispatcher(), evt.getBuildContext());
    }

    private void reload(AddReloadListenerEvent evt) {
        ICondition.IContext ctx = evt.getConditionContext();
        CuriosSlotManager.SERVER = new CuriosSlotManager(ctx);
        evt.addListener((PreparableReloadListener)CuriosSlotManager.SERVER);
        CuriosEntityManager.SERVER = new CuriosEntityManager(ctx);
        evt.addListener((PreparableReloadListener)CuriosEntityManager.SERVER);
        evt.addListener((PreparableReloadListener)new SimplePreparableReloadListener<Void>(){

            @Nonnull
            protected Void prepare(@Nonnull ResourceManager resourceManagerIn, @Nonnull ProfilerFiller profilerIn) {
                return null;
            }

            protected void apply(@Nonnull Void objectIn, @Nonnull ResourceManager resourceManagerIn, @Nonnull ProfilerFiller profilerIn) {
                CuriosEventHandler.dirtyTags = true;
            }
        });
    }

    @Mod.EventBusSubscriber(modid="curios", value={Dist.CLIENT}, bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientProxy {
        @SubscribeEvent
        public static void registerKeys(RegisterKeyMappingsEvent evt) {
            evt.register(KeyRegistry.openCurios);
        }

        @SubscribeEvent
        public static void setupClient(FMLClientSetupEvent evt) {
            CuriosApi.setIconHelper(new IconHelper());
            MinecraftForge.EVENT_BUS.register((Object)new ClientEventHandler());
            MinecraftForge.EVENT_BUS.register((Object)new GuiEventHandler());
            MenuScreens.m_96206_((MenuType)((MenuType)CuriosRegistry.CURIO_MENU.get()), CuriosScreen::new);
            MenuScreens.m_96206_(CuriosRegistry.CURIO_MENU_NEW.get(), CuriosScreenV2::new);
        }

        @SubscribeEvent
        public static void addLayers(EntityRenderersEvent.AddLayers evt) {
            ClientProxy.addPlayerLayer(evt, "default");
            ClientProxy.addPlayerLayer(evt, "slim");
            CuriosRendererRegistry.load();
        }

        private static void addPlayerLayer(EntityRenderersEvent.AddLayers evt, String skin) {
            LivingEntityRenderer renderer = evt.getSkin(skin);
            if (renderer instanceof LivingEntityRenderer) {
                LivingEntityRenderer livingRenderer = renderer;
                livingRenderer.m_115326_(new CuriosLayer(livingRenderer));
            }
        }
    }
}

