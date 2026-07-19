package com.aymen.saas.services;

import com.aymen.saas.common.PageResponse;
import com.aymen.saas.requests.StockMvtRequest;
import com.aymen.saas.responses.StockMvtResponse;

public interface StockMvtService extends BasicService<StockMvtRequest, StockMvtResponse> {
    PageResponse<StockMvtResponse> findAllByProductId(final String productId, final int page, final int size);
}
