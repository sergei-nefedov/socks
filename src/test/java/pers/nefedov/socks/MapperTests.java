package pers.nefedov.socks;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pers.nefedov.socks.dto.SocksDto;
import pers.nefedov.socks.mappers.SocksMapper;
import pers.nefedov.socks.models.Socks;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MapperTests {
    private final SocksMapper socksMapper;

    @Autowired
    public MapperTests(SocksMapper socksMapper) {
        this.socksMapper = socksMapper;
    }

    @Test
    public void givenSocksDTO_whenMapsToSocks_thenCorrect() {
        SocksDto socksDto = new SocksDto();
        socksDto.setColor("Черный");
        socksDto.setCottonPercentage(70.5);
        socksDto.setQuantity(50);

        Socks entity = socksMapper.socksDtoToSocks(socksDto);

        assertEquals(socksDto.getColor(), entity.getColor());
        assertEquals(socksDto.getCottonPercentage(), entity.getCottonPercentage());
        assertEquals(socksDto.getQuantity(), entity.getQuantity());
    }

    @Test
    public void givenSocks_whenMapsToSocksDTO_thenCorrect() {
        Socks socks = new Socks();
        socks.setId(10L);
        socks.setColor("Черный");
        socks.setCottonPercentage(70.5);
        socks.setQuantity(50);

        SocksDto socksDto = socksMapper.socksToSocksDto(socks);

        assertEquals(socksDto.getId(), socks.getId());
        assertEquals(socksDto.getColor(), socks.getColor());
        assertEquals(socksDto.getCottonPercentage(), socks.getCottonPercentage());
        assertEquals(socksDto.getQuantity(), socks.getQuantity());
    }
}
