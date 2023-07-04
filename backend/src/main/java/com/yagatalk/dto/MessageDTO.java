package com.yagatalk.dto;

import com.yagatalk.openaiclient.Role;

import java.time.LocalDateTime;

public record MessageDTO(Role role, LocalDateTime localDateTime,String content) {
}
