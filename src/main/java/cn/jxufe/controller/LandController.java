package cn.jxufe.controller;

import cn.jxufe.model.dto.LandTypeResponse;
import cn.jxufe.model.enums.LandType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(("/api/lands"))
@Tag(name = "土地相关接口")
public class LandController {

    @GetMapping("/types")
    @Operation(summary = "获取所有土地类型")
    public List<LandTypeResponse> getAllLandTypes() {
        return Arrays.stream(LandType.values())
                .map(type -> new LandTypeResponse(type.name(), type.getChineseName()))
                .collect(Collectors.toList());
    }
}

