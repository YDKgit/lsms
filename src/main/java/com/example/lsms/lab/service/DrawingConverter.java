package com.example.lsms.lab.service;

import org.springframework.web.multipart.MultipartFile;

public interface DrawingConverter {

    String convertDxfToPng(MultipartFile dxfFile, Long labId);
}
