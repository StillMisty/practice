package cn.jxufe.model.enums;

public enum SoundType {
    SUCCESS("成功"),
    FAIL("失败"),
    TAUNT("嘲讽"),
    INSECT("虫害");

    private final String soundName;

    SoundType(String soundName) {
        this.soundName = soundName;
    }

    public String getSoundName() {
        return soundName;
    }
}
