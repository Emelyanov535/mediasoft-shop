package ru.mediasoft.shop.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mediasoft.shop.controller.dto.CriteriaData;
import ru.mediasoft.shop.controller.dto.ProductFilterDto;
import ru.mediasoft.shop.service.SearchService;
import ru.mediasoft.shop.service.dto.ProductDto;

import java.util.List;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/api/v1/search")
@Tag(name = "Поиск", description = "Многокритериальный поиск")
public class SearchController {

    private final SearchService searchService;

    @GetMapping()
    public ResponseEntity<List<ProductDto>> simpleSearch(@Validated ProductFilterDto productFilterDto){
        return ResponseEntity.ok(searchService.simpleSearch(productFilterDto));
    }

    @PostMapping("/hard")
    public Page<ProductDto> hardSearch(
            Pageable pageable,
            @RequestBody List<@Valid CriteriaData<?>> data
    ){
        return searchService.hardSearch(pageable, data);
    }
}
