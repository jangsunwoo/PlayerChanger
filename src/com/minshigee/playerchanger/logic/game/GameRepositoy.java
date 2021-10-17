package com.minshigee.playerchanger.logic.game;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.GameState;
import com.minshigee.playerchanger.domain.module.Repository;
import com.minshigee.playerchanger.util.MessageUtil;
import com.mojang.datafixers.util.Pair;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GameRepositoy extends Repository<GameData> {
    public GameRepositoy(GameData localDB) {
        super(localDB);
    }

    public void excuteGameStart(Player player){
        if(GameData.getGameState().equals(GameState.Disable)){
            validateBeforeGameStart(player);
            scanGameWorldPos();
            GameData.makeNextGameStatus();
        }
    }
    private void validateBeforeGameStart(Player player){
        MessageUtil.printMsgToAll(ChatMessageType.CHAT, ChatColor.AQUA + "게임이 곧 시작됩니다. 관련 세팅을 시작합니다.");
        resetGame();
    }

    private void scanGameWorldPos(){
        MessageUtil.printMsgToPlayers(ChatMessageType.CHAT, ChatColor.AQUA + "월드 스캐닝을 시작합니다. 시간이 조금 걸립니다.");
        final Location pos1 = PlayerChanger.config.getLocation("GameWorld.Size_Pos1");
        final Location pos2 = PlayerChanger.config.getLocation("GameWorld.Size_Pos2");
        final Material spawnBlock = Material.valueOf(PlayerChanger.config.getString("SpawnBlock"));
        final Material assignBlock = Material.valueOf(PlayerChanger.config.getString("MissionAssignBlock"));
        BlockVector stV =  new BlockVector(Math.min(pos1.getBlockX(), pos2.getBlockX()), Math.min(pos1.getBlockY(),pos2.getBlockY()), Math.min(pos1.getBlockZ(),pos2.getBlockZ()));
        BlockVector edV = new BlockVector(Math.max(pos1.getBlockX(), pos2.getBlockX()), Math.max(pos1.getBlockY(),pos2.getBlockY()), Math.max(pos1.getBlockZ(),pos2.getBlockZ()));
        new BukkitRunnable(){
            @Override
            public void run() {
                AtomicInteger processCount = new AtomicInteger(0);
                int searchProcess =((int)(edV.getBlockX() - stV.getBlockX()) / 16) * ((int)(edV.getBlockZ() - stV.getBlockZ()) / 16);
                AtomicInteger searchCount = new AtomicInteger(0);
                ArrayList<Pair<Integer,Integer>> chunkPoses = new ArrayList<>();
                MessageUtil.printMsgToAll(ChatMessageType.ACTION_BAR, "유효한 청크를 조사 중입니다.".formatted((int)(((double)searchCount.get() / searchProcess) * 100)));
                for(int x = stV.getBlockX(); x <= edV.getBlockX(); x+= 16) {
                    for (int z = stV.getBlockZ(); z <= edV.getBlockZ(); z += 16) {
                        chunkPoses.add(new Pair(x, z));
                        searchCount.getAndIncrement();
                    }
                }
                int chunksSize = chunkPoses.size();
                chunkPoses.stream().parallel().forEach(pair -> {
                    getBlocksInChunk(pos1.getWorld().getChunkAt(pair.getFirst(),pair.getSecond()), spawnBlock, GameData.spawnBlockVectors, stV, edV);
                    getBlocksInChunk(pos1.getWorld().getChunkAt(pair.getFirst(),pair.getSecond()), assignBlock, GameData.assignBlockVectors, stV, edV);
                    getBlocksInChunk(pos1.getWorld().getChunkAt(pair.getFirst(),pair.getSecond()), Material.CHEST, GameData.chestBlockVectors, stV, edV);
                    processCount.getAndIncrement();
                    MessageUtil.printMsgToAll(ChatMessageType.ACTION_BAR, ChatColor.DARK_AQUA +"월드 스캔 %d%% 진행 중".formatted((int)(((double)processCount.get() / chunksSize) * 100)));
                });
                MessageUtil.printMsgToAll(ChatMessageType.ACTION_BAR, ChatColor.GREEN +"월드 스캔이 끝났습니다.");
            }
        }.runTask(PlayerChanger.singleton);
    }

    private void getBlocksInChunk(final Chunk chunk, Material material, List<BlockVector> toList, BlockVector minV, BlockVector maxV){
        ArrayList<BlockVector> blocks = new ArrayList<>();
        final int minX = chunk.getX();
        final int maxX = chunk.getX() | 16;
        final int minY = minV.getBlockY();
        final int maxY = maxV.getBlockY();
        final int minZ = chunk.getZ() << 4;
        final int maxZ = chunk.getZ() | 16;
        for(int x = minX; x < maxX; x++)
            for(int y = minY; y < maxY; y++)
                for(int z = minZ; z < maxZ; z++) {
                    try {
                        if (chunk.getBlock(x, y, z).getType().equals(material))
                            blocks.add(new BlockVector(x, y, x));
                    }
                    catch (Exception e){
                        continue;
                    }
                }
        toList.addAll(blocks);
        return;
    }

    /*
    게임 Stop 관련 코드
     */
    public void executeGameStop(Player player){
        resetGame();
    }
    private void resetGame(){
        localDB.clearSettingData();
        GameData.clearParticipants();
        GameData.offGameState();
    }

    /*
    Setter 관련 Repo
     */

    public void makePlayerSetterOrNot(Player player){
        if(!localDB.checkPlayerIsSetter(player)){
            localDB.addSetter(player);
            MessageUtil.printLogToPlayer(player, ChatColor.AQUA + "세터모드가 되었습니다.");
        }
        else{
            localDB.removeSetter(player);
            MessageUtil.printLogToPlayer(player, ChatColor.AQUA + "세터모드가 해제되었습니다.");
        }
    }
    public void updateGameWorldPos(PlayerInteractEvent event){
        if(!localDB.checkPlayerIsSetter(event.getPlayer()))
            return;
        if(!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BLAZE_ROD))
            return;
        Location location = event.getPlayer().getLocation();
        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK){
            PlayerChanger.config.set("GameWorld.Size_Pos1", location);
            MessageUtil.printLogToPlayer(event.getPlayer(), ChatColor.AQUA + " 월드 Pos1이 정해졌습니다. {%d / %d / %d}".formatted(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
            return;
        }
        PlayerChanger.config.set("GameWorld.Size_Pos2", event.getPlayer().getLocation());
        MessageUtil.printLogToPlayer(event.getPlayer(), ChatColor.AQUA + " 월드 Pos2이 정해졌습니다. {%d / %d / %d}".formatted(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
    }
    public void updateGameCoreBolck(PlayerInteractEvent event){
        if(!localDB.checkPlayerIsSetter(event.getPlayer()))
            return;
        if(!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.ARROW))
            return;
        if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
            PlayerChanger.config.set("SpawnBlock", event.getClickedBlock().getBlockData().getMaterial().name());
            MessageUtil.printLogToPlayer(event.getPlayer(), ChatColor.AQUA + " 스폰 블럭이 %s로 지정되었습니다.".formatted(event.getClickedBlock().getType().name()));
        }
        else if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            PlayerChanger.config.set("MissionAssignBlock", event.getClickedBlock().getBlockData().getMaterial().name());
            MessageUtil.printLogToPlayer(event.getPlayer(), ChatColor.AQUA + " 미션 완료 블럭이 %s로 지정되었습니다.".formatted(event.getClickedBlock().getType().name()));
        }
    }

}
