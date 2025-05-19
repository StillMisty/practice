package cn.jxufe.model.dto;

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
    private String type; // 操作类型，如 plant, killWorm, harvest, cleanLand, insect
    @Schema(description = "消息内容")
    private String msg;
    @Schema(description = "音效类型")
    private String sound; // 音效类型，如 success, fail, taunt, insect
    @Schema(description = "数据")
    private T data;

    public Message(int code, String type, String msg, String sound) {
        this.code = code;
        this.type = type;
        this.msg = msg;
        this.sound = sound;
        this.data = null;
    }
}
