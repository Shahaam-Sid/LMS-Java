package com.shahaam.lms.dto.book;

public sealed interface BookResponseDTO
    permits PhysicalBookResponseDTO, EBookResponseDTO, AudioBookResponseDTO {}