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

package io.github.lxgaming.rainbow.listener;

import io.github.lxgaming.rainbow.manager.RainbowManager;
import io.github.lxgaming.rainbow.util.Toolbox;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.network.ClientConnectionEvent;

public class PlayerListener {
    
    @Listener
    public void onClientConnectionDisconnect(ClientConnectionEvent.Disconnect event, @Getter("getTargetEntity") Player player) {
        player.getHelmet().ifPresent(itemStack -> RainbowManager.saveHue(player, itemStack));
        player.getChestplate().ifPresent(itemStack -> RainbowManager.saveHue(player, itemStack));
        player.getLeggings().ifPresent(itemStack -> RainbowManager.saveHue(player, itemStack));
        player.getBoots().ifPresent(itemStack -> RainbowManager.saveHue(player, itemStack));
        
        RainbowManager.getTracking().remove(player.getUniqueId());
    }
    
    @Listener
    public void onClientConnectionJoin(ClientConnectionEvent.Join event, @Getter("getTargetEntity") Player player) {
        RainbowManager.getTracking().compute(player.getUniqueId(), (key, value) -> {
            Float[] array;
            if (value != null) {
                array = value;
            } else {
                array = new Float[4];
            }
            
            return Toolbox.fillArray(array, 0.0F);
        });
        
        player.getHelmet().ifPresent(itemStack -> RainbowManager.loadHue(player, itemStack));
        player.getChestplate().ifPresent(itemStack -> RainbowManager.loadHue(player, itemStack));
        player.getLeggings().ifPresent(itemStack -> RainbowManager.loadHue(player, itemStack));
        player.getBoots().ifPresent(itemStack -> RainbowManager.loadHue(player, itemStack));
    }
}