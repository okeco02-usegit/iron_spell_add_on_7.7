/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.IntegerArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.builder.RequiredArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType
 *  com.mojang.brigadier.exceptions.DynamicCommandExceptionType
 *  com.mojang.brigadier.suggestion.SuggestionProvider
 *  com.mojang.brigadier.suggestion.SuggestionsBuilder
 *  net.minecraft.commands.CommandBuildContext
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.commands.SharedSuggestionProvider
 *  net.minecraft.commands.arguments.EntityArgument
 *  net.minecraft.commands.arguments.ResourceLocationArgument
 *  net.minecraft.commands.arguments.SlotArgument
 *  net.minecraft.commands.arguments.coordinates.BlockPosArgument
 *  net.minecraft.commands.arguments.item.ItemArgument
 *  net.minecraft.commands.arguments.item.ItemInput
 *  net.minecraft.core.BlockPos
 *  net.minecraft.network.chat.Component
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.Container
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.SlotAccess
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.block.entity.BlockEntity
 *  net.minecraft.world.level.storage.loot.LootContext
 *  net.minecraft.world.level.storage.loot.LootContext$Builder
 *  net.minecraft.world.level.storage.loot.LootDataManager
 *  net.minecraft.world.level.storage.loot.LootDataType
 *  net.minecraft.world.level.storage.loot.LootParams
 *  net.minecraft.world.level.storage.loot.LootParams$Builder
 *  net.minecraft.world.level.storage.loot.functions.LootItemFunction
 *  net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
 *  net.minecraft.world.level.storage.loot.parameters.LootContextParams
 *  net.minecraftforge.network.PacketDistributor
 */
package top.theillusivec4.curios.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.SlotArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.network.PacketDistributor;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.common.data.CuriosEntityManager;
import top.theillusivec4.curios.common.data.CuriosSlotManager;
import top.theillusivec4.curios.common.network.NetworkHandler;
import top.theillusivec4.curios.common.network.server.sync.SPacketSyncCurios;
import top.theillusivec4.curios.common.slottype.LegacySlotManager;
import top.theillusivec4.curios.server.command.CurioArgumentType;

public class CuriosCommand {
    private static final Dynamic3CommandExceptionType ERROR_SOURCE_NOT_A_CONTAINER = new Dynamic3CommandExceptionType((p_180347_, p_180348_, p_180349_) -> Component.m_237110_((String)"commands.item.source.not_a_container", (Object[])new Object[]{p_180347_, p_180348_, p_180349_}));
    private static final DynamicCommandExceptionType ERROR_SOURCE_INAPPLICABLE_SLOT = new DynamicCommandExceptionType(p_180353_ -> Component.m_237110_((String)"commands.item.source.no_such_slot", (Object[])new Object[]{p_180353_}));
    private static final SuggestionProvider<CommandSourceStack> SUGGEST_MODIFIER = (p_278910_, p_278911_) -> {
        LootDataManager lootdatamanager = ((CommandSourceStack)p_278910_.getSource()).m_81377_().m_278653_();
        return SharedSuggestionProvider.m_82926_((Iterable)lootdatamanager.m_278706_(LootDataType.f_278496_), (SuggestionsBuilder)p_278911_);
    };

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext) {
        LiteralArgumentBuilder curiosCommand = (LiteralArgumentBuilder)Commands.m_82127_((String)"curios").requires(player -> player.m_6761_(2));
        curiosCommand.then(Commands.m_82127_((String)"list").executes(context -> {
            HashMap<String, Set<String>> map = new HashMap<String, Set<String>>(LegacySlotManager.getIdsToMods());
            for (Map.Entry<String, Set<String>> entry : CuriosSlotManager.SERVER.getModsFromSlots().entrySet()) {
                map.computeIfAbsent(entry.getKey(), k -> new HashSet()).addAll((Collection)entry.getValue());
            }
            for (Map.Entry<String, Set<String>> entry : CuriosEntityManager.SERVER.getModsFromSlots().entrySet()) {
                map.computeIfAbsent(entry.getKey(), k -> new HashSet()).addAll((Collection)entry.getValue());
            }
            for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
                ((CommandSourceStack)context.getSource()).m_288197_(() -> Component.m_237113_((String)((String)entry.getKey() + " - " + String.join((CharSequence)", ", (Iterable)entry.getValue()))), false);
            }
            return 1;
        }));
        curiosCommand.then(Commands.m_82127_((String)"replace").then(Commands.m_82129_((String)"slot", (ArgumentType)CurioArgumentType.slot()).then(Commands.m_82129_((String)"index", (ArgumentType)IntegerArgumentType.integer()).then(((RequiredArgumentBuilder)Commands.m_82129_((String)"player", (ArgumentType)EntityArgument.m_91466_()).then(Commands.m_82127_((String)"with").then(((RequiredArgumentBuilder)Commands.m_82129_((String)"item", (ArgumentType)ItemArgument.m_235279_((CommandBuildContext)buildContext)).executes(context -> CuriosCommand.replaceItemForPlayer((CommandSourceStack)context.getSource(), EntityArgument.m_91474_((CommandContext)context, (String)"player"), CurioArgumentType.getSlot((CommandContext<CommandSourceStack>)context, "slot"), IntegerArgumentType.getInteger((CommandContext)context, (String)"index"), ItemArgument.m_120963_((CommandContext)context, (String)"item")))).then(Commands.m_82129_((String)"count", (ArgumentType)IntegerArgumentType.integer()).executes(context -> CuriosCommand.replaceItemForPlayer((CommandSourceStack)context.getSource(), EntityArgument.m_91474_((CommandContext)context, (String)"player"), CurioArgumentType.getSlot((CommandContext<CommandSourceStack>)context, "slot"), IntegerArgumentType.getInteger((CommandContext)context, (String)"index"), ItemArgument.m_120963_((CommandContext)context, (String)"item"), IntegerArgumentType.getInteger((CommandContext)context, (String)"count"))))))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.m_82127_((String)"from").then(Commands.m_82127_((String)"block").then(Commands.m_82129_((String)"source", (ArgumentType)BlockPosArgument.m_118239_()).then(((RequiredArgumentBuilder)Commands.m_82129_((String)"sourceSlot", (ArgumentType)SlotArgument.m_111276_()).executes(context -> CuriosCommand.blockToSlot((CommandSourceStack)context.getSource(), BlockPosArgument.m_118242_((CommandContext)context, (String)"source"), SlotArgument.m_111279_((CommandContext)context, (String)"sourceSlot"), EntityArgument.m_91474_((CommandContext)context, (String)"player"), CurioArgumentType.getSlot((CommandContext<CommandSourceStack>)context, "slot"), IntegerArgumentType.getInteger((CommandContext)context, (String)"index")))).then(Commands.m_82129_((String)"modifier", (ArgumentType)ResourceLocationArgument.m_106984_()).suggests(SUGGEST_MODIFIER).executes(context -> CuriosCommand.blockToSlot((CommandSourceStack)context.getSource(), BlockPosArgument.m_118242_((CommandContext)context, (String)"source"), SlotArgument.m_111279_((CommandContext)context, (String)"sourceSlot"), ResourceLocationArgument.m_171031_((CommandContext)context, (String)"modifier"), EntityArgument.m_91474_((CommandContext)context, (String)"player"), CurioArgumentType.getSlot((CommandContext<CommandSourceStack>)context, "slot"), IntegerArgumentType.getInteger((CommandContext)context, (String)"index")))))))).then(Commands.m_82127_((String)"entity-vanilla").then(Commands.m_82129_((String)"source", (ArgumentType)EntityArgument.m_91449_()).then(((RequiredArgumentBuilder)Commands.m_82129_((String)"sourceSlot", (ArgumentType)SlotArgument.m_111276_()).executes(context -> CuriosCommand.entityToSlot((CommandSourceStack)context.getSource(), EntityArgument.m_91452_((CommandContext)context, (String)"source"), SlotArgument.m_111279_((CommandContext)context, (String)"sourceSlot"), EntityArgument.m_91474_((CommandContext)context, (String)"player"), CurioArgumentType.getSlot((CommandContext<CommandSourceStack>)context, "slot"), IntegerArgumentType.getInteger((CommandContext)context, (String)"index")))).then(Commands.m_82129_((String)"modifier", (ArgumentType)ResourceLocationArgument.m_106984_()).suggests(SUGGEST_MODIFIER).executes(context -> CuriosCommand.entityToSlot((CommandSourceStack)context.getSource(), EntityArgument.m_91452_((CommandContext)context, (String)"source"), SlotArgument.m_111279_((CommandContext)context, (String)"sourceSlot"), ResourceLocationArgument.m_171031_((CommandContext)context, (String)"modifier"), EntityArgument.m_91474_((CommandContext)context, (String)"player"), CurioArgumentType.getSlot((CommandContext<CommandSourceStack>)context, "slot"), IntegerArgumentType.getInteger((CommandContext)context, (String)"index")))))))).then(Commands.m_82127_((String)"entity-curios").then(Commands.m_82129_((String)"sourceSlot", (ArgumentType)CurioArgumentType.slot()).then(Commands.m_82129_((String)"sourceIndex", (ArgumentType)IntegerArgumentType.integer()).then(((RequiredArgumentBuilder)Commands.m_82129_((String)"source", (ArgumentType)EntityArgument.m_91466_()).executes(context -> CuriosCommand.entityToSlot((CommandSourceStack)context.getSource(), EntityArgument.m_91474_((CommandContext)context, (String)"source"), CurioArgumentType.getSlot((CommandContext<CommandSourceStack>)context, "sourceSlot"), IntegerArgumentType.getInteger((CommandContext)context, (String)"sourceIndex"), EntityArgument.m_91474_((CommandContext)context, (String)"player"), CurioArgumentType.getSlot((CommandContext<CommandSourceStack>)context, "slot"), IntegerArgumentType.getInteger((CommandContext)context, (String)"index")))).then(Commands.m_82129_((String)"modifier", (ArgumentType)ResourceLocationArgument.m_106984_()).suggests(SUGGEST_MODIFIER).executes(context -> CuriosCommand.entityToSlot((CommandSourceStack)context.getSource(), EntityArgument.m_91474_((CommandContext)context, (String)"source"), CurioArgumentType.getSlot((CommandContext<CommandSourceStack>)context, "sourceSlot"), IntegerArgumentType.getInteger((CommandContext)context, (String)"sourceIndex"), ResourceLocationArgument.m_171031_((CommandContext)context, (String)"modifier"), EntityArgument.m_91474_((CommandContext)context, (String)"player"), CurioArgumentType.getSlot((CommandContext<CommandSourceStack>)context, "slot"), IntegerArgumentType.getInteger((CommandContext)context, (String)"index")))))))))))));
        curiosCommand.then(Commands.m_82127_((String)"set").then(Commands.m_82129_((String)"slot", (ArgumentType)CurioArgumentType.slot()).then(((RequiredArgumentBuilder)Commands.m_82129_((String)"player", (ArgumentType)EntityArgument.m_91466_()).executes(context -> CuriosCommand.setSlotsForPlayer((CommandSourceStack)context.getSource(), EntityArgument.m_91474_((CommandContext)context, (String)"player"), CurioArgumentType.getSlot((CommandContext<CommandSourceStack>)context, "slot"), 1))).then(Commands.m_82129_((String)"amount", (ArgumentType)IntegerArgumentType.integer()).executes(context -> CuriosCommand.setSlotsForPlayer((CommandSourceStack)context.getSource(), EntityArgument.m_91474_((CommandContext)context, (String)"player"), CurioArgumentType.getSlot((CommandContext<CommandSourceStack>)context, "slot"), IntegerArgumentType.getInteger((CommandContext)context, (String)"amount")))))));
        curiosCommand.then(Commands.m_82127_((String)"add").then(Commands.m_82129_((String)"slot", (ArgumentType)CurioArgumentType.slot()).then(((RequiredArgumentBuilder)Commands.m_82129_((String)"player", (ArgumentType)EntityArgument.m_91466_()).executes(context -> CuriosCommand.growSlotForPlayer((CommandSourceStack)context.getSource(), EntityArgument.m_91474_((CommandContext)context, (String)"player"), CurioArgumentType.getSlot((CommandContext<CommandSourceStack>)context, "slot"), 1))).then(Commands.m_82129_((String)"amount", (ArgumentType)IntegerArgumentType.integer()).executes(context -> CuriosCommand.growSlotForPlayer((CommandSourceStack)context.getSource(), EntityArgument.m_91474_((CommandContext)context, (String)"player"), CurioArgumentType.getSlot((CommandContext<CommandSourceStack>)context, "slot"), IntegerArgumentType.getInteger((CommandContext)context, (String)"amount")))))));
        curiosCommand.then(Commands.m_82127_((String)"remove").then(Commands.m_82129_((String)"slot", (ArgumentType)CurioArgumentType.slot()).then(((RequiredArgumentBuilder)Commands.m_82129_((String)"player", (ArgumentType)EntityArgument.m_91466_()).executes(context -> CuriosCommand.shrinkSlotForPlayer((CommandSourceStack)context.getSource(), EntityArgument.m_91474_((CommandContext)context, (String)"player"), CurioArgumentType.getSlot((CommandContext<CommandSourceStack>)context, "slot"), 1))).then(Commands.m_82129_((String)"amount", (ArgumentType)IntegerArgumentType.integer()).executes(context -> CuriosCommand.shrinkSlotForPlayer((CommandSourceStack)context.getSource(), EntityArgument.m_91474_((CommandContext)context, (String)"player"), CurioArgumentType.getSlot((CommandContext<CommandSourceStack>)context, "slot"), IntegerArgumentType.getInteger((CommandContext)context, (String)"amount")))))));
        curiosCommand.then(Commands.m_82127_((String)"clear").then(((RequiredArgumentBuilder)Commands.m_82129_((String)"player", (ArgumentType)EntityArgument.m_91466_()).executes(context -> CuriosCommand.clearSlotsForPlayer((CommandSourceStack)context.getSource(), EntityArgument.m_91474_((CommandContext)context, (String)"player"), ""))).then(Commands.m_82129_((String)"slot", (ArgumentType)CurioArgumentType.slot()).executes(context -> CuriosCommand.clearSlotsForPlayer((CommandSourceStack)context.getSource(), EntityArgument.m_91474_((CommandContext)context, (String)"player"), CurioArgumentType.getSlot((CommandContext<CommandSourceStack>)context, "slot"))))));
        curiosCommand.then(Commands.m_82127_((String)"drop").then(((RequiredArgumentBuilder)Commands.m_82129_((String)"player", (ArgumentType)EntityArgument.m_91466_()).executes(context -> CuriosCommand.dropSlotsForPlayer((CommandSourceStack)context.getSource(), EntityArgument.m_91474_((CommandContext)context, (String)"player"), ""))).then(Commands.m_82129_((String)"slot", (ArgumentType)CurioArgumentType.slot()).executes(context -> CuriosCommand.dropSlotsForPlayer((CommandSourceStack)context.getSource(), EntityArgument.m_91474_((CommandContext)context, (String)"player"), CurioArgumentType.getSlot((CommandContext<CommandSourceStack>)context, "slot"))))));
        curiosCommand.then(Commands.m_82127_((String)"reset").then(Commands.m_82129_((String)"player", (ArgumentType)EntityArgument.m_91466_()).executes(context -> CuriosCommand.resetSlotsForPlayer((CommandSourceStack)context.getSource(), EntityArgument.m_91474_((CommandContext)context, (String)"player")))));
        dispatcher.register(curiosCommand);
    }

    private static int entityToSlot(CommandSourceStack source, ServerPlayer sourcePlayer, String sourceSlot, int sourceIndex, LootItemFunction lootFunction, ServerPlayer player, String slot, int index) {
        return CuriosCommand.replaceItemForPlayer(source, player, slot, index, CuriosCommand.applyModifier(source, lootFunction, CuriosCommand.getEntityItem(sourcePlayer, sourceSlot, sourceIndex)));
    }

    private static int entityToSlot(CommandSourceStack source, ServerPlayer sourcePlayer, String sourceSlot, int sourceIndex, ServerPlayer player, String slot, int index) {
        return CuriosCommand.replaceItemForPlayer(source, player, slot, index, CuriosCommand.getEntityItem(sourcePlayer, sourceSlot, sourceIndex));
    }

    private static int entityToSlot(CommandSourceStack source, Entity entity, int sourceSlot, LootItemFunction lootFunction, ServerPlayer player, String slot, int index) throws CommandSyntaxException {
        return CuriosCommand.replaceItemForPlayer(source, player, slot, index, CuriosCommand.applyModifier(source, lootFunction, CuriosCommand.getEntityItem(entity, sourceSlot)));
    }

    private static int entityToSlot(CommandSourceStack source, Entity entity, int sourceSlot, ServerPlayer player, String slot, int index) throws CommandSyntaxException {
        return CuriosCommand.replaceItemForPlayer(source, player, slot, index, CuriosCommand.getEntityItem(entity, sourceSlot));
    }

    private static int blockToSlot(CommandSourceStack source, BlockPos pos, int sourceSlot, LootItemFunction lootFunction, ServerPlayer player, String slot, int index) throws CommandSyntaxException {
        return CuriosCommand.replaceItemForPlayer(source, player, slot, index, CuriosCommand.applyModifier(source, lootFunction, CuriosCommand.getBlockItem(source, pos, sourceSlot)));
    }

    private static int blockToSlot(CommandSourceStack source, BlockPos pos, int sourceSlot, ServerPlayer player, String slot, int index) throws CommandSyntaxException {
        return CuriosCommand.replaceItemForPlayer(source, player, slot, index, CuriosCommand.getBlockItem(source, pos, sourceSlot));
    }

    private static ItemStack getBlockItem(CommandSourceStack source, BlockPos pos, int slot) throws CommandSyntaxException {
        Container container = CuriosCommand.getContainer(source, pos);
        if (slot >= 0 && slot < container.m_6643_()) {
            return container.m_8020_(slot).m_41777_();
        }
        throw ERROR_SOURCE_INAPPLICABLE_SLOT.create((Object)slot);
    }

    private static ItemStack getEntityItem(ServerPlayer player, String slot, int index) {
        ItemStack[] stack = new ItemStack[]{ItemStack.f_41583_};
        CuriosApi.getCuriosInventory((LivingEntity)player).resolve().flatMap(inv -> inv.findCurio(slot, index)).ifPresent(slotResult -> {
            stack[0] = slotResult.stack().m_41777_();
        });
        return stack[0];
    }

    private static ItemStack getEntityItem(Entity entity, int slot) throws CommandSyntaxException {
        SlotAccess slotaccess = entity.m_141942_(slot);
        if (slotaccess == SlotAccess.f_147290_) {
            throw ERROR_SOURCE_INAPPLICABLE_SLOT.create((Object)slot);
        }
        return slotaccess.m_142196_().m_41777_();
    }

    static Container getContainer(CommandSourceStack source, BlockPos pos) throws CommandSyntaxException {
        BlockEntity blockentity = source.m_81372_().m_7702_(pos);
        if (blockentity instanceof Container) {
            Container container = (Container)blockentity;
            return container;
        }
        throw ERROR_SOURCE_NOT_A_CONTAINER.create((Object)pos.m_123341_(), (Object)pos.m_123342_(), (Object)pos.m_123343_());
    }

    private static ItemStack applyModifier(CommandSourceStack source, LootItemFunction lootFunction, ItemStack stack) {
        ServerLevel serverlevel = source.m_81372_();
        LootParams lootparams = new LootParams.Builder(serverlevel).m_287286_(LootContextParams.f_81460_, (Object)source.m_81371_()).m_287289_(LootContextParams.f_81455_, (Object)source.m_81373_()).m_287235_(LootContextParamSets.f_81412_);
        LootContext lootcontext = new LootContext.Builder(lootparams).m_287259_(null);
        lootcontext.m_278759_(LootContext.m_278853_((LootItemFunction)lootFunction));
        return (ItemStack)lootFunction.apply((Object)stack, (Object)lootcontext);
    }

    private static int replaceItemForPlayer(CommandSourceStack source, ServerPlayer player, String slot, int index, ItemStack stack) {
        CuriosApi.getCuriosInventory((LivingEntity)player).ifPresent(inv -> inv.setEquippedCurio(slot, index, stack));
        source.m_288197_(() -> Component.m_237110_((String)"commands.curios.replace.success", (Object[])new Object[]{slot, player.m_5446_(), stack.m_41611_()}), true);
        return 1;
    }

    private static int replaceItemForPlayer(CommandSourceStack source, ServerPlayer player, String slot, int index, ItemInput item) throws CommandSyntaxException {
        return CuriosCommand.replaceItemForPlayer(source, player, slot, index, item, 1);
    }

    private static int replaceItemForPlayer(CommandSourceStack source, ServerPlayer player, String slot, int index, ItemInput item, int count) throws CommandSyntaxException {
        ItemStack stack = item.m_120980_(count, false);
        CuriosApi.getCuriosHelper().setEquippedCurio((LivingEntity)player, slot, index, stack);
        source.m_288197_(() -> Component.m_237110_((String)"commands.curios.replace.success", (Object[])new Object[]{slot, player.m_5446_(), stack.m_41611_()}), true);
        return 1;
    }

    private static int setSlotsForPlayer(CommandSourceStack source, ServerPlayer playerMP, String slot, int amount) {
        CuriosApi.getSlotHelper().setSlotsForType(slot, (LivingEntity)playerMP, amount);
        source.m_288197_(() -> Component.m_237110_((String)"commands.curios.set.success", (Object[])new Object[]{slot, CuriosApi.getSlotHelper().getSlotsForType((LivingEntity)playerMP, slot), playerMP.m_5446_()}), true);
        return 1;
    }

    private static int growSlotForPlayer(CommandSourceStack source, ServerPlayer playerMP, String slot, int amount) {
        CuriosApi.getSlotHelper().growSlotType(slot, amount, (LivingEntity)playerMP);
        source.m_288197_(() -> Component.m_237110_((String)"commands.curios.add.success", (Object[])new Object[]{amount, slot, playerMP.m_5446_()}), true);
        return 1;
    }

    private static int shrinkSlotForPlayer(CommandSourceStack source, ServerPlayer playerMP, String slot, int amount) {
        CuriosApi.getSlotHelper().shrinkSlotType(slot, amount, (LivingEntity)playerMP);
        source.m_288197_(() -> Component.m_237110_((String)"commands.curios.remove.success", (Object[])new Object[]{amount, slot, playerMP.m_5446_()}), true);
        return 1;
    }

    private static int dropSlotsForPlayer(CommandSourceStack source, ServerPlayer playerMP, String slot) {
        CuriosApi.getCuriosHelper().getCuriosHandler((LivingEntity)playerMP).ifPresent(handler -> {
            Map<String, ICurioStacksHandler> curios = handler.getCurios();
            if (!slot.isEmpty() && curios.get(slot) != null) {
                CuriosCommand.drop(curios.get(slot), playerMP);
            } else {
                for (String id : curios.keySet()) {
                    CuriosCommand.drop(curios.get(id), playerMP);
                }
            }
        });
        if (slot.isEmpty()) {
            source.m_288197_(() -> Component.m_237110_((String)"commands.curios.dropAll.success", (Object[])new Object[]{playerMP.m_5446_()}), true);
        } else {
            source.m_288197_(() -> Component.m_237110_((String)"commands.curios.drop.success", (Object[])new Object[]{slot, playerMP.m_5446_()}), true);
        }
        return 1;
    }

    private static void drop(ICurioStacksHandler stacksHandler, ServerPlayer serverPlayer) {
        for (int i = 0; i < stacksHandler.getSlots(); ++i) {
            ItemStack stack1 = stacksHandler.getStacks().getStackInSlot(i);
            stacksHandler.getStacks().setStackInSlot(i, ItemStack.f_41583_);
            ItemStack stack2 = stacksHandler.getCosmeticStacks().getStackInSlot(i);
            stacksHandler.getCosmeticStacks().setStackInSlot(i, ItemStack.f_41583_);
            if (!stack1.m_41619_()) {
                serverPlayer.m_7197_(stack1, true, false);
            }
            if (stack2.m_41619_()) continue;
            serverPlayer.m_7197_(stack2, true, false);
        }
    }

    private static int clearSlotsForPlayer(CommandSourceStack source, ServerPlayer playerMP, String slot) {
        CuriosApi.getCuriosHelper().getCuriosHandler((LivingEntity)playerMP).ifPresent(handler -> {
            Map<String, ICurioStacksHandler> curios = handler.getCurios();
            if (!slot.isEmpty() && curios.get(slot) != null) {
                CuriosCommand.clear(curios.get(slot));
            } else {
                for (String id : curios.keySet()) {
                    CuriosCommand.clear(curios.get(id));
                }
            }
        });
        if (slot.isEmpty()) {
            source.m_288197_(() -> Component.m_237110_((String)"commands.curios.clearAll.success", (Object[])new Object[]{playerMP.m_5446_()}), true);
        } else {
            source.m_288197_(() -> Component.m_237110_((String)"commands.curios.clear.success", (Object[])new Object[]{slot, playerMP.m_5446_()}), true);
        }
        return 1;
    }

    private static int resetSlotsForPlayer(CommandSourceStack source, ServerPlayer playerMP) {
        CuriosApi.getCuriosHelper().getCuriosHandler((LivingEntity)playerMP).ifPresent(handler -> {
            handler.reset();
            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> playerMP), (Object)new SPacketSyncCurios(playerMP.m_19879_(), handler.getCurios()));
        });
        source.m_288197_(() -> Component.m_237110_((String)"commands.curios.reset.success", (Object[])new Object[]{playerMP.m_5446_()}), true);
        return 1;
    }

    private static void clear(ICurioStacksHandler stacksHandler) {
        for (int i = 0; i < stacksHandler.getSlots(); ++i) {
            stacksHandler.getStacks().setStackInSlot(i, ItemStack.f_41583_);
            stacksHandler.getCosmeticStacks().setStackInSlot(i, ItemStack.f_41583_);
        }
    }
}

