package pers.nefedov.socks.utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pers.nefedov.socks.dto.SocksDto;
import pers.nefedov.socks.exceptions.FileUploadErrorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileUploaderImpl implements FileUploader {
    @Override
    public List<SocksDto> excelUpload(MultipartFile file) {
        List<SocksDto> socksDtoList = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                SocksDto socksDto = new SocksDto();
                socksDto.setColor(row.getCell(0).getStringCellValue());
                socksDto.setCottonPercentage(row.getCell(1).getNumericCellValue());
                socksDto.setQuantity((int) row.getCell(2).getNumericCellValue());
                socksDtoList.add(socksDto);
            }
        } catch (IOException e) {
            throw new FileUploadErrorException("Error while uploading file " + file.getName());
        }
        return socksDtoList;
    }
}

