package com.minshigee.playerchanger.util;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.logic.game.GameData;
import com.mojang.datafixers.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockVector;

import java.io.Console;
import java.util.*;
import java.util.stream.Collectors;

public class ItemGenerate {
    private static int resetTime = 180;
    private static int playerPerShop = 4; //플레이어 value명 당 상점 1개씩 증가
    private static HashMap<Integer,ArrayList<Pair<Material,Integer>>> changedItems = new HashMap<>();
    private static HashMap<Integer,ArrayList<Material>> changedBlocks = new HashMap<>();
    private static World world = null;

    public static void addChangedItem(Integer code, Pair<Material,Integer> data){if(changedItems.get(code) == null) changedItems.put(code, new ArrayList<>());changedItems.get(code).add(data);}
    public static void addChangedItems(Integer code, Collection<Pair<Material,Integer>> collection){if(changedItems.get(code) == null) changedItems.put(code, new ArrayList<>());changedItems.get(code).addAll(collection);}
    public static void clearChangedCodeItems(Integer code){if(changedItems.get(code) == null) return;changedItems.get(code).clear();}
    public static void clearChangedItems(){changedItems.clear();}

    public static void addChangedBlock(Integer code, Material material){if(changedBlocks.get(code) == null) changedBlocks.put(code, new ArrayList<>());changedBlocks.get(code).add(material);}
    public static void addChangedBlocks(Integer code, Collection<Material> collection) {if (changedBlocks.get(code) == null) changedBlocks.put(code, new ArrayList<>());changedBlocks.get(code).addAll(collection);}
    public static void clearChangedCodeBlocks(Integer code){if(changedBlocks.get(code) == null) return;changedBlocks.get(code).clear();}
    public static void clearChangedBlocks(){changedBlocks.clear();}

    public static void resetWorldBlock(){
        world = PlayerChanger.config.getLocation("GameWorld.Size_Pos1").getWorld();
        initSpecBlocks(GameData.spawnBlockVectors, Material.BEDROCK);
        initSpecBlocks(GameData.chestBlockVectors, Material.CHEST);
        initSpecBlocks(GameData.craftingBlockVectors, Material.CRAFTING_TABLE);
        initSpecBlocks(GameData.furnaceBlockVectors, Material.FURNACE);
        MessageUtil.printConsoleLog(ChatColor.GREEN + "월드 특정 블럭을 리셋했습니다.");
    }

    public static void resetChestBlocks(){
        world = PlayerChanger.config.getLocation("GameWorld.Size_Pos1").getWorld();
        GameData.chestBlockVectors.stream()
                .map(blockVector ->(Chest) world.getBlockAt(blockVector.getBlockX(),blockVector.getBlockY(), blockVector.getBlockZ()).getState())
                .forEach(chest -> {
                    chest.getBlockInventory().setContents(Bukkit.createInventory(null, InventoryType.CHEST,"").getContents());
                });
        MessageUtil.printConsoleLog(ChatColor.GREEN + "월드 상자 내부를 초기화했습니다.");
    }

    public static void insertItemToChest(Integer code){
        if(changedItems.get(code) == null)
            return;
        Random random = new Random();
        int size = GameData.chestBlockVectors.size();
        ArrayList<Chest> chests = new ArrayList<>(GameData.chestBlockVectors.stream()
                .map(blockVector ->(Chest) world.getBlockAt(
                        blockVector.getBlockX(),blockVector.getBlockY(), blockVector.getBlockZ()).getState())
                .collect(Collectors.toList()));
        Collections.shuffle(chests);
        changedItems.get(code).forEach(pair ->{
            Material mat = pair.getFirst();
            int cnt = pair.getSecond();
            for(int i = cnt; i > 0; i--){
                chests.get(random.nextInt(size)).getInventory().addItem(new ItemStack(mat, 1));
            }
        });
        MessageUtil.printConsoleLog(ChatColor.GREEN + "%d 코드의 아이템 리스트를 창고에 배정했습니다.");
    }

    private static void makeNecessaryBlock(){
        ArrayList<BlockVector> tmpVector = new ArrayList<>(){{
            addAll(GameData.craftingBlockVectors);
            addAll(GameData.furnaceBlockVectors);
        }};
        Collections.shuffle(tmpVector);
        int cnt = 1 + GameData.getParticipantsAlive().size()/playerPerShop;
        for(int i = 0; i < cnt; i++)
            setVectorBlock(tmpVector.get(i),Material.LECTERN);
    }

    private static void initSpecBlocks(Set<BlockVector> set, Material mat){
        set.forEach(blockVector -> {
            setVectorBlock(blockVector, mat);
        });
    }

    private static void setVectorBlock(BlockVector vector, Material mat){
        world.getBlockAt(vector.getBlockX(),vector.getBlockY(),vector.getBlockZ()).setType(mat);
    }

}
