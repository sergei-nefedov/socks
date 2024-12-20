package pers.nefedov.socks.services;

import org.springframework.stereotype.Service;
import pers.nefedov.socks.dto.SocksDto;

import java.util.List;

public interface SocksService {
    SocksDto add(SocksDto socksDto);

    SocksDto subtract(SocksDto socksDto);

    List<SocksDto> get();
}
