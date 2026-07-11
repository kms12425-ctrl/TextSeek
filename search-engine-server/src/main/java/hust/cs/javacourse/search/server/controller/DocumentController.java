package hust.cs.javacourse.search.server.controller;

import hust.cs.javacourse.search.server.dto.ApiResult;
import hust.cs.javacourse.search.server.dto.DocInfo;
import hust.cs.javacourse.search.server.service.IndexService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文档管理控制器
 */
@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final IndexService indexService;

    public DocumentController(IndexService indexService) {
        this.indexService = indexService;
    }

    /**
     * 上传文档（.txt）
     */
    @PostMapping("/upload")
    public ApiResult<DocInfo> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResult.badRequest("文件为空");
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.toLowerCase().endsWith(".txt")) {
            return ApiResult.badRequest("仅支持 .txt 文件");
        }

        try {
            DocInfo doc = indexService.addDocument(file);
            return ApiResult.ok(doc);
        } catch (Exception e) {
            return ApiResult.internalError("上传失败: " + e.getMessage());
        }
    }

    /**
     * 列出所有已索引文档
     */
    @GetMapping
    public ApiResult<List<DocInfo>> list() {
        return ApiResult.ok(indexService.listDocuments());
    }

    /**
     * 获取某个文档详情（当前返回基本信息）
     */
    @GetMapping("/{docId}")
    public ApiResult<DocInfo> get(@PathVariable int docId) {
        List<DocInfo> docs = indexService.listDocuments();
        for (DocInfo d : docs) {
            if (d.getDocId() == docId) {
                return ApiResult.ok(d);
            }
        }
        return ApiResult.notFound("文档不存在: " + docId);
    }

    /**
     * 删除文档
     */
    @DeleteMapping("/{docId}")
    public ApiResult<Void> delete(@PathVariable int docId) {
        indexService.deleteDocument(docId);
        return ApiResult.ok();
    }
}
