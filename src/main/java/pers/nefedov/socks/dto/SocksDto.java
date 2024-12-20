package pers.nefedov.socks.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocksDto {
    @Nullable
    private Long id;
    @Schema(description = "Цвет носков", example = "Черный")
    @NotEmpty(message = "Цвет обязателен")
    @NotBlank
    @Size(min = 3, max = 20)
    private String color;
    @Schema(description = "Содержание хлопка в процентах, может быть дробным", example = "70.0")
    @Min(0)
    @Max(100)
    private Double cottonPercentage;
    @Schema(description = "Количество", example = "100")
    @Min(1)
    @Max(1_000_000)
    private Integer quantity;
}
