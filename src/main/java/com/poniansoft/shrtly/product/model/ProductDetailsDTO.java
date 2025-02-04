package com.poniansoft.shrtly.product.model;

import com.poniansoft.shrtly.click.model.HistoricalClickDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductDetailsDTO {
    private Long productId;
    private String productName;
    private String productUrl;
    private String imageUrl;
    private String currentSlug;
    private String currentShortCode;
    private String currentLongUrl;
    private int totalClicks;
    private List<HistoricalClickDTO> historicalClicks;
}
