package com.shahaam.lms.dto.book;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PhysicalBookRequestDTO.class,  name = "PHYSICAL"),
    @JsonSubTypes.Type(value = EBookRequestDTO.class,         name = "EBOOK"),
    @JsonSubTypes.Type(value = AudioBookRequestDTO.class,     name = "AUDIOBOOK")
})

public sealed interface BookRequestDTO 
    permits EBookRequestDTO, PhysicalBookRequestDTO, AudioBookRequestDTO {}