package cn.jxufe.model.enums;

public enum FarmAction {
    PLANT("播种"),
    HARVEST("收获"),
    CLEAN("清理"),
    KILL_WORM("清理虫害"),
    CROP_STATUS_UPDATE("作物状态更新"),
    PEST_INFESTATION("虫害侵扰");


    private final String actionName;


    FarmAction(String actionName) {
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }
}
