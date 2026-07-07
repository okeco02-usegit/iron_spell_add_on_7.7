/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraftforge.network.NetworkEvent$Context
 *  net.minecraftforge.network.NetworkRegistry$ChannelBuilder
 *  net.minecraftforge.network.simple.SimpleChannel
 */
package top.theillusivec4.curios.common.network;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import top.theillusivec4.curios.common.network.client.CPacketDestroy;
import top.theillusivec4.curios.common.network.client.CPacketOpenCurios;
import top.theillusivec4.curios.common.network.client.CPacketOpenVanilla;
import top.theillusivec4.curios.common.network.client.CPacketPage;
import top.theillusivec4.curios.common.network.client.CPacketScroll;
import top.theillusivec4.curios.common.network.client.CPacketToggleCosmetics;
import top.theillusivec4.curios.common.network.client.CPacketToggleRender;
import top.theillusivec4.curios.common.network.server.SPacketBreak;
import top.theillusivec4.curios.common.network.server.SPacketGrabbedItem;
import top.theillusivec4.curios.common.network.server.SPacketPage;
import top.theillusivec4.curios.common.network.server.SPacketQuickMove;
import top.theillusivec4.curios.common.network.server.SPacketScroll;
import top.theillusivec4.curios.common.network.server.SPacketSetIcons;
import top.theillusivec4.curios.common.network.server.sync.SPacketSyncCurios;
import top.theillusivec4.curios.common.network.server.sync.SPacketSyncData;
import top.theillusivec4.curios.common.network.server.sync.SPacketSyncModifiers;
import top.theillusivec4.curios.common.network.server.sync.SPacketSyncRender;
import top.theillusivec4.curios.common.network.server.sync.SPacketSyncStack;

public class NetworkHandler {
    private static final String PTC_VERSION = "1";
    public static SimpleChannel INSTANCE;
    private static int id;

    public static void register() {
        INSTANCE = NetworkRegistry.ChannelBuilder.named((ResourceLocation)new ResourceLocation("curios", "main")).networkProtocolVersion(() -> PTC_VERSION).clientAcceptedVersions(PTC_VERSION::equals).serverAcceptedVersions(PTC_VERSION::equals).simpleChannel();
        NetworkHandler.register(CPacketOpenCurios.class, CPacketOpenCurios::encode, CPacketOpenCurios::decode, CPacketOpenCurios::handle);
        NetworkHandler.register(CPacketOpenVanilla.class, CPacketOpenVanilla::encode, CPacketOpenVanilla::decode, CPacketOpenVanilla::handle);
        NetworkHandler.register(CPacketScroll.class, CPacketScroll::encode, CPacketScroll::decode, CPacketScroll::handle);
        NetworkHandler.register(CPacketDestroy.class, CPacketDestroy::encode, CPacketDestroy::decode, CPacketDestroy::handle);
        NetworkHandler.register(CPacketToggleRender.class, CPacketToggleRender::encode, CPacketToggleRender::decode, CPacketToggleRender::handle);
        NetworkHandler.register(CPacketPage.class, CPacketPage::encode, CPacketPage::decode, CPacketPage::handle);
        NetworkHandler.register(CPacketToggleCosmetics.class, CPacketToggleCosmetics::encode, CPacketToggleCosmetics::decode, CPacketToggleCosmetics::handle);
        NetworkHandler.register(SPacketSyncStack.class, SPacketSyncStack::encode, SPacketSyncStack::decode, SPacketSyncStack::handle);
        NetworkHandler.register(SPacketScroll.class, SPacketScroll::encode, SPacketScroll::decode, SPacketScroll::handle);
        NetworkHandler.register(SPacketSyncCurios.class, SPacketSyncCurios::encode, SPacketSyncCurios::decode, SPacketSyncCurios::handle);
        NetworkHandler.register(SPacketBreak.class, SPacketBreak::encode, SPacketBreak::decode, SPacketBreak::handle);
        NetworkHandler.register(SPacketGrabbedItem.class, SPacketGrabbedItem::encode, SPacketGrabbedItem::decode, SPacketGrabbedItem::handle);
        NetworkHandler.register(SPacketSetIcons.class, SPacketSetIcons::encode, SPacketSetIcons::decode, SPacketSetIcons::handle);
        NetworkHandler.register(SPacketSyncRender.class, SPacketSyncRender::encode, SPacketSyncRender::decode, SPacketSyncRender::handle);
        NetworkHandler.register(SPacketSyncModifiers.class, SPacketSyncModifiers::encode, SPacketSyncModifiers::decode, SPacketSyncModifiers::handle);
        NetworkHandler.register(SPacketSyncData.class, SPacketSyncData::encode, SPacketSyncData::decode, SPacketSyncData::handle);
        NetworkHandler.register(SPacketPage.class, SPacketPage::encode, SPacketPage::decode, SPacketPage::handle);
        NetworkHandler.register(SPacketQuickMove.class, SPacketQuickMove::encode, SPacketQuickMove::decode, SPacketQuickMove::handle);
    }

    private static <M> void register(Class<M> messageType, BiConsumer<M, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, M> decoder, BiConsumer<M, Supplier<NetworkEvent.Context>> messageConsumer) {
        INSTANCE.registerMessage(id++, messageType, encoder, decoder, messageConsumer);
    }

    static {
        id = 0;
    }
}

