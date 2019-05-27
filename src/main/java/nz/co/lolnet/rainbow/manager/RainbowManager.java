/*
 * Copyright 2019 lolnet.co.nz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nz.co.lolnet.rainbow.manager;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.item.ItemArmor;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.network.play.server.SPacketSetSlot;
import net.minecraft.world.WorldServer;
import nz.co.lolnet.rainbow.Rainbow;
import nz.co.lolnet.rainbow.configuration.Config;
import nz.co.lolnet.rainbow.data.RainbowData;
import nz.co.lolnet.rainbow.interfaces.network.play.server.IMixinSPacketEntityEquipment;
import nz.co.lolnet.rainbow.interfaces.network.play.server.IMixinSPacketSetSlot;
import nz.co.lolnet.rainbow.util.Toolbox;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.Color;
import org.spongepowered.common.data.util.NbtDataUtil;
import org.spongepowered.common.entity.EntityUtil;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public final class RainbowManager {
    
    private static final Color DEFAULT_COLOR = Color.ofRgb(10511680);
    private static final Map<UUID, Float[]> TRACKING = Maps.newHashMap();
    private static final Set<ItemType> VALID_ITEM_TYPES = Sets.newHashSet(ItemTypes.LEATHER_HELMET, ItemTypes.LEATHER_CHESTPLATE, ItemTypes.LEATHER_LEGGINGS, ItemTypes.LEATHER_BOOTS);
    
    public static ItemStack createItem(Player player, ItemType itemType) {
        ItemStack itemStack = ItemStack.of(itemType, 1);
        itemStack.offer(Keys.HIDE_UNBREAKABLE, true);
        itemStack.offer(Keys.UNBREAKABLE, true);
        itemStack.offer(Keys.DISPLAY_NAME, Text.of(
                TextColors.RED, "R",
                TextColors.GOLD, "a",
                TextColors.YELLOW, "i",
                TextColors.GREEN, "n",
                TextColors.AQUA, "b",
                TextColors.BLUE, "o",
                TextColors.DARK_PURPLE, "w")
        );
        
        itemStack.offer(Keys.ITEM_LORE, Lists.newArrayList(Text.of(TextColors.GRAY, "Owner: ", TextColors.WHITE, player.getName())));
        itemStack.offer(new RainbowData(player.getUniqueId(), 0.0F));
        return itemStack;
    }
    
    public static void loadHue(Player player, ItemStack itemStack) {
        if (!getValidItemTypes().contains(itemStack.getType())) {
            return;
        }
        
        RainbowData rainbowData = itemStack.get(RainbowData.class).orElse(null);
        if (rainbowData == null) {
            return;
        }
        
        if (!player.getUniqueId().equals(rainbowData.getUniqueId())) {
            return;
        }
        
        getTracking().computeIfAbsent(player.getUniqueId(), value -> Toolbox.fillArray(new Float[4], 0.0F))[getArmorIndex(itemStack)] = rainbowData.getHue();
        sendUpdate(player, itemStack, Toolbox.HueToColor(rainbowData.getHue()));
    }
    
    public static void saveHue(Player player, ItemStack itemStack) {
        RainbowData rainbowData = getRainbowData(itemStack).orElse(null);
        if (rainbowData == null) {
            return;
        }
        
        if (!player.getUniqueId().equals(rainbowData.getUniqueId())) {
            return;
        }
        
        Float[] values = getTracking().get(player.getUniqueId());
        if (values != null) {
            rainbowData.setHue(values[getArmorIndex(itemStack)]);
            itemStack.offer(rainbowData);
        }
    }
    
    public static void updateHue(Player player, ItemStack itemStack) {
        if (!getValidItemTypes().contains(itemStack.getType())) {
            return;
        }
        
        if (!itemStack.get(RainbowData.class).map(RainbowData::getUniqueId).map(player.getUniqueId()::equals).orElse(false)) {
            return;
        }
        
        Float[] values = getTracking().computeIfAbsent(player.getUniqueId(), uniqueId -> new Float[4]);
        int index = getArmorIndex(itemStack);
        
        float rainbowSpeed = getRainbowSpeed(player).orElse(0.0F);
        if (rainbowSpeed > 0.0F && rainbowSpeed < 360.0F) {
            float newValue = values[index];
            newValue += rainbowSpeed;
            if (newValue > 360) {
                // Ensure the value isn't above 360
                newValue = Math.min(newValue - 360, 360);
            }
            
            values[index] = newValue;
            sendUpdate(player, itemStack, Toolbox.HueToColor(newValue));
        } else {
            Rainbow.getInstance().getLogger().info("Outside range");
        }
    }
    
    public static void sendUpdate(Player player, ItemStack itemStack, Color color) {
        // ItemStack must be copied to prevent Minecraft from detecting the change.
        net.minecraft.item.ItemStack nativeItemStack = ItemStackUtil.toNative(itemStack).copy();
        NbtDataUtil.setColorToNbt(nativeItemStack, color);
        
        SPacketEntityEquipment entityEquipmentPacket = new SPacketEntityEquipment();
        SPacketSetSlot setSlotPacket = new SPacketSetSlot();
        
        // Use alternative method to prevent copying the ItemStack twice
        Toolbox.cast(entityEquipmentPacket, IMixinSPacketEntityEquipment.class).populate(player, nativeItemStack);
        Toolbox.cast(setSlotPacket, IMixinSPacketSetSlot.class).populate(player, nativeItemStack);
        
        ((WorldServer) player.getWorld()).getEntityTracker().sendToTracking(EntityUtil.toNative(player), entityEquipmentPacket);
        ((NetHandlerPlayServer) player.getConnection()).sendPacket(setSlotPacket);
    }
    
    public static Optional<RainbowData> getRainbowData(ItemStack itemStack) {
        if (getValidItemTypes().contains(itemStack.getType())) {
            return itemStack.get(RainbowData.class);
        }
        
        return Optional.empty();
    }
    
    private static int getArmorIndex(ItemStack itemStack) {
        return ((ItemArmor) ItemStackUtil.toNative(itemStack).getItem()).armorType.getIndex();
    }
    
    private static Optional<Float> getRainbowSpeed(Player player) {
        Optional<String> option = Toolbox.getOptionFromSubject(player, "rainbow-speed");
        if (option.isPresent()) {
            Optional<Float> rainbowSpeed = option.flatMap(Toolbox::parseFloat);
            if (rainbowSpeed.isPresent()) {
                return rainbowSpeed;
            }
            
            Rainbow.getInstance().getLogger().warn("Failed to parse rainbow speed for {} ({})", player.getName(), player.getUniqueId());
        }
        
        return Rainbow.getInstance().getConfig().map(Config::getDefaultRainbowSpeed);
    }
    
    public static Color getDefaultColor() {
        return DEFAULT_COLOR;
    }
    
    public static Map<UUID, Float[]> getTracking() {
        return TRACKING;
    }
    
    public static Set<ItemType> getValidItemTypes() {
        return VALID_ITEM_TYPES;
    }
}