/*
 * Copyright 2018 Alex Thomson
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

package io.github.lxgaming.rainbow.mixin.core.entity.player;

import net.minecraft.entity.player.EntityPlayerMP;
import io.github.lxgaming.rainbow.manager.RainbowManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.common.interfaces.IMixinCommandSender;
import org.spongepowered.common.interfaces.IMixinCommandSource;
import org.spongepowered.common.interfaces.IMixinSubject;
import org.spongepowered.common.interfaces.entity.player.IMixinEntityPlayerMP;

@Mixin(value = EntityPlayerMP.class, priority = 1337)
public abstract class MixinEntityPlayerMP implements Player, IMixinSubject, IMixinEntityPlayerMP, IMixinCommandSender, IMixinCommandSource {
    
    @Inject(method = "onUpdate", at = @At(value = "HEAD"))
    public void rainbowOnTickEntityPre(CallbackInfo callbackInfo) {
        if (!isLoaded()) {
            return;
        }
        
        getHelmet().ifPresent(itemStack -> RainbowManager.updateHue(this, itemStack));
        getChestplate().ifPresent(itemStack -> RainbowManager.updateHue(this, itemStack));
        getLeggings().ifPresent(itemStack -> RainbowManager.updateHue(this, itemStack));
        getBoots().ifPresent(itemStack -> RainbowManager.updateHue(this, itemStack));
    }
}