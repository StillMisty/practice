package cn.jxufe.model.dto;

import cn.jxufe.model.enums.FarmAction;
import cn.jxufe.model.enums.SoundType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Message<T> {
    @Schema(description = "1: 正常, -1: 错误")
    private int code;
    @Schema(description = "消息类型")
    private FarmAction type; // 操作类型，如 plant, killWorm, harvest, cleanLand, insect
    @Schema(description = "消息内容")
    private String msg;
    @Schema(description = "音效类型")
    private SoundType sound; // 音效类型，如 success, fail, taunt, insect
    @Schema(description = "数据")
    private T data;
    @Schema(description = "是否显示提示框")
    private boolean showDialog;

    public Message(int code, FarmAction type, String msg, SoundType sound) {
        this.code = code;
        this.type = type;
        this.msg = msg;
        this.sound = sound;
        this.data = null;
        this.showDialog = true;
    }

    public Message(int code, FarmAction type, String msg, SoundType sound, T data) {
        this.code = code;
        this.type = type;
        this.msg = msg;
        this.sound = sound;
        this.data = data;
        this.showDialog = true;
    }
}
