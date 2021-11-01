package com.minshigee.playerchanger.logic.change;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.Participant;
import com.minshigee.playerchanger.domain.annotation.MappingChange;
import com.minshigee.playerchanger.domain.module.Repository;
import com.minshigee.playerchanger.logic.game.GameData;
import com.minshigee.playerchanger.util.MessageUtil;
import com.minshigee.playerchanger.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class ChangeRepository extends Repository<ChangeData> {
    public ChangeRepository(ChangeData localDB, Integer viewCode) {
        super(localDB,viewCode);
        //registerChangeMethod(this.getClass()); //register이 controller보다 먼저 선언되기 때문에 여기선 직접 넣어줘야함.
        Arrays.stream(this.getClass().getDeclaredMethods()).filter(method -> method.getDeclaredAnnotation(MappingChange.class) != null).forEach(method -> registerChangeMethod(viewCode,method));
    }

    public void clearChangeData(){
        localDB.clearScores();
    }

    /*
    Point 관련 모듈
     */
    public void addParticipantPoint(Participant participant, Integer value){
        localDB.updateScore(participant,value);
    }
    public boolean useParticipantPoint(Participant participant, Integer value){
        return localDB.useScore(participant, value);
    }
    public Integer readPointOfParticipant(Player player){
        AtomicInteger res = new AtomicInteger(0);
        GameData.getParticipantByPlayer(player).ifPresent(participant -> {
            res.set(localDB.getScore(participant));
        });
        return res.get();
    }

    /*
    Shop 관련 모듈
     */
    public void showShopMain(Player player){
        //player.openInventory(localDB.makeShopMainForPlayer(player));
    }

    public void showShopEffect(Player player){

    }

    public void inventoryClickShopInventory(InventoryClickEvent event){
        Player player = (Player)event.getWhoClicked();
        GameData.getParticipantAlive(player).ifPresent(participant -> {
            Material clickedMat = Objects.requireNonNull(event.getCurrentItem()).getType();
            switch (clickedMat){
                case CLOCK:
                    if(localDB.useScore(participant, 100))
                        ((ChangeController)PlayerChanger.getInstanceOfClass(ChangeController.class))
                                .changePlayers((Player)event.getWhoClicked(), null);
                    break;
                case GOLDEN_APPLE:
                    //player.openInventory(localDB.makeEffectShopForPlayer(player));
                    break;
            }
        });
    }

    /*
    Change 관련 모듈
     */

    public void changePlayers(Player player, String[] args){
        new BukkitRunnable(){
            @Override
            public void run() {
                changePlayerInfo();
            }
        }.runTaskLater(PlayerChanger.singleton, 60);
    }

    public void changePlayer(Player p1, Player p2){
        localDB.executeMethods(p1,p2);
    }

    public void registerChangeMethod(Integer code, Method method){
        localDB.addMethod(code, method);
    }

    public void changePlayerInfo(){
        MessageUtil.printConsoleLog("아이템을 교체합니다.");
        ArrayList<Participant> pars = Util.getShuffledAliveParticipants();
        for(int i = 0; i < pars.size() / 2; i++){
            Player from = pars.get(i).getPlayer();
            Player to = pars.get(pars.size() - i - 1).getPlayer();
            localDB.executeMethods(from,to);
        }
        if(pars.size() % 2 == 1){
            Player from = pars.get(pars.size()/2).getPlayer();
            Player to = pars.get(0).getPlayer();
            localDB.executeMethods(from,to);
        }
    }

    @MappingChange
    public void changeLocation(Player from, Player to){
        Location tmpLoc = from.getLocation();
        from.teleport(to.getLocation());
        to.teleport(tmpLoc);
    }

    @MappingChange
    public void changeInventory(Player from, Player to){
        Inventory tmpInv = Bukkit.createInventory(null, InventoryType.PLAYER, "tmp Inventory");
        ItemStack[] tmpArmor = to.getInventory().getArmorContents();
        ItemStack[] tmpExtra = to.getInventory().getExtraContents();
        tmpInv.setContents(to.getInventory().getContents());

        to.getInventory().setContents(from.getInventory().getContents());
        to.getInventory().setArmorContents(from.getInventory().getArmorContents());
        to.getInventory().setExtraContents(from.getInventory().getExtraContents());

        from.getInventory().setContents(tmpInv.getContents());
        from.getInventory().setArmorContents(tmpArmor);
        from.getInventory().setExtraContents(tmpExtra);

        to.updateInventory();
        from.updateInventory();
    }


}
