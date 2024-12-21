package pers.nefedov.socks.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pers.nefedov.socks.dto.SocksDto;
import pers.nefedov.socks.dto.SocksUpdateDto;
import pers.nefedov.socks.services.SocksService;

import java.util.List;

@RestController
public class SocksController implements SwaggerSocksController {

    private final SocksService socksService;

    public SocksController(SocksService socksService) {
        this.socksService = socksService;
    }


    @Override
    public ResponseEntity<SocksDto> add(SocksDto socksDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .body(socksService.add(socksDto));
    }

    @Override
    public ResponseEntity<SocksDto> subtract(SocksDto socksDto) {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(socksService.subtract(socksDto));
    }

    @Override
    public int get(String color, Double cottonPercentage, String comparison) {
        return socksService.get(color, cottonPercentage, comparison);
    }

    @Override
    public ResponseEntity<SocksDto> update(long id, SocksUpdateDto socksUpdateDto) {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(socksService.update(id, socksUpdateDto));
    }

    @Override
    public ResponseEntity<List<SocksDto>> addBatch(MultipartFile file) {
        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(socksService.addBatch(file));
    }
}
