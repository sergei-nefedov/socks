package pers.nefedov.socks.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pers.nefedov.socks.dto.SocksDto;

import java.util.List;

@RequestMapping("/api/socks")
public interface SwaggerSocksController {

    @PostMapping("/income")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Регистрация прихода носков",
            description = "Параметры: цвет носков, процентное содержание хлопка, количество.\n" +
                    "Увеличивает количество носков на складе. Если на складе уже имелись носки " +
                    "такого же цвета и с таким же содержанием хлопка, новое поступление будет " +
                    "присоединено к имеющемуся количеству."
    )
    ResponseEntity<SocksDto> add(@Validated @RequestBody SocksDto taskDTO);

    @PostMapping("/outcome")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Регистрация отпуска носков",
            description = "Параметры: цвет носков, процентное содержание хлопка, количество.\n" +
                    "Уменьшает количество носков на складе, если их хватает."
    )
    ResponseEntity<SocksDto> subtract(@Validated @RequestBody SocksDto taskDTO);

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
    ResponseEntity<List<SocksDto>> get();

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Обновление данных носков",
            description = "Позволяет изменить параметры носков (цвет, процент хлопка, количество)."
    )
    ResponseEntity<SocksDto> change(@Validated @RequestBody SocksDto taskDTO);

    @PostMapping("/batch")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Прием нескольких партий носков",
            description = "Принимает Excel файл с партиями носков, содержащими цвет, процентное содержание хлопка и количество."
    )
    ResponseEntity<String> addBatch(@RequestParam("file") MultipartFile file);
}
