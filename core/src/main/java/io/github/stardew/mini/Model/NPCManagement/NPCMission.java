package io.github.stardew.mini.Model.NPCManagement;

import io.github.stardew.mini.MainApp;
import io.github.stardew.mini.Model.Result;
import io.github.stardew.mini.Model.Things.Item;
import io.github.stardew.mini.Model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NPCMission {
    private Map<String, Integer> requiredItems;
    private Map<String, Integer> prizeItems;
    Boolean isAlreadyDone;

    public NPCMission(Map<String, Integer> requiredItems, Map<String, Integer> prizeItems) {
        this.requiredItems = new HashMap<>(requiredItems);
        this.prizeItems = new HashMap<>(prizeItems);
        this.isAlreadyDone = false;
    }

    public Map<String, Integer> getRequiredItems() {
        return requiredItems;
    }

    public Map<String, Integer> getPrizeItems() {
        return prizeItems;
    }

    public Boolean getAlreadyDone() {
        return isAlreadyDone;
    }

    public void setAlreadyDone(Boolean alreadyDone) {
        isAlreadyDone = alreadyDone;
    }

    public static Result doMission(NPCMission selectedMission, User currentUser) {
        for (NPC npc : MainApp.getInstance().getCurrentGame().getNpcs()) {
            int missionIndex = 1;
            for (NPCMission mission : npc.getMissions()){
                if (mission.equals(selectedMission)) {
                    return npc.doMission(missionIndex, currentUser);
                }
                missionIndex++;
            }
        }
        return new Result(false,"No mission found");
    }
}
