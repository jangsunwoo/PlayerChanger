package com.minshigee.playerchanger.logic.change;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.Participant;
import com.minshigee.playerchanger.domain.module.Data;
import com.minshigee.playerchanger.logic.game.GameData;
import com.minshigee.playerchanger.util.MessageUtil;
import com.minshigee.playerchanger.util.Util;
import com.mojang.datafixers.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.lang.reflect.Method;
import java.util.*;

public class ChangeData extends Data implements InventoryHolder {
    private final ArrayList<Pair<Integer, Method>> changeMethods = new ArrayList<>();
    private HashMap<Participant, Integer> scoreData = new HashMap<>();
    private Inventory shopMain = Bukkit.createInventory(this,8, "Shop Menu");
    private Inventory shopEffect = Bukkit.createInventory(this,9, "Effect Shop");

    public ChangeData() {
        init();
    }

    private void init(){
        initShopMain();
        initShopEffect();
    }

    private void initShopMain(){
        shopMain.setItem(2, Util.createItem("#", Material.GREEN_STAINED_GLASS_PANE, Collections.singletonList("")));
        shopMain.setItem(4,Util.createItem("Change", Material.CLOCK, Collections.singletonList(ChatColor.GREEN + "유저들을 Change합니다.")));
        shopMain.setItem(6, Util.createItem("Effect Shop", Material.GOLDEN_APPLE, Collections.singletonList(ChatColor.GREEN + "효과를 구입합니다.")));
    }

    private void initShopEffect(){
        //TODO Const Material 추가하기.
    }

    public Inventory makeShopMainForPlayer(Player player){
        GameData.getParticipantAlive(player).ifPresentOrElse(
                participant -> {
                    shopMain.setItem(0, Util.createPlayerHead(
                            player,
                            ChatColor.GOLD + player.getName(),
                            Collections.singletonList("체력 : %d\n점수 : %d".formatted((int) player.getHealth(), scoreData.get(participant)))
                    ));

                    shopMain.setItem(1, Util.createItem(
                                    ChatColor.GREEN + "점수 : %d".formatted(scoreData.get(participant)),
                                    Material.EMERALD,
                                    Collections.singletonList("")
                            )
                    );
                },
                () -> {
                    shopMain.clear(0);
                    shopMain.clear(1);
                }
        );
        return shopMain;
    }

    public Inventory makeEffectShopForPlayer(Player player){
        GameData.getParticipantAlive(player).ifPresentOrElse(
                participant -> {
                    shopEffect.setItem(31, Util.createItem(
                                    ChatColor.GREEN + "점수 : %d".formatted(scoreData.get(participant)),
                                    Material.EMERALD,
                                    Collections.singletonList("")
                            )
                    );
                },
                () -> {
                    shopMain.clear(31);
                }
        );
        return shopMain;
    }

    public Integer getScore(Participant participant){
        return scoreData.get(participant);
    }

    public void updateScore(Participant participant, Integer score){
        scoreData.putIfAbsent(participant, 0);
        scoreData.replace(participant,getScore(participant) + score);
    }

    public boolean useScore(Participant participant, Integer value){
        Integer score = getScore(participant);
        if(score < value) {
            return false;
        }
        updateScore(participant,-value);
        return true;
    }

    public void clearScores(){
        scoreData.clear();
    }

    public void addMethod(Integer code, Method method)
    {
        MessageUtil.printConsoleLog("%s를 Change 목록에 추가합니다.".formatted(method.getName()));
        changeMethods.add(new Pair<>(code, method));
    }

    public void executeMethod(Integer code, Player p1, Player p2){
        changeMethods.stream()
                .filter(pair -> pair.getFirst().equals(code))
                .map(Pair::getSecond)
                .forEach(method -> {
                            try {
                                method.invoke(
                                        PlayerChanger.getInstanceOfClass(PlayerChanger.getContainerKeys().get(code)));
                            } catch (Exception e) {
                                MessageUtil.printConsoleLog("change에 실패했습니다.");
                            }
                        }
                );
    }

    public void executeMethods(Player p1, Player p2){
        changeMethods.forEach(pair ->{
            int code = pair.getFirst();
            Method method = pair.getSecond();
            try {
                method.invoke(PlayerChanger.getInstanceOfClass(method.getDeclaringClass()), p1, p2);
            } catch (Exception e) {
                MessageUtil.printConsoleLog(e.getMessage());
                MessageUtil.printConsoleLog("change에 실패했습니다.");
            }
        });
    }

    @Override
    public Inventory getInventory() {
        return shopMain;
    }
}
