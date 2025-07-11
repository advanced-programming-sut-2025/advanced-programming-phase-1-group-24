package io.github.stardew.mini.Model.NPCManagement;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.github.stardew.mini.Model.Things.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
@JsonIdentityInfo(
    generator = ObjectIdGenerators.IntSequenceGenerator.class,
    property = "@id"
)
public class NPCMission {
    private Map<String, Integer> requiredItems;
    private Map<String, Integer> prizeItems;
    Boolean isAlreadyDone;

    public NPCMission(Map<String, Integer> requiredItems, Map<String, Integer> prizeItems) {
        this.requiredItems = new HashMap<>(requiredItems);
        this.prizeItems = new HashMap<>(prizeItems);
        this.isAlreadyDone = false;
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
}
