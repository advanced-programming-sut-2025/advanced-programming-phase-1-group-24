package io.github.stardew.mini.common.Model.NPCManagement;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.github.stardew.mini.client.MainApp;
import io.github.stardew.mini.common.Model.Result;
import io.github.stardew.mini.common.Model.User;

import java.util.HashMap;
import java.util.Map;
@JsonIdentityInfo(
    generator = ObjectIdGenerators.IntSequenceGenerator.class,
    property = "@id"
)
public class NPCMission {
    private String initials;
    private Map<String, Integer> requiredItems;
    private Map<String, Integer> prizeItems;
    Boolean isAlreadyDone;

    public NPCMission(String initials, Map<String, Integer> requiredItems, Map<String, Integer> prizeItems) {
        this.requiredItems = new HashMap<>(requiredItems);
        this.prizeItems = new HashMap<>(prizeItems);
        this.isAlreadyDone = false;
        this.initials = initials;
    }

    public NPCMission() {
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

    public String getInitials() {
        return initials;
    }

    public static Result doMission(String initials, User currentUser) {
        for (NPC npc : MainApp.getInstance().getCurrentGame().getNpcs()) {
            int missionIndex = 1;
            for (NPCMission mission : npc.getMissions()) {
                if (mission.getInitials().equals(initials)) {
                    return npc.doMission(missionIndex, currentUser);
                }
                missionIndex++;
            }
        }
        return new Result(false, "No mission found");
    }
}
