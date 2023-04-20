package com.kytokvinily.vinyl;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class VinylDto {

    Long id;
    @NotBlank
    private String title;
    @Min(1900)
    private int year;
    @NotBlank
    private String author;

    public VinylDto(String title, int year, String author) {
        this.title = title;
        this.year = year;
        this.author = author;
    }
}
