package com.aymen.saas.services.impl;

import com.aymen.saas.common.PageResponse;
import com.aymen.saas.entities.Product;
import com.aymen.saas.entities.StockMvt;
import com.aymen.saas.mappers.StockMvtMapper;
import com.aymen.saas.repositories.ProductRepository;
import com.aymen.saas.repositories.StockMvtRepository;
import com.aymen.saas.requests.StockMvtRequest;
import com.aymen.saas.responses.StockMvtResponse;
import com.aymen.saas.services.StockMvtService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockMvtServiceImpl implements StockMvtService {

    private final StockMvtRepository stockMvtRepository;
    private final StockMvtMapper stockMvtMapper;
    private final ProductRepository productRepository;

    @Override
    public void create(StockMvtRequest request) {
        // check if product exist by id
        checkIfProductExistsById(request.getProductId());

        final StockMvt entity = this.stockMvtMapper.toEntity(request);
        entity.setDateMvt(LocalDate.now());
        this.stockMvtRepository.save(entity);

    }

    @Override
    public void update(String id, StockMvtRequest request) {

        // check if stockmvt exist
        Optional<StockMvt> stockMvt = stockMvtRepository.findById(id);
        if(stockMvt.isEmpty()){
            log.debug("StockMvt does not exist");
            throw  new RuntimeException("stock mvt does not exist");
        }

        // check if product exist
        checkIfProductExistsById(request.getProductId());

        final StockMvt stockMvtToUpdate = this.stockMvtMapper.toEntity(request);
        stockMvtToUpdate.setDateMvt(LocalDate.now());
        stockMvtToUpdate.setId(id);
        this.stockMvtRepository.save(stockMvtToUpdate);

    }

    @Override
    public StockMvtResponse findById(String id) {
        return stockMvtRepository.findById(id)
                .map(stockMvtMapper::toResponse)
                .orElseThrow(()-> new RuntimeException("Stock does not exist"));
    }

    @Override
    public List<StockMvtResponse> findAll() {
        return List.of();
    }

    @Override
    public PageResponse<StockMvtResponse> findAll(int page, int size) {
        final PageRequest pageRequest = PageRequest.of(page, size);
        final Page<StockMvt> stockMvts = this.stockMvtRepository.findAll(pageRequest);
        final Page<StockMvtResponse> stockMvtResponses = stockMvts.map(this.stockMvtMapper::toResponse);
        return PageResponse.of(stockMvtResponses);
    }



    @Override
    public void delete(String id) {
        final StockMvt stockMvt =
                this.stockMvtRepository.findById(id)
                        .orElseThrow(()->new RuntimeException("stockMvt does not exist"));
        stockMvtRepository.delete(stockMvt);
    }

    @Override
    public PageResponse<StockMvtResponse> findAllByProductId(final String productId, final int page, final int size) {
        final PageRequest pageRequest = PageRequest.of(page, size);
        final Page<StockMvt> stockMvts = this.stockMvtRepository.findAllByProductId(productId, pageRequest);
        final Page<StockMvtResponse> stockMvtResponses = stockMvts.map(this.stockMvtMapper::toResponse);
        return PageResponse.of(stockMvtResponses);
    }


    private void checkIfProductExistsById( String productId) {
        final Optional<Product> product = this.productRepository.findById(productId);
        if(product.isEmpty()){
            log.debug("Product does not exist");
            throw new RuntimeException("Product does not exist");
        }
    }


}
