package com.minshigee.playerchanger.logic.change;

import com.minshigee.playerchanger.domain.GameState;
import com.minshigee.playerchanger.domain.annotation.IsController;
import com.minshigee.playerchanger.domain.annotation.MappingCommand;
import com.minshigee.playerchanger.domain.annotation.MappingEvent;
import com.minshigee.playerchanger.domain.module.Controller;
import com.minshigee.playerchanger.util.MessageUtil;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.lang.reflect.Method;

@IsController
public class ChangeController extends Controller<ChangeRepository> {
    public ChangeController(ChangeRepository repository) {
        super(repository);
    }

    @MappingCommand(arg = "change", needOp = true, states = {GameState.Disable,GameState.Waitting, GameState.Freezing})
    public void changePlayers(Player player, String[] args){
        if(args != null && args.length >= 3){
            Player p1 = Bukkit.getPlayer(args[1]);
            Player p2 = Bukkit.getPlayer(args[2]);
            if(p1 == null || p2 == null) {
                MessageUtil.printMsgToPlayer(ChatMessageType.CHAT,player,"존재하지 않는 유저 이름입니다.");
                return;
            }
            repository.changePlayer(p1,p2);
            return;
        }
        repository.changePlayers(null,null);
    }

    @MappingEvent(states = {GameState.Disable, GameState.Enable, GameState.Waitting, GameState.Freezing})
    public void clickPlayerInventoryEvent_Change(InventoryClickEvent event){
        if(event.getClickedInventory() == null) {return;}
        if(!(event.getClickedInventory().getHolder() instanceof ChangeData)){
            return;
        }
        event.setCancelled(true);
        repository.inventoryClickShopInventory(event);
    }

    @MappingEvent(states = {GameState.Enable, GameState.Waitting, GameState.Freezing})
    public void interactPlayerSpecificBlockEvent(PlayerInteractEvent event){
        if(!(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
            return;
        if(!(event.getClickedBlock().getType().equals(Material.LECTERN)))
            return;
        repository.showShopMain(event.getPlayer());
    }

    public void registerChangeMethod(Integer code, Method method)
    {
        repository.registerChangeMethod(code, method);
    }
}
