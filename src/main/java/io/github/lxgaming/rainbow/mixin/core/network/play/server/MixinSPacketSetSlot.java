/*
 * Copyright 2019 Alex Thomson
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

package io.github.lxgaming.rainbow.mixin.core.network.play.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketSetSlot;
import io.github.lxgaming.rainbow.interfaces.network.play.server.IMixinSPacketSetSlot;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = SPacketSetSlot.class, priority = 1337)
@Implements(@Interface(iface = IMixinSPacketSetSlot.class, prefix = "rainbow$"))
public abstract class MixinSPacketSetSlot {
    
    @Shadow
    private int windowId;
    
    @Shadow
    private int slot;
    
    @Shadow
    private ItemStack item;
    
    public boolean rainbow$populate(EntityPlayer entityPlayer, ItemStack itemStack) {
        if (itemStack.isEmpty() || !(itemStack.getItem() instanceof ItemArmor)) {
            return false;
        }
        
        this.windowId = -2;
        this.slot = entityPlayer.inventory.mainInventory.size() + ((ItemArmor) itemStack.getItem()).armorType.getIndex();
        this.item = itemStack;
        return true;
    }
}