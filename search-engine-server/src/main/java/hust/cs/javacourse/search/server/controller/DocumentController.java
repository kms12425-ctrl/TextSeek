package hust.cs.javacourse.search.server.controller;

import hust.cs.javacourse.search.server.dto.ApiResult;
import hust.cs.javacourse.search.server.dto.DocInfo;
import hust.cs.javacourse.search.server.dto.DocumentDetail;
import hust.cs.javacourse.search.server.service.IndexService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final IndexService indexService;

    public DocumentController(IndexService indexService) {
        this.indexService = indexService;
    }

    @PostMapping("/upload")
    public ApiResult<DocInfo> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResult.badRequest("文件不能为空");
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.toLowerCase().endsWith(".txt")) {
            return ApiResult.badRequest("仅支持上传 .txt 文件");
        }

        try {
            return ApiResult.ok(indexService.addDocument(file));
        } catch (Exception e) {
            return ApiResult.internalError("上传失败: " + e.getMessage());
        }
    }

    @GetMapping
    public ApiResult<List<DocInfo>> list() {
        return ApiResult.ok(indexService.listDocuments());
    }

    @GetMapping("/{docId}")
    public ApiResult<DocumentDetail> get(@PathVariable("docId") int docId) {
        try {
            DocumentDetail detail = indexService.getDocument(docId);
            if (detail == null) {
                return ApiResult.notFound("文档不存在: " + docId);
            }
            return ApiResult.ok(detail);
        } catch (Exception e) {
            return ApiResult.internalError("读取文档失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{docId}")
    public ApiResult<Void> delete(@PathVariable("docId") int docId) {
        indexService.deleteDocument(docId);
        return ApiResult.ok();
    }
}
