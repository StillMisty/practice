package cn.jxufe.controller;

import cn.jxufe.model.dto.SeedDTO;
import cn.jxufe.model.enums.LandType;
import cn.jxufe.model.enums.SeedType;
import cn.jxufe.service.FileStorageService;
import cn.jxufe.service.SeedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
@RequestMapping("/api/seeds")
@RequiredArgsConstructor
@Tag(name = "种子管理", description = "种子的增删改查接口")
public class SeedController {

    private final SeedService seedService;
    private final FileStorageService fileStorageService;

    @PostMapping
    @Operation(summary = "创建新种子", description = "添加一个新的种子信息")
    public ResponseEntity<SeedDTO> createSeed(@Valid @RequestBody SeedDTO seedDTO) {
        return new ResponseEntity<>(seedService.createSeed(seedDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新种子信息", description = "根据ID更新已有的种子信息")
    public ResponseEntity<SeedDTO> updateSeed(
            @Parameter(description = "种子ID") @PathVariable Long id,
            @Valid @RequestBody SeedDTO seedDTO
    ) {
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
    @Operation(summary = "获取所有种子", description = "获取系统中所有种子的列表")
    public ResponseEntity<List<SeedDTO>> getAllSeeds() {
        return ResponseEntity.ok(seedService.getAllSeeds());
    }

    @GetMapping("/search")
    @Operation(summary = "按名称搜索种子", description = "根据种子名称进行模糊搜索")
    public ResponseEntity<List<SeedDTO>> searchSeedsByName(
            @Parameter(description = "种子名称关键字") @RequestParam String name
    ) {
        return ResponseEntity.ok(seedService.searchSeedsByName(name));
    }

    @GetMapping("/type/{seedType}")
    @Operation(summary = "按种子类型查找", description = "获取指定类型的所有种子")
    public ResponseEntity<List<SeedDTO>> findSeedsByType(
            @Parameter(description = "种子类型") @PathVariable SeedType seedType
    ) {
        return ResponseEntity.ok(seedService.findSeedsByType(seedType));
    }

    @GetMapping("/level/{level}")
    @Operation(summary = "按种子等级查找", description = "获取指定等级的所有种子")
    public ResponseEntity<List<SeedDTO>> findSeedsByLevel(
            @Parameter(description = "种子等级") @PathVariable int level
    ) {
        return ResponseEntity.ok(seedService.findSeedsByLevel(level));
    }

    @GetMapping("/land/{landType}")
    @Operation(summary = "按土地需求查找", description = "获取需要特定土地类型的所有种子")
    public ResponseEntity<List<SeedDTO>> findSeedsByLandRequirement(
            @Parameter(description = "土地类型") @PathVariable LandType landType
    ) {
        return ResponseEntity.ok(seedService.findSeedsByLandRequirement(landType));
    }

    @GetMapping("/price-range")
    @Operation(summary = "按价格区间查找", description = "获取指定价格区间内的所有种子")
    public ResponseEntity<List<SeedDTO>> findSeedsByPriceRange(
            @Parameter(description = "最低价格") @RequestParam double min,
            @Parameter(description = "最高价格") @RequestParam double max
    ) {
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
            @Parameter(description = "玩家ID") @PathVariable Long playerId
    ) {
        return ResponseEntity.ok(seedService.getSeedsByPlayerId(playerId));
    }

    @PostMapping("/{id}/image")
    @Operation(summary = "上传种子图片", description = "为特定种子上传图片")
    @ApiResponse(responseCode = "200", description = "图片上传成功",
        content = @Content(mediaType = "application/json", 
        schema = @Schema(implementation = SeedDTO.class)))
    @ApiResponse(responseCode = "500", description = "图片上传失败")
    public ResponseEntity<SeedDTO> uploadSeedImage(
            @Parameter(description = "种子ID") @PathVariable Long id,
            @Parameter(description = "图片文件", 
                    content = @Content(mediaType = "multipart/form-data"),
                    schema = @Schema(type = "string", format = "binary")) 
            @RequestParam("file") MultipartFile file
    ) {
        try {
            // 获取当前种子信息
            SeedDTO seedDTO = seedService.getSeedById(id);
            
            // 删除旧图片（如果存在）
            if (seedDTO.getImagePath() != null && !seedDTO.getImagePath().isEmpty()) {
                fileStorageService.deleteFile(seedDTO.getImagePath());
            }
            
            // 上传新图片
            String imagePath = fileStorageService.storeFile(file, "seeds");
            
            // 更新种子的图片路径
            seedDTO.setImagePath(imagePath);
            return ResponseEntity.ok(seedService.updateSeed(id, seedDTO));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}/image")
    @Operation(summary = "获取种子图片", description = "获取特定种子的图片")
    public ResponseEntity<Resource> getSeedImage(
            @Parameter(description = "种子ID") @PathVariable Long id
    ) {
        SeedDTO seedDTO = seedService.getSeedById(id);
        
        if (seedDTO.getImagePath() == null || seedDTO.getImagePath().isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            Path imagePath = fileStorageService.getFilePath(seedDTO.getImagePath());
            Resource resource = new UrlResource(imagePath.toUri());
            
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}/image")
    @Operation(summary = "删除种子图片", description = "删除特定种子的图片")
    public ResponseEntity<SeedDTO> deleteSeedImage(
            @Parameter(description = "种子ID") @PathVariable Long id
    ) {
        SeedDTO seedDTO = seedService.getSeedById(id);
        
        if (seedDTO.getImagePath() != null && !seedDTO.getImagePath().isEmpty()) {
            // 删除图片文件
            fileStorageService.deleteFile(seedDTO.getImagePath());
            
            // 更新实体
            seedDTO.setImagePath(null);
            return ResponseEntity.ok(seedService.updateSeed(id, seedDTO));
        }
        
        return ResponseEntity.ok(seedDTO);
    }
}