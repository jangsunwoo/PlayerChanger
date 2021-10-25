package com.minshigee.playerchanger.logic.change;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.module.Data;
import com.minshigee.playerchanger.util.MessageUtil;
import com.minshigee.playerchanger.util.Util;
import com.mojang.datafixers.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChangeData extends Data implements InventoryHolder {
    private final ArrayList<Pair<Integer, Method>> changeMethods = new ArrayList<>();
    private Inventory shopMain = Bukkit.createInventory(this,9, "Shop Menu");
    private Inventory shopEffect = Bukkit.createInventory(this,9, "Effects Shop");

    public ChangeData() {
        init();
    }

    private void init(){
        shopMain.setItem(2,Util.createItem("Change", Material.CLOCK, Collections.singletonList(ChatColor.GREEN + "유저들을 Change합니다.")));
        shopMain.setItem(6, Util.createItem("Effect Shop", Material.GOLDEN_APPLE, Collections.singletonList(ChatColor.GREEN + "효과를 구입합니다.")));
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
