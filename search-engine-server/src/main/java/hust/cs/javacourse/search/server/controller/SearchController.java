package hust.cs.javacourse.search.server.controller;

import hust.cs.javacourse.search.server.dto.ApiResult;
import hust.cs.javacourse.search.server.dto.SearchResponse;
import hust.cs.javacourse.search.server.service.IndexService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SearchController {

    private final IndexService indexService;

    public SearchController(IndexService indexService) {
        this.indexService = indexService;
    }

    @GetMapping("/search")
    public ApiResult<SearchResponse> search(
            @RequestParam("q") String query,
            @RequestParam(name = "mode", defaultValue = "or") String mode,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        if (query == null || query.isBlank()) {
            return ApiResult.badRequest("查询词不能为空");
        }

        String[] words = query.trim().split("\\s+");
        SearchResponse response;

        if (words.length == 1) {
            response = indexService.search(words[0], page, size);
        } else {
            response = indexService.search(words[0], words[1], mode, page, size);
        }

        return ApiResult.ok(response);
    }
}
