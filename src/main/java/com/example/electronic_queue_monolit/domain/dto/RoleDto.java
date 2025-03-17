package com.example.electronic_queue_monolit.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RoleDto{
    private Long id;
    private String name;
    private PageDto page;
    private Long pageId;
}