package pers.nefedov.socks.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pers.nefedov.socks.dto.SocksDto;
import pers.nefedov.socks.dto.SocksUpdateDto;
import pers.nefedov.socks.exceptions.InvalidRequestParametersException;
import pers.nefedov.socks.exceptions.NoDataForUpdateException;
import pers.nefedov.socks.exceptions.NoSuchSocksInStockException;
import pers.nefedov.socks.exceptions.ShortageInStockException;
import pers.nefedov.socks.mappers.SocksMapper;
import pers.nefedov.socks.models.Socks;
import pers.nefedov.socks.repositories.SocksRepository;
import pers.nefedov.socks.utils.Comparisons;
import pers.nefedov.socks.utils.FileUploader;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SocksServiceImpl implements SocksService {
    private final SocksMapper socksMapper;
    private final FileUploader fileUploader;
    private final SocksRepository socksRepository;
    final Logger logger = LoggerFactory.getLogger(SocksServiceImpl.class);

    public SocksServiceImpl(SocksMapper socksMapper, FileUploader fileUploader, SocksRepository socksRepository) {
        this.socksMapper = socksMapper;
        this.fileUploader = fileUploader;
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
        Socks socksAlreadyInStock = getSocksAlreadyInStockByColorAndCottonPercentage(socksDto);
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

    private Socks getSocksAlreadyInStockByColorAndCottonPercentage(SocksDto socksDto) {
        Optional<Socks> optionalSocks = socksRepository.findByColorAndCottonPercentage(socksDto.getColor(),
                socksDto.getCottonPercentage());
        return optionalSocks.orElseThrow(
                () -> new NoSuchSocksInStockException("Socks with color " + socksDto.getColor() + " and cotton " +
                        "percentage " + socksDto.getCottonPercentage() + " not found"));
    }

    @Override

    public Integer get(String color, Double cottonPercentage, String comparison) {
        validateParameters(cottonPercentage, comparison);
        return socksRepository.sumQuantityWithFilters(color, cottonPercentage, comparison).orElse(0);
    }

    private static void validateParameters(Double cottonPercentage, String comparison) {
        if (cottonPercentage != null && (cottonPercentage < 0 || cottonPercentage > 100) || !Comparisons.contains(comparison) && comparison != null)
            throw new InvalidRequestParametersException(
                    "Percentage or comparison values incorrect");
    }

    @Override
    @Transactional
    public SocksDto update(long id, SocksUpdateDto socksUpdateDto) {
        String newColor = socksUpdateDto.getColor();
        Double newCottonPercentage = socksUpdateDto.getCottonPercentage();
        Integer newQuantity = socksUpdateDto.getQuantity();
        if (newColor == null && newCottonPercentage == null && newQuantity == null)
            throw new NoDataForUpdateException("Request doesn't contain any useful data");
        Socks socksAlreadyInStock = getSocksAlreadyInStockById(id);
        String oldColor = socksAlreadyInStock.getColor();
        Double oldCottonPercentage = socksAlreadyInStock.getCottonPercentage();
        Integer oldQuantity = socksAlreadyInStock.getQuantity();
        if (newColor != null) socksAlreadyInStock.setColor(newColor);
        if (newCottonPercentage != null) socksAlreadyInStock.setCottonPercentage(newCottonPercentage);
        if (newQuantity != null) socksAlreadyInStock.setQuantity(newQuantity);
        Socks storedSocks = socksRepository.save(socksAlreadyInStock);
        logger.info("Параметры носков с id={} были изменены: цвет - с {} на {}, содержание хлопка - с {} на {}, " +
                        "количество - с {} на {}.",
                storedSocks.getId(), oldColor, storedSocks.getColor(),
                oldCottonPercentage, storedSocks.getCottonPercentage(),
                oldQuantity, storedSocks.getQuantity());
        return socksMapper.socksToSocksDto(storedSocks);
    }

    private Socks getSocksAlreadyInStockById(long id) {
        Optional<Socks> optionalSocks = socksRepository.findById(id);
        return optionalSocks.orElseThrow(
                () -> new NoSuchSocksInStockException("Socks with ID" + id + " not found"));
    }

    @Override
    @Transactional
    public List<SocksDto> addBatch(MultipartFile file) {
        List<SocksDto> incomingSocksDtoList = fileUploader.excelUpload(file);
        return incomingSocksDtoList.stream()
                .map(this::add)
                .collect(Collectors.toList());
    }


}
