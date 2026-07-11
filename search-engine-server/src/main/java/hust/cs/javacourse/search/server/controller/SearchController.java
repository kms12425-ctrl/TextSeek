package hust.cs.javacourse.search.server.controller;

import hust.cs.javacourse.search.server.dto.ApiResult;
import hust.cs.javacourse.search.server.dto.SearchResponse;
import hust.cs.javacourse.search.server.service.IndexService;
import org.springframework.web.bind.annotation.*;

/**
 * 搜索控制器
 */
@RestController
@RequestMapping("/api")
public class SearchController {

    private final IndexService indexService;

    public SearchController(IndexService indexService) {
        this.indexService = indexService;
    }

    /**
     * 单/双关键词搜索
     *
     * @param q    搜索词（单词用空格分隔，多词时配合 mode 使用）
     * @param mode AND / OR（默认 OR）
     * @param page 页码（0 开始）
     * @param size 每页条数（默认 10）
     */
    @GetMapping("/search")
    public ApiResult<SearchResponse> search(
            @RequestParam("q") String query,
            @RequestParam(defaultValue = "or") String mode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (query == null || query.isBlank()) {
            return ApiResult.badRequest("查询词不能为空");
        }

        String trimmed = query.trim();
        String[] words = trimmed.split("\\s+");

        SearchResponse response;

        if (words.length == 1) {
            response = indexService.search(words[0], page, size);
        } else if (words.length >= 2) {
            // 取前两个词
            response = indexService.search(words[0], words[1], mode, page, size);
        } else {
            response = new SearchResponse();
        }

        return ApiResult.ok(response);
    }
}
