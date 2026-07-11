package hust.cs.javacourse.search.server.controller;

import hust.cs.javacourse.search.server.dto.ApiResult;
import hust.cs.javacourse.search.server.service.IndexService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 索引状态控制器
 */
@RestController
@RequestMapping("/api")
public class StatusController {

    private final IndexService indexService;

    public StatusController(IndexService indexService) {
        this.indexService = indexService;
    }

    /**
     * 获取索引统计信息
     */
    @GetMapping("/status")
    public ApiResult<Map<String, Object>> status() {
        return ApiResult.ok(indexService.getStatus());
    }
}
