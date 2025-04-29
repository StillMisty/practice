package cn.jxufe.service.impl;

import cn.jxufe.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Override
    public String storeFile(MultipartFile file, String directory) throws IOException {
        // 检查文件是否为空
        if (file.isEmpty()) {
            throw new IOException("无法存储空文件");
        }
        
        // 获取文件名
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        
        // 检查文件名是否包含无效字符
        if (filename.contains("..")) {
            throw new IOException("文件名包含无效的路径序列：" + filename);
        }
        
        // 生成唯一文件名，防止文件覆盖
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        
        // 创建目录
        Path dirPath = Paths.get(uploadDir, directory).toAbsolutePath().normalize();
        Files.createDirectories(dirPath);
        
        // 保存文件
        Path targetLocation = dirPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        
        // 返回相对路径
        return directory + "/" + uniqueFilename;
    }

    @Override
    public boolean deleteFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }
        
        try {
            Path path = getFilePath(filePath);
            return Files.deleteIfExists(path);
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public Path getFilePath(String relativePath) {
        return Paths.get(uploadDir).resolve(relativePath).normalize();
    }
}