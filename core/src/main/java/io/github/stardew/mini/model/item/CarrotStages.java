package io.github.stardew.mini.model.item;

public enum CarrotStages {
    CARROT_STAGE_1("game/crops/carrot_stage_1.png"),
    CARROT_STAGE_2("game/crops/carrot_stage_2.png"),
    CARROT_STAGE_3("game/crops/carrot_stage_3.png"),
    CARROT_STAGE_4("game/crops/carrot_stage_4.png"),
    ;
    private final String iconPath;
    private CarrotStages(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getIconPath() {
        return iconPath;
    }
}
