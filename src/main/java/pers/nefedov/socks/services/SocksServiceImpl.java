package pers.nefedov.socks.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.nefedov.socks.dto.SocksDto;
import pers.nefedov.socks.exceptions.ShortageInStockException;
import pers.nefedov.socks.mappers.SocksMapper;
import pers.nefedov.socks.models.Socks;
import pers.nefedov.socks.repositories.SocksRepository;

import java.util.List;

@Service
public class SocksServiceImpl implements SocksService {
    private final SocksMapper socksMapper;
    private final SocksRepository socksRepository;
    final Logger logger = LoggerFactory.getLogger(SocksServiceImpl.class);

    public SocksServiceImpl(SocksMapper socksMapper, SocksRepository socksRepository) {
        this.socksMapper = socksMapper;
        this.socksRepository = socksRepository;
    }

    @Override
    @Transactional
    public SocksDto add(SocksDto socksDto) {
        Socks incomingSocks = socksMapper.socksDtoToSocks(socksDto);
        Socks socksAlreadyInStock = socksRepository.getSocksByColorAndCottonPercentage(socksDto.getColor(), socksDto.getCottonPercentage());
        if (socksAlreadyInStock == null) {
            Socks storedSocks = socksRepository.save(incomingSocks);
            logger.info("Новая партия носков (id={}) принята на склад, цвет - {}, содержание хлопка - {}, количество " +
                            "- {}",
                    storedSocks.getId(), storedSocks.getColor(), storedSocks.getCottonPercentage(),
                    storedSocks.getQuantity());
            return socksMapper.socksToSocksDto(storedSocks);
        }

        int newQuantity = socksAlreadyInStock.getQuantity() + incomingSocks.getQuantity();
        socksAlreadyInStock.setQuantity(newQuantity);
        Socks storedSocks = socksRepository.save(socksAlreadyInStock);
        logger.info("Партия носков, идентичная имеющейся (id={}, цвет - {}, содержание хлопка - {}) принята на склад," +
                        " количество увеличилось на {} и составило {}",
                storedSocks.getId(), storedSocks.getColor(), storedSocks.getCottonPercentage(),
                incomingSocks.getQuantity(), storedSocks.getQuantity());
        return socksMapper.socksToSocksDto(storedSocks);
    }

    @Override
    @Transactional
    public SocksDto subtract(SocksDto socksDto) {
        Socks socksAlreadyInStock = socksRepository.getSocksByColorAndCottonPercentage(socksDto.getColor(), socksDto.getCottonPercentage());
        int quantityAfterSubtraction = socksAlreadyInStock.getQuantity() - socksDto.getQuantity();
        if (quantityAfterSubtraction < 0) throw new ShortageInStockException("Remaining socks in stock are less than " +
                "requested by " + quantityAfterSubtraction);
        socksAlreadyInStock.setQuantity(quantityAfterSubtraction);
        Socks storedSocks = socksRepository.save(socksAlreadyInStock);
        logger.info("Партия носков (id={}, цвет - {}, содержание хлопка - {}) выдана со склада," +
                        " количество уменьшилось на {} и составило {}",
                storedSocks.getId(), storedSocks.getColor(), storedSocks.getCottonPercentage(),
                socksDto.getQuantity(), storedSocks.getQuantity());
        return socksMapper.socksToSocksDto(storedSocks);
    }

    @Override
    public List<SocksDto> get() {
        return socksMapper.socksToSocksDto(socksRepository.findAll());//TODO должно возвращаться количество, фильтры
        // сделать
    }


}
