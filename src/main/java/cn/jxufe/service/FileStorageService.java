package cn.jxufe.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

/**
 * 文件存储服务，用于处理图片上传和管理
 */
public interface FileStorageService {
    
    /**
     * 存储文件
     * 
     * @param file 要存储的文件
     * @param directory 存储目录
     * @return 文件的访问路径
     * @throws IOException 如果文件存储过程中发生IO异常
     */
    String storeFile(MultipartFile file, String directory) throws IOException;
    
    /**
     * 删除文件
     * 
     * @param filePath 文件路径
     * @return 是否成功删除
     */
    boolean deleteFile(String filePath);
    
    /**
     * 获取文件的绝对路径
     * 
     * @param relativePath 相对路径
     * @return 文件的绝对路径
     */
    Path getFilePath(String relativePath);
}