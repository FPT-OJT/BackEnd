package com.fpt.ojt.exceptions;

import com.fpt.ojt.presentations.dtos.responses.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class DetailedErrorResponse extends ErrorResponse {
    @Schema(
            name = "items",
            description = "Error message",
            type = "Map",
            nullable = true,
            example = "{\"foo\": \"Bar\"}")
    private Map<String, String> items;
}
