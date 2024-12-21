package pers.nefedov.socks.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pers.nefedov.socks.dto.SocksDto;
import pers.nefedov.socks.dto.SocksUpdateDto;

import java.util.List;

public interface SocksService {
    SocksDto add(SocksDto socksDto);

    SocksDto subtract(SocksDto socksDto);

    List<SocksDto> get();

    SocksDto update(long id, SocksUpdateDto socksUpdateDto);

    List<SocksDto> addBatch(MultipartFile file);
}
