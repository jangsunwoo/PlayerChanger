package com.minshigee.playerchanger;

import com.minshigee.playerchanger.util.ConsoleLogs;
import com.minshigee.playerchanger.logic.CommandsExecutor;
import com.minshigee.playerchanger.logic.EventsListener;
import com.minshigee.playerchanger.logic.ability.AbilitiesController;
import com.minshigee.playerchanger.logic.ability.AbilitiesData;
import com.minshigee.playerchanger.logic.ability.AbilitiesRepository;
import com.minshigee.playerchanger.logic.change.ChangeController;
import com.minshigee.playerchanger.logic.change.ChangeData;
import com.minshigee.playerchanger.logic.change.ChangeRepository;
import com.minshigee.playerchanger.logic.game.GameController;
import com.minshigee.playerchanger.logic.game.GameData;
import com.minshigee.playerchanger.logic.game.GameRepositoy;
import com.minshigee.playerchanger.logic.mission.MissionController;
import com.minshigee.playerchanger.logic.mission.MissionData;
import com.minshigee.playerchanger.logic.mission.MissionRepository;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Set;

public class PlayerChanger extends JavaPlugin {

    public static FileConfiguration config;
    private static HashMap<Class, Object> Container = new HashMap<>();

    public static Object getInstanceOfClass(Class cls){
        return Container.get(cls);
    }
    public static Set<Class> getContainerKeys(){
        return Container.keySet();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        initPlugin();
        ConsoleLogs.printConsoleLog(ChatColor.GREEN + "작동되었습니다.");
    }

    @Override
    public void onDisable() {
        super.onDisable();
        ConsoleLogs.printConsoleLog(ChatColor.GREEN + "종료되었습니다.");
    }

    private void initPlugin() {
        this.saveDefaultConfig();
        config = this.getConfig();

        registerModules();
        registerCommandExecutor();
        registerEventListener();
        //logDIResult();
    }

    private void registerModules(){
        injectDependency(GameController.class, GameRepositoy.class, GameData.class);
        injectDependency(AbilitiesController.class, AbilitiesRepository.class, AbilitiesData.class);
        injectDependency(ChangeController.class, ChangeRepository.class, ChangeData.class);
        injectDependency(MissionController.class, MissionRepository.class, MissionData.class);
    }

    private <T,K,S> void injectDependency(T conClass, K repoClass, S dbClass){
        try {
            S tmpDB = (S) ((Class)dbClass).getConstructor().newInstance();
            K tmpRepo = (K) ((Class)repoClass).getConstructor(tmpDB.getClass()).newInstance(tmpDB);
            T tmpCont = (T) ((Class)conClass).getConstructor(tmpRepo.getClass()).newInstance(tmpRepo);
            registerInstanceToContainer(tmpDB);
            registerInstanceToContainer(tmpRepo);
            registerInstanceToContainer(tmpCont);
        }
        catch (Exception e){
            ConsoleLogs.printConsoleLog(ChatColor.RED + "DI에 실패했습니다. { " + ((Class)conClass).getName() + " } 관련 모듈.");
        }
    }

    private void registerEventListener(){
        this.getPluginLoader().createRegisteredListeners(
                registerInstanceToContainer(new EventsListener()),
                this
        );
    }

    private void registerCommandExecutor(){
        try {
            this.getCommand("ph").setExecutor(registerInstanceToContainer(new CommandsExecutor()));
        }
        catch (Exception e){
            ConsoleLogs.printConsoleLog(ChatColor.RED + " 명령어 등록에 실패했습니다.");
        }
    }

    private void logDIResult(){
        Container.keySet().forEach(o -> ConsoleLogs.printConsoleLog(ChatColor.AQUA + "DI Cheker: " + o.getName()));
        Container.values().forEach(o -> ConsoleLogs.printConsoleLog(ChatColor.AQUA + "DI Cheker: " + o.getClass().getName()));
    }

    private <T> T registerInstanceToContainer(T instance){
        Container.put(instance.getClass(), instance);
        return instance;
    }
}
