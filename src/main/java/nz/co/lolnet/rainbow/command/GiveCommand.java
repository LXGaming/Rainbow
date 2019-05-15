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
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.entity.Hotbar;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;
import java.util.Optional;

public class GiveCommand extends AbstractCommand {
    
    public GiveCommand() {
        addAlias("give");
        setPermission("rainbow.command.give");
    }
    
    @Override
    public CommandResult execute(CommandSource commandSource, List<String> arguments) {
        if (arguments.isEmpty()) {
            if (!(commandSource instanceof Player)) {
                commandSource.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "This command cannot be run by Console"));
                return CommandResult.empty();
            }
            
            Player player = (Player) commandSource;
            getInventory(player).ifPresent(inventory -> inventory.offer(RainbowManager.createItem(player, ItemTypes.LEATHER_HELMET)));
            getInventory(player).ifPresent(inventory -> inventory.offer(RainbowManager.createItem(player, ItemTypes.LEATHER_CHESTPLATE)));
            getInventory(player).ifPresent(inventory -> inventory.offer(RainbowManager.createItem(player, ItemTypes.LEATHER_LEGGINGS)));
            getInventory(player).ifPresent(inventory -> inventory.offer(RainbowManager.createItem(player, ItemTypes.LEATHER_BOOTS)));
            player.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.GREEN, "You have received Rainbow Armor"));
            return CommandResult.success();
        }
        
        if (arguments.size() == 1) {
            Optional<Player> player = Sponge.getServer().getPlayer(arguments.get(0));
            if (!player.isPresent()) {
                commandSource.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "Failed to find Player"));
                return CommandResult.empty();
            }
            
            if (!player.get().isOnline()) {
                commandSource.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.WHITE, player.get().getName(), TextColors.RED, " is not online"));
                return CommandResult.empty();
            }
            
            getInventory(player.get()).ifPresent(inventory -> inventory.offer(RainbowManager.createItem(player.get(), ItemTypes.LEATHER_HELMET)));
            getInventory(player.get()).ifPresent(inventory -> inventory.offer(RainbowManager.createItem(player.get(), ItemTypes.LEATHER_CHESTPLATE)));
            getInventory(player.get()).ifPresent(inventory -> inventory.offer(RainbowManager.createItem(player.get(), ItemTypes.LEATHER_LEGGINGS)));
            getInventory(player.get()).ifPresent(inventory -> inventory.offer(RainbowManager.createItem(player.get(), ItemTypes.LEATHER_BOOTS)));
            player.get().sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.GREEN, "You have received Rainbow Armor"));
            return CommandResult.success();
        }
        
        commandSource.sendMessage(Text.of(Toolbox.getTextPrefix(), TextColors.RED, "Invalid arguments"));
        return CommandResult.empty();
    }
    
    private Optional<Inventory> getInventory(Player player) {
        Inventory hotbarInventory = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(Hotbar.class));
        if (hotbarInventory.size() < hotbarInventory.capacity()) {
            return Optional.of(hotbarInventory);
        }
        
        Inventory playerInventory = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(GridInventory.class));
        if (playerInventory.size() < playerInventory.capacity()) {
            return Optional.of(playerInventory);
        }
        
        return Optional.empty();
    }
}