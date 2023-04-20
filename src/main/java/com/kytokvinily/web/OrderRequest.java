package com.kytokvinily.web;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderRequest {

    @NotNull(message = "Vinyl id must be defined.")
    @Min(1)
    Long vinylId;

    @NotNull
    @Min(value = 1, message = "You must order at least 1 item.")
    @Max(value = 5, message = "You cannot order more than 5 items.")
    Integer quantity;
}
