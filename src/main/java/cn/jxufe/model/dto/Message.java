package cn.jxufe.model.dto;

import cn.jxufe.model.enums.FarmAction;
import cn.jxufe.model.enums.SoundType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Message<T> {
    @Schema(description = "1: 正常, -1: 错误")
    private int code;
    @Schema(description = "消息类型")
    private FarmAction type;
    @Schema(description = "消息内容")
    private String msg;
    @Schema(description = "音效类型")
    private SoundType sound;
    @Schema(description = "数据")
    private T data;
    @Schema(description = "是否显示提示框")
    private boolean showDialog = true;

    public Message(int code, FarmAction type, String msg, SoundType sound, T data) {
        this.code = code;
        this.type = type;
        this.msg = msg;
        this.sound = sound;
        this.data = data;
    }

    // 无数据的静态工厂方法
    public static Message<?> of(int code, FarmAction type, String msg, SoundType sound) {
        return new Message<>(code, type, msg, sound, null);
    }

    // 有数据的静态工厂方法
    public static <T> Message<T> of(int code, FarmAction type, String msg, SoundType sound, T data) {
        return new Message<>(code, type, msg, sound, data);
    }
}