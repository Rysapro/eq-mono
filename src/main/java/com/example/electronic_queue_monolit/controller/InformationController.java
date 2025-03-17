package com.example.electronic_queue_monolit.controller;

import com.example.electronic_queue_monolit.controller.base.BaseController;
import com.example.electronic_queue_monolit.domain.dto.InformationDto;
import com.example.electronic_queue_monolit.domain.model.Information;
import com.example.electronic_queue_monolit.service.InformationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/information")
@Tag(name = "Информация о декларации")
public class InformationController extends BaseController<Information, InformationDto, InformationService> {

    public InformationController(InformationService service) {
        super(service);
    }

    @Override
    protected String getBasePath() {
        return "information";
    }

    @Override
    protected InformationDto createEmptyDto() {
        return new InformationDto(null, null);
    }
}