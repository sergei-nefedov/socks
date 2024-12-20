package pers.nefedov.socks.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Service;
import pers.nefedov.socks.dto.SocksDto;
import pers.nefedov.socks.models.Socks;

import java.util.List;

//@Service
@Mapper(componentModel = "spring")
public interface SocksMapper {

//    @Mapping(target = "color", source = "socksDto.color")
//    @Mapping(target = "cottonPercentage", source = "socksDto.cottonPercentage")
//    @Mapping(target = "quantity", source = "socksDto.quantity")
    Socks socksDtoToSocks(SocksDto socksDto);

//    @Mapping(target = "id", source = "socks.id")
//    @Mapping(target = "color", source = "socks.color")
//    @Mapping(target = "cottonPercentage", source = "socks.cottonPercentage")
//    @Mapping(target = "quantity", source = "socks.quantity")
    SocksDto socksToSocksDto(Socks socks);

    List<Socks> socksDtoToSocks(List<SocksDto> socksDtoList);

    List<SocksDto> socksToSocksDto(List<Socks> socksDtoList);
}

