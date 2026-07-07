/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  net.minecraftforge.fml.InterModComms$IMCMessage
 */
package top.theillusivec4.curios.common.slottype;

import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;
import net.minecraftforge.fml.InterModComms;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.common.slottype.SlotType;

public class LegacySlotManager {
    private static final Map<String, SlotType.Builder> IMC_BUILDERS = new HashMap<String, SlotType.Builder>();
    private static final Map<String, Set<String>> IDS_TO_MODS = new HashMap<String, Set<String>>();

    public static Map<String, Set<String>> getIdsToMods() {
        return ImmutableMap.copyOf(IDS_TO_MODS);
    }

    public static Map<String, SlotType.Builder> getImcBuilders() {
        return ImmutableMap.copyOf(IMC_BUILDERS);
    }

    public static void buildImcSlotTypes(Stream<InterModComms.IMCMessage> register, Stream<InterModComms.IMCMessage> modify) {
        IMC_BUILDERS.clear();
        LegacySlotManager.processImc(register, true);
        LegacySlotManager.processImc(modify, false);
    }

    private static void processImc(Stream<InterModComms.IMCMessage> messages, boolean create) {
        TreeMap messageMap = new TreeMap();
        List<InterModComms.IMCMessage> messageList = messages.toList();
        messageList.forEach(msg -> {
            Object firstChild;
            Iterable iterable;
            Iterator iter;
            Object messageObject = msg.messageSupplier().get();
            if (messageObject instanceof SlotTypeMessage) {
                messageMap.computeIfAbsent(msg.senderModId(), k -> new ArrayList()).add((SlotTypeMessage)messageObject);
            } else if (messageObject instanceof Iterable && (iter = (iterable = (Iterable)messageObject).iterator()).hasNext() && (firstChild = iter.next()) instanceof SlotTypeMessage) {
                messageMap.computeIfAbsent(msg.senderModId(), k -> new ArrayList()).add((SlotTypeMessage)firstChild);
                iter.forEachRemaining(child -> messageMap.computeIfAbsent(msg.senderModId(), k -> new ArrayList()).add((SlotTypeMessage)child));
            }
        });
        for (Map.Entry entry : messageMap.entrySet()) {
            String modId = (String)entry.getKey();
            for (SlotTypeMessage msg2 : (List)entry.getValue()) {
                SlotTypeMessage presetMsg;
                String id = msg2.getIdentifier();
                SlotType.Builder builder = IMC_BUILDERS.get(id);
                if (builder == null && create) {
                    builder = new SlotType.Builder(id);
                    IMC_BUILDERS.put(id, builder);
                    IDS_TO_MODS.computeIfAbsent(id, k -> new HashSet()).add(modId);
                }
                if (builder == null) continue;
                builder.size(msg2.getSize()).useNativeGui(msg2.isVisible()).hasCosmetic(msg2.hasCosmetic());
                SlotTypeMessage.Builder preset = SlotTypePreset.findPreset(id).map(SlotTypePreset::getMessageBuilder).orElse(null);
                SlotTypeMessage slotTypeMessage = presetMsg = preset != null ? preset.build() : null;
                if (msg2.getIcon() == null && presetMsg != null && presetMsg.getIcon() != null) {
                    builder.icon(presetMsg.getIcon());
                } else if (msg2.getIcon() != null) {
                    builder.icon(msg2.getIcon());
                }
                if (msg2.getPriority() == null && presetMsg != null && presetMsg.getPriority() != null) {
                    builder.order(presetMsg.getPriority());
                    continue;
                }
                if (msg2.getPriority() == null) continue;
                builder.order(msg2.getPriority());
            }
        }
    }
}

