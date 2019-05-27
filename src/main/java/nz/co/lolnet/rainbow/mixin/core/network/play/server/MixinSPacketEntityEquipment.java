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

package nz.co.lolnet.rainbow.mixin.core.network.play.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import nz.co.lolnet.rainbow.interfaces.network.play.server.IMixinSPacketEntityEquipment;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = SPacketEntityEquipment.class, priority = 1337)
@Implements(@Interface(iface = IMixinSPacketEntityEquipment.class, prefix = "rainbow$"))
public abstract class MixinSPacketEntityEquipment {
    
    @Shadow
    private int entityID;
    
    @Shadow
    private EntityEquipmentSlot equipmentSlot;
    
    @Shadow
    private ItemStack itemStack;
    
    public boolean rainbow$populate(EntityPlayer entityPlayer, ItemStack itemStack) {
        if (itemStack.isEmpty() || !(itemStack.getItem() instanceof ItemArmor)) {
            return false;
        }
        
        this.entityID = entityPlayer.getEntityId();
        this.equipmentSlot = ((ItemArmor) itemStack.getItem()).armorType;
        this.itemStack = itemStack;
        return true;
    }
}