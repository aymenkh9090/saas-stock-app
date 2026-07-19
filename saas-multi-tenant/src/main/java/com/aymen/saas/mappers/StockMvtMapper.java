package com.aymen.saas.mappers;

import com.aymen.saas.entities.Product;
import com.aymen.saas.entities.StockMvt;
import com.aymen.saas.requests.StockMvtRequest;
import com.aymen.saas.responses.StockMvtResponse;
import org.springframework.stereotype.Component;

@Component
public class StockMvtMapper {

    // Dto ---> Entity
    public StockMvt toEntity(final StockMvtRequest request) {

        return StockMvt.builder()
                .dateMvt(request.getDateMvt())
                .typeMvt(request.getTypeMvt())
                .quantity(request.getQuantity())
                .comment(request.getComment())
                .product(Product.builder()
                        .id(request.getProductId())
                        .build())
                .deleted(false)
                .build();

    }

    public StockMvtResponse toResponse(final StockMvt entity) {
        return StockMvtResponse.builder()
                .id(entity.getId())
                .dateMvt(entity.getDateMvt())
                .comment(entity.getComment())
                .typeMvt(entity.getTypeMvt())
                .quantity(entity.getQuantity())
                .build();
    }


}
