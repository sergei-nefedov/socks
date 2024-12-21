package pers.nefedov.socks.utils;

import org.springframework.web.multipart.MultipartFile;
import pers.nefedov.socks.dto.SocksDto;

import java.util.List;

public interface FileUploader {
    List<SocksDto> excelUpload(MultipartFile file);
}
