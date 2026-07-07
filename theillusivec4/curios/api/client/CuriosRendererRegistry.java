/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.Item
 */
package top.theillusivec4.curios.api.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class CuriosRendererRegistry {
    private static final Map<Item, Supplier<ICurioRenderer>> RENDERER_REGISTRY = new ConcurrentHashMap<Item, Supplier<ICurioRenderer>>();
    private static final Map<Item, ICurioRenderer> RENDERERS = new HashMap<Item, ICurioRenderer>();

    public static void register(Item item, Supplier<ICurioRenderer> renderer) {
        RENDERER_REGISTRY.put(item, renderer);
    }

    public static Optional<ICurioRenderer> getRenderer(Item item) {
        return Optional.ofNullable(RENDERERS.get(item));
    }

    public static void load() {
        for (Map.Entry<Item, Supplier<ICurioRenderer>> entry : RENDERER_REGISTRY.entrySet()) {
            RENDERERS.put(entry.getKey(), entry.getValue().get());
        }
    }
}

