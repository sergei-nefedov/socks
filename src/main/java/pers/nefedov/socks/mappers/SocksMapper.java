package pers.nefedov.socks.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pers.nefedov.socks.dto.SocksDto;
import pers.nefedov.socks.models.Socks;

@Mapper(componentModel = "spring")
public interface SocksMapper {

    @Mapping(target = "id", expression = "java(null)")
    Socks socksDtoToSocks(SocksDto socksDto);

    SocksDto socksToSocksDto(Socks socks);
}

