package com.minshigee.playerchanger.logic.game;

import com.minshigee.playerchanger.PlayerChanger;
import com.minshigee.playerchanger.domain.GameState;
import com.minshigee.playerchanger.domain.module.Repository;
import com.minshigee.playerchanger.logic.view.ViewController;
import com.minshigee.playerchanger.logic.view.domain.ShowType;
import com.minshigee.playerchanger.util.MessageUtil;
import com.mojang.datafixers.util.Pair;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockVector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class GameRepositoy extends Repository<GameData> {
    public GameRepositoy(GameData localDB) {
        super(localDB);
    }

    public void excuteGameStart(Player player){
        if(GameData.getGameState().equals(GameState.Disable)){
            validateBeforeGameStart(player);
            GameData.makeNextGameStatus();
            scanGameWorldPos();
        }
    }
    private void validateBeforeGameStart(Player player){
        MessageUtil.printMsgToAll(ChatMessageType.CHAT, ChatColor.AQUA + "게임이 곧 시작됩니다. 관련 세팅을 시작합니다.");
        resetGame();
    }

    /*
    월드 스캔과 관련된 코드
     */
    private void scanGameWorldPos() {MessageUtil.printMsgToPlayers(ChatMessageType.CHAT, ChatColor.AQUA + "월드 스캐닝을 시작합니다. 시간이 조금 걸립니다.");final Location pos1 = PlayerChanger.config.getLocation("GameWorld.Size_Pos1");final Location pos2 = PlayerChanger.config.getLocation("GameWorld.Size_Pos2");final String spawnBlock = PlayerChanger.config.getString("SpawnBlock");final String assignBlock = PlayerChanger.config.getString("MissionAssignBlock");final BlockVector stV = new BlockVector(Math.min(pos1.getBlockX(), pos2.getBlockX()), Math.min(pos1.getBlockY(), pos2.getBlockY()), Math.min(pos1.getBlockZ(), pos2.getBlockZ()));final BlockVector edV = new BlockVector(Math.max(pos1.getBlockX(), pos2.getBlockX()), Math.max(pos1.getBlockY(), pos2.getBlockY()), Math.max(pos1.getBlockZ(), pos2.getBlockZ()));new BukkitRunnable() {@Override public void run() {ArrayList<Pair<Integer, Integer>> chunkPoz = new ArrayList<>();MessageUtil.printMsgToAll(ChatMessageType.ACTION_BAR, "유효한 청크를 조사 중입니다.");for (int x = stV.getBlockX(); x <= edV.getBlockX(); x += 16) for (int z = stV.getBlockZ(); z <= edV.getBlockZ(); z += 16) chunkPoz.add(new Pair(x >> 4, z >> 4));MessageUtil.printMsgToAll(ChatMessageType.ACTION_BAR, "청크를 조사 중입니다.");final Pair<String,String> blocks = new Pair<String,String>(spawnBlock, assignBlock);final Pair<Integer,Integer> rRange = new Pair<Integer, Integer>(stV.getBlockY(), edV.getBlockY());Integer threadCount = Runtime.getRuntime().availableProcessors();Integer scanSize = chunkPoz.size();Integer scanCount = 0;Chunk chunk = null;ArrayList<ChunkSnapshot> tmpChunks = new ArrayList<>();for (Pair<Integer, Integer> chunkPos : chunkPoz) {if (!GameData.getGameState().equals(GameState.Waitting)) {MessageUtil.printConsoleLog(ChatColor.RED + "맵 스캔이 종료되었습니다.");break;}chunk = pos1.getWorld().getChunkAt(chunkPos.getFirst(), chunkPos.getSecond());tmpChunks.add(chunk.getChunkSnapshot());if (tmpChunks.size() < threadCount) continue;MessageUtil.printMsgToAll(ChatMessageType.ACTION_BAR, ChatColor.DARK_AQUA + "월드 스캔 %d/%d 진행 중".formatted(scanCount, scanSize));scanSomeChunks(tmpChunks, blocks, rRange);tmpChunks.clear();scanCount += threadCount;}scanSomeChunks(tmpChunks, blocks, rRange);MessageUtil.printMsgToAll(ChatMessageType.ACTION_BAR, ChatColor.GREEN + "월드 스캔이 끝났습니다.");MessageUtil.printConsoleLog(ChatColor.GREEN + "%s : [%d], %s : [%d], %s : [%d]".formatted("Chest", GameData.chestBlockVectors.stream().count(), spawnBlock, GameData.spawnBlockVectors.stream().count(), assignBlock, GameData.assignBlockVectors.stream().count()));this.cancel();}}.runTaskAsynchronously(PlayerChanger.singleton);}
    private void scanSomeChunks(final ArrayList<ChunkSnapshot> chunks, final Pair<String,String> blocks, final Pair<Integer, Integer> yRange) {chunks.parallelStream().forEach(chunk -> getBlocksInChunk(chunk, blocks, yRange));}
    private void getBlocksInChunk(final ChunkSnapshot chunk, final Pair<String,String> materialNames, final Pair<Integer, Integer> yRange) {Set<BlockVector> spawnblocks = new HashSet<>();Set<BlockVector> assignblocks = new HashSet<>();Set<BlockVector> chestblocks = new HashSet<>();final int minX = chunk.getX() << 4;final int maxX = minX | 15;final int minY = yRange.getFirst();final int maxY = yRange.getSecond();final int minZ = chunk.getZ() << 4;final int maxZ = minZ | 15;for (int x = minX; x <= maxX; ++x) for (int y = minY; y <= maxY; ++y) for (int z = minZ; z <= maxZ; ++z) {                    if (materialNames.getFirst().equals(chunk.getBlockType(x&0xF, y, z&0xF).name())) spawnblocks.add(new BlockVector(x,y,z)); if (materialNames.getSecond().equals(chunk.getBlockType(x&0xF, y, z&0xF).name())) assignblocks.add(new BlockVector(x,y,z)); if (Material.CHEST.name().equals(chunk.getBlockType(x&0xF, y, z&0xF).name())) chestblocks.add(new BlockVector(x,y,z));}GameData.spawnBlockVectors.addAll(spawnblocks);GameData.assignBlockVectors.addAll(assignblocks);GameData.chestBlockVectors.addAll(chestblocks);}

    /*
    게임 Stop 관련 코드
     */
    public void executeGameStop(Player player){
        resetGame();
        MessageUtil.printLogToPlayer(player, ChatColor.RED + " 게임이 종료되었습니다.");
    }
    private void resetGame(){
        localDB.clearSettingData();
        GameData.clearParticipants();
        GameData.clearParticipants();
        GameData.offGameState();
    }

    /*
    Setter 관련 Repo
     */
    public void makePlayerSetterOrNot(Player player){if(!localDB.checkPlayerIsSetter(player)){localDB.addSetter(player);MessageUtil.printLogToPlayer(player, ChatColor.AQUA + "세터모드가 되었습니다.");} else{localDB.removeSetter(player);MessageUtil.printLogToPlayer(player, ChatColor.AQUA + "세터모드가 해제되었습니다.");}}
    public void updateGameWorldPos(PlayerInteractEvent event){if(!localDB.checkPlayerIsSetter(event.getPlayer())) return;if(!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.BLAZE_ROD)) return;Location location = event.getPlayer().getLocation();if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK){PlayerChanger.config.set("GameWorld.Size_Pos1", location);MessageUtil.printLogToPlayer(event.getPlayer(), ChatColor.AQUA + " 월드 Pos1이 정해졌습니다. {%d / %d / %d}".formatted(location.getBlockX(), location.getBlockY(), location.getBlockZ()));return;}PlayerChanger.config.set("GameWorld.Size_Pos2", event.getPlayer().getLocation());MessageUtil.printLogToPlayer(event.getPlayer(), ChatColor.AQUA + " 월드 Pos2이 정해졌습니다. {%d / %d / %d}".formatted(location.getBlockX(), location.getBlockY(), location.getBlockZ()));}
    public void updateGameCoreBolck(PlayerInteractEvent event){if(!localDB.checkPlayerIsSetter(event.getPlayer())) return;if(!event.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.ARROW)) return;if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)){PlayerChanger.config.set("SpawnBlock", event.getClickedBlock().getBlockData().getMaterial().name());MessageUtil.printLogToPlayer(event.getPlayer(), ChatColor.AQUA + " 스폰 블럭이 %s로 지정되었습니다.".formatted(event.getClickedBlock().getType().name()));} else if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){PlayerChanger.config.set("MissionAssignBlock", event.getClickedBlock().getBlockData().getMaterial().name());MessageUtil.printLogToPlayer(event.getPlayer(), ChatColor.AQUA + " 미션 완료 블럭이 %s로 지정되었습니다.".formatted(event.getClickedBlock().getType().name()));}}
    public void breakBlockSetter(BlockBreakEvent event){if(!localDB.checkPlayerIsSetter(event.getPlayer())) return;event.setCancelled(true);}

}
