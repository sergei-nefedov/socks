package pers.nefedov.socks.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pers.nefedov.socks.dto.SocksDto;
import pers.nefedov.socks.dto.SocksUpdateDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RequestMapping("/api/socks")
public interface SwaggerSocksController {

    @PostMapping("/income")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Регистрация прихода носков",
            description = "Параметры: цвет носков, процентное содержание хлопка, количество. " +
                    "Увеличивает количество носков на складе. Если на складе уже имелись носки " +
                    "такого же цвета и с таким же содержанием хлопка, новое поступление будет " +
                    "присоединено к имеющемуся количеству с существующим id. Если таких носков не имелось, id будет " +
                    "присвоен автоматически"
    )
    ResponseEntity<SocksDto> add(@Validated @RequestBody SocksDto socksDto);

    @PostMapping("/outcome")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Регистрация отпуска носков",
            description = "Параметры: цвет носков, процентное содержание хлопка, количество.\n" +
                    "Уменьшает количество носков на складе, если их хватает. Уменьшается количество носков такого " +
                    "же цвета и с таким же содержанием хлопка, переданный id игнорируется."
    )
    ResponseEntity<SocksDto> subtract(@Validated @RequestBody SocksDto socksDto);

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Получение общего количества носков с фильтрацией",
            description = """
                    Фильтры:
                    Цвет носков.
                    Оператор сравнения (moreThan, lessThan, equal).
                    Процент содержания хлопка.
                    Возвращает количество носков, соответствующих критериям."""
    )

    int get(@RequestParam(required = false) String color,
            @RequestParam(required = false) Double cottonPercentage,
            @RequestParam(required = false) String comparison);

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Обновление данных носков",
            description = "Позволяет изменить параметры носков (цвет, процент хлопка, количество) по заданному id. Id" +
                    " передается через параметр пути, остальные параметры - через тело запроса. Обновляются только " +
                    "переданные параметры, остальные могут не передаваться. Id, переданный в теле запроса, " +
                    "игнорируется."
    )
    ResponseEntity<SocksDto> update(@PathVariable @Valid @Positive long id,
                                    @Validated @RequestBody SocksUpdateDto socksUpdateDto);

    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Прием нескольких партий носков",
            description = "Принимает Excel файл с партиями носков, содержащими цвет, процентное содержание хлопка и " +
                    "количество. Если на складе уже имелись носки  такого же цвета и с таким же содержанием хлопка, " +
                    "новое поступление будет присоединено к имеющемуся количеству."
    )
    ResponseEntity<List<SocksDto>> addBatch(@RequestParam("file") MultipartFile file);
}
