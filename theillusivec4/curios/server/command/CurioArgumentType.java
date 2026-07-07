/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.StringReader
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.brigadier.exceptions.DynamicCommandExceptionType
 *  com.mojang.brigadier.suggestion.Suggestions
 *  com.mojang.brigadier.suggestion.SuggestionsBuilder
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.SharedSuggestionProvider
 *  net.minecraft.network.chat.Component
 */
package top.theillusivec4.curios.server.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import top.theillusivec4.curios.api.CuriosApi;

public class CurioArgumentType
implements ArgumentType<String> {
    public static Set<String> slotIds = new HashSet<String>();
    private static final Collection<String> EXAMPLES = Arrays.asList("ring", "head");
    private static final DynamicCommandExceptionType UNKNOWN_TYPE = new DynamicCommandExceptionType(type -> Component.m_237110_((String)"argument.curios.type.unknown", (Object[])new Object[]{type}));

    public static CurioArgumentType slot() {
        return new CurioArgumentType();
    }

    public static String getSlot(CommandContext<CommandSourceStack> context, String name) {
        return (String)context.getArgument(name, String.class);
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.m_82970_(slotIds, (SuggestionsBuilder)builder);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public String parse(StringReader reader) throws CommandSyntaxException {
        String s = reader.readUnquotedString();
        if (CuriosApi.getSlotHelper() != null && !slotIds.contains(s)) {
            throw UNKNOWN_TYPE.create((Object)s);
        }
        return s;
    }
}

