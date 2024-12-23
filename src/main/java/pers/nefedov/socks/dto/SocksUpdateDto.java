package pers.nefedov.socks.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocksUpdateDto {
    @Nullable
    private Long id;
    @Nullable
    @Schema(description = "Цвет носков", example = "Черный")
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[\\p{L}\\p{N}\\p{Z}\\p{P}]*$", message = "Название цвета должно состоять только из отображаемых символов")
    private String color;
    @Nullable
    @Schema(description = "Содержание хлопка в процентах, может быть дробным", example = "70.0")
    @Min(0)
    @Max(100)
    private Double cottonPercentage;
    @Nullable
    @Schema(description = "Количество", example = "100")
    @Min(1)
    @Max(1_000_000)
    private Integer quantity;
}
