package cn.jxufe.controller;

import cn.jxufe.model.dto.SeedDTO;
import cn.jxufe.model.enums.LandType;
import cn.jxufe.model.enums.SeedType;
import cn.jxufe.service.SeedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
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
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/seeds")
@RequiredArgsConstructor
@Tag(name = "种子管理", description = "种子的增删改查接口")
public class SeedController {

    private final SeedService seedService;

    @PostMapping
    @Operation(summary = "创建新种子", description = "添加一个新的种子信息")
    public ResponseEntity<SeedDTO> createSeed(@Valid @RequestBody SeedDTO seedDTO) {
        return new ResponseEntity<>(seedService.createSeed(seedDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新种子信息", description = "根据ID更新已有的种子信息")
    public ResponseEntity<SeedDTO> updateSeed(
            @Parameter(description = "种子ID") @PathVariable Long id,
            @Valid @RequestBody SeedDTO seedDTO) {
        return ResponseEntity.ok(seedService.updateSeed(id, seedDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除种子", description = "根据ID删除种子")
    public ResponseEntity<Void> deleteSeed(@Parameter(description = "种子ID") @PathVariable Long id) {
        seedService.deleteSeed(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取单个种子信息", description = "根据ID获取种子详细信息")
    public ResponseEntity<SeedDTO> getSeedById(@Parameter(description = "种子ID") @PathVariable Long id) {
        return ResponseEntity.ok(seedService.getSeedById(id));
    }

    @GetMapping
    @Operation(summary = "分页获取所有种子", description = "获取系统中所有种子的列表")
    public ResponseEntity<Page<SeedDTO>> getAllSeeds(
            @Parameter(description = "页码，从0开始") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "排序所依据的数据库中字段名") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "排序方向") Sort.Direction sortDirection) {
        Sort sort = Sort.by(sortDirection, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(seedService.getAllSeeds(pageable));
    }

    @GetMapping("/search")
    @Operation(summary = "按名称搜索种子", description = "根据种子名称进行模糊搜索")
    public ResponseEntity<List<SeedDTO>> searchSeedsByName(
            @Parameter(description = "种子名称关键字") @RequestParam String name) {
        return ResponseEntity.ok(seedService.searchSeedsByName(name));
    }

    @GetMapping("/type/{seedType}")
    @Operation(summary = "按种子类型查找", description = "获取指定类型的所有种子")
    public ResponseEntity<List<SeedDTO>> findSeedsByType(
            @Parameter(description = "种子类型") @PathVariable SeedType seedType) {
        return ResponseEntity.ok(seedService.findSeedsByType(seedType));
    }

    @GetMapping("/level/{level}")
    @Operation(summary = "按种子等级查找", description = "获取指定等级的所有种子")
    public ResponseEntity<List<SeedDTO>> findSeedsByLevel(
            @Parameter(description = "种子等级") @PathVariable int level) {
        return ResponseEntity.ok(seedService.findSeedsByLevel(level));
    }

    @GetMapping("/land/{landType}")
    @Operation(summary = "按土地需求查找", description = "获取需要特定土地类型的所有种子")
    public ResponseEntity<List<SeedDTO>> findSeedsByLandRequirement(
            @Parameter(description = "土地类型") @PathVariable LandType landType) {
        return ResponseEntity.ok(seedService.findSeedsByLandRequirement(landType));
    }

    @GetMapping("/price-range")
    @Operation(summary = "按价格区间查找", description = "获取指定价格区间内的所有种子")
    public ResponseEntity<List<SeedDTO>> findSeedsByPriceRange(
            @Parameter(description = "最低价格") @RequestParam double min,
            @Parameter(description = "最高价格") @RequestParam double max) {
        return ResponseEntity.ok(seedService.findSeedsByPriceRange(min, max));
    }

    @GetMapping("/sort-by-price")
    @Operation(summary = "按价格排序获取所有种子", description = "获取所有种子并按价格升序排列")
    public ResponseEntity<List<SeedDTO>> getAllSeedsOrderByPrice() {
        return ResponseEntity.ok(seedService.getAllSeedsOrderByPrice());
    }

    @GetMapping("/player/{playerId}")
    @Operation(summary = "获取玩家拥有的种子", description = "获取特定玩家拥有的所有种子")
    public ResponseEntity<List<SeedDTO>> getSeedsByPlayerId(
            @Parameter(description = "玩家ID") @PathVariable Long playerId) {
        return ResponseEntity.ok(seedService.getSeedsByPlayerId(playerId));
    }

    @PostMapping(path = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "上传种子图片", description = "为特定种子上传图片")
    public ResponseEntity<SeedDTO> uploadSeedImage(
            @Parameter(description = "种子ID") @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(seedService.updateSeedImage(id, file));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/image")
    @Operation(summary = "获取种子图片", description = "获取特定种子的图片")
    public ResponseEntity<Resource> getSeedImage(
            @Parameter(description = "种子ID") @PathVariable Long id) throws IOException {

        // 获取图片文件路径
        Path imagePath = seedService.getSeedImagePath(id);
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
    @Operation(summary = "删除种子图片", description = "删除特定种子的图片")
    public void deleteSeedImage(
            @Parameter(description = "种子ID") @PathVariable Long id) throws IOException {
        seedService.deleteSeedImage(id);
    }
}