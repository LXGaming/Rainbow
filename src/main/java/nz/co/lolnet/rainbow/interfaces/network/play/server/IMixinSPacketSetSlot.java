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

package nz.co.lolnet.rainbow.interfaces.network.play.server;

import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.common.entity.EntityUtil;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;

public interface IMixinSPacketSetSlot {
    
    default boolean populate(Player player, ItemStack itemStack) {
        return populate(EntityUtil.toNative(player), ItemStackUtil.toNative(itemStack));
    }
    
    default boolean populate(EntityPlayer entityPlayer, ItemStack itemStack) {
        return populate(entityPlayer, ItemStackUtil.toNative(itemStack));
    }
    
    default boolean populate(Player player, net.minecraft.item.ItemStack itemStack) {
        return populate(EntityUtil.toNative(player), itemStack);
    }
    
    boolean populate(EntityPlayer entityPlayer, net.minecraft.item.ItemStack itemStack);
}