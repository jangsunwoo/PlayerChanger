package com.minshigee.playerchanger.logic.mission;

import com.minshigee.playerchanger.domain.module.Repository;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class MissionRepository extends Repository<MissionData> {
    public MissionRepository(MissionData localDB) {
        super(localDB);
    }

    public void resetMissions(){
        localDB.resetMissions();
        updateScoreBoard();
    }

    public<T> void updateMissions(T event){
        if(!(event instanceof Event))
            return;
        localDB.getMissions().forEach(mission -> {
            if(mission.getClearPlayer() == null)
                return;
            Player player = mission.updateMission(event);
            if(player == null)
                return;
            //TODO View에 내용 업데이트하기.
            validateMission();
        });
    }

    private void validateMission(){
        long mCnt = localDB.getMissions().stream().filter(mission -> {
            return mission.getClearPlayer() == null;
        }).count();
        if(mCnt > 0)
            return;

        //TODO Event발생
        resetMissions();
    }

    private void updateScoreBoard(){
        //TODO ViewController에 미션 상태 업데이트 코드 작성.
    }
}
