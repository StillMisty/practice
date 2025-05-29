package cn.jxufe.controller;

import cn.jxufe.model.dto.CropStatusesResponse;
import cn.jxufe.model.dto.GrowthCharacteristicDTO;
import cn.jxufe.model.enums.CropStatus;
import cn.jxufe.service.GrowthCharacteristicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/growth-characteristics")
@RequiredArgsConstructor
@Tag(name = "生长特性管理", description = "种子生长特性的增删改查接口")
public class GrowthCharacteristicController {

    private final GrowthCharacteristicService growthCharacteristicService;

    @PostMapping
    @Operation(summary = "创建新的生长特性", description = "添加一个新的种子生长特性")
    public ResponseEntity<GrowthCharacteristicDTO> createGrowthCharacteristic(
            @Valid @RequestBody GrowthCharacteristicDTO dto
    ) {
        return new ResponseEntity<>(growthCharacteristicService.createGrowthCharacteristic(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新生长特性", description = "根据ID更新已有的生长特性信息")
    public ResponseEntity<GrowthCharacteristicDTO> updateGrowthCharacteristic(
            @Parameter(description = "生长特性ID") @PathVariable Long id,
            @Valid @RequestBody GrowthCharacteristicDTO dto
    ) {
        return ResponseEntity.ok(growthCharacteristicService.updateGrowthCharacteristic(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除生长特性", description = "根据ID删除生长特性")
    public ResponseEntity<Void> deleteGrowthCharacteristic(@Parameter(description = "生长特性ID") @PathVariable Long id) {
        growthCharacteristicService.deleteGrowthCharacteristic(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取单个生长特性信息", description = "根据ID获取生长特性详细信息")
    public ResponseEntity<GrowthCharacteristicDTO> getGrowthCharacteristicById(
            @Parameter(description = "生长特性ID") @PathVariable Long id
    ) {

        return ResponseEntity.ok(growthCharacteristicService.getGrowthCharacteristicById(id));
    }

    @GetMapping
    @Operation(summary = "分页获取所有生长特性", description = "获取系统中所有生长特性的列表")
    public ResponseEntity<Page<GrowthCharacteristicDTO>> getAllGrowthCharacteristics(
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "排序所依据的数据库中字段名") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "排序方向") Sort.Direction sortDirection
    ) {
        Sort sort = Sort.by(sortDirection, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(growthCharacteristicService.getAllGrowthCharacteristics(pageable));
    }

    @GetMapping("/seed/{seedId}")
    @Operation(summary = "获取特定种子的所有生长特性", description = "获取指定种子的所有生长特性信息")
    public ResponseEntity<List<GrowthCharacteristicDTO>> getGrowthCharacteristicsBySeedId(
            @Parameter(description = "种子ID") @PathVariable Long seedId,
            @Parameter(description = "排序所依据的数据库中字段名") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "排序方向") @RequestParam(required = false) Sort.Direction sortDirection
    ) {
        if (sortDirection == null) {
            sortDirection = Sort.Direction.ASC; // 默认排序方向
        }
        Sort sort = Sort.by(sortDirection, sortBy);
        return ResponseEntity.ok(growthCharacteristicService.getGrowthCharacteristicsBySeedId(seedId,sort));
    }

    @GetMapping("/stage/{growthStage}")
    @Operation(summary = "按生长阶段查询生长特性", description = "获取特定生长阶段的所有生长特性")
    public ResponseEntity<List<GrowthCharacteristicDTO>> getGrowthCharacteristicsByStage(
            @Parameter(description = "生长阶段") @PathVariable int growthStage
    ) {
        return ResponseEntity.ok(growthCharacteristicService.getGrowthCharacteristicsByStage(growthStage));
    }

    @GetMapping("/status/{cropStatus}")
    @Operation(summary = "按作物状态查询生长特性", description = "获取特定作物状态的所有生长特性")
    public ResponseEntity<List<GrowthCharacteristicDTO>> getGrowthCharacteristicsByCropStatus(
            @Parameter(description = "作物状态") @PathVariable CropStatus cropStatus
    ) {
        return ResponseEntity.ok(growthCharacteristicService.getGrowthCharacteristicsByCropStatus(cropStatus));
    }

    @GetMapping("/seed/{seedId}/stage/{growthStage}")
    @Operation(summary = "获取特定种子特定生长阶段的生长特性", description = "根据种子ID和生长阶段获取生长特性")
    public ResponseEntity<GrowthCharacteristicDTO> getGrowthCharacteristicBySeedIdAndStage(
            @Parameter(description = "种子ID") @PathVariable Long seedId,
            @Parameter(description = "生长阶段") @PathVariable int growthStage
    ) {
        return ResponseEntity
                .ok(growthCharacteristicService.getGrowthCharacteristicBySeedIdAndStage(seedId, growthStage));
    }

    @GetMapping("/pest-probability")
    @Operation(summary = "获取虫害概率大于特定值的生长特性", description = "查找虫害发生概率大于指定阈值的生长特性")
    public ResponseEntity<List<GrowthCharacteristicDTO>> getGrowthCharacteristicsByPestProbability(
            @Parameter(description = "虫害概率阈值") @RequestParam double probability
    ) {
        return ResponseEntity.ok(growthCharacteristicService.getGrowthCharacteristicsByPestProbability(probability));
    }

    @GetMapping("/search")
    @Operation(summary = "按生长阶段标题搜索", description = "根据生长阶段标题进行模糊搜索")
    public ResponseEntity<List<GrowthCharacteristicDTO>> searchGrowthCharacteristicsByTitle(
            @Parameter(description = "标题关键字") @RequestParam String title
    ) {
        return ResponseEntity.ok(growthCharacteristicService.searchGrowthCharacteristicsByTitle(title));
    }

    @GetMapping("/seed/{seedId}/sorted")
    @Operation(summary = "获取特定种子的所有生长阶段并排序", description = "获取指定种子的所有生长特性，按生长阶段排序")
    public ResponseEntity<List<GrowthCharacteristicDTO>> getGrowthCharacteristicsBySeedIdSorted(
            @Parameter(description = "种子ID") @PathVariable Long seedId
    ) {
        return ResponseEntity.ok(growthCharacteristicService.getGrowthCharacteristicsBySeedIdSorted(seedId));
    }

    @PostMapping(path = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "上传生长特性图片", description = "为特定的生长特性上传图片")
    public ResponseEntity<GrowthCharacteristicDTO> uploadGrowthCharacteristicImage(
            @Parameter(description = "生长特性ID") @PathVariable Long id,
            @Parameter(description = "图片文件") @RequestParam("file") MultipartFile file
    ) throws IOException {
        return ResponseEntity.ok(growthCharacteristicService.updateGrowthCharacteristicImage(id, file));
    }

    @GetMapping("/{id}/image")
    @Operation(summary = "获取生长特性图片", description = "获取特定生长特性的图片")
    public ResponseEntity<Resource> getGrowthCharacteristicImage(
            @Parameter(description = "生长特性ID") @PathVariable Long id
    ) throws MalformedURLException {
        Path imagePath = growthCharacteristicService.getGrowthCharacteristicImagePath(id);
        if (imagePath == null) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(imagePath.toUri());

        // 检查资源是否存在且可读
        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/image")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "删除生长特性图片", description = "删除特定生长特性的图片")
    public void deleteGrowthCharacteristicImage(
            @Parameter(description = "生长特性ID") @PathVariable Long id
    ) {
        growthCharacteristicService.deleteGrowthCharacteristicImage(id);
    }

    @GetMapping("/crop-status")
    @Operation(summary = "获取生长特性的所有可用作物状态", description = "获取生长特性的所有可用作物状态")
    public ResponseEntity<List<CropStatusesResponse>> getAllCropStatuses() {
        return ResponseEntity.ok(growthCharacteristicService.getAllCropStatuses());
    }
}