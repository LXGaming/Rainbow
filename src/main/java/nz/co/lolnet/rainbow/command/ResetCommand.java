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

package nz.co.lolnet.rainbow.command;

import nz.co.lolnet.rainbow.manager.RainbowManager;
import nz.co.lolnet.rainbow.util.Toolbox;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;

public class ResetCommand extends AbstractCommand {
    
    public ResetCommand() {
        addAlias("reset");
        setPermission("rainbow.command.reset");
    }
    
    @Override
    public CommandResult execute(CommandSource commandSource, List<String> arguments) {
        if (!(commandSource instanceof Player)) {
            commandSource.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "This command cannot be run by Console"));
            return CommandResult.empty();
        }
        
        Player player = (Player) commandSource;
        player.getHelmet().ifPresent(RainbowManager::resetItem);
        player.getChestplate().ifPresent(RainbowManager::resetItem);
        player.getLeggings().ifPresent(RainbowManager::resetItem);
        player.getBoots().ifPresent(RainbowManager::resetItem);
        player.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.GREEN, "Rainbow Armor reset"));
        return CommandResult.success();
    }
}