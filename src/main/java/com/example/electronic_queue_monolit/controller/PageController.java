package com.example.electronic_queue_monolit.controller;

import com.example.electronic_queue_monolit.controller.base.BaseController;
import com.example.electronic_queue_monolit.domain.dto.PageDto;
import com.example.electronic_queue_monolit.domain.model.PageForRole;
import com.example.electronic_queue_monolit.service.PageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page")
@Tag(name = "Страница")
public class PageController extends BaseController<PageForRole, PageDto, PageService> {

    public PageController(PageService service) {
        super(service);
    }

    @Override
    protected String getBasePath() {
        return "page";
    }

    @Override
    protected PageDto createEmptyDto() {
        return new PageDto(null, null);
    }
}
