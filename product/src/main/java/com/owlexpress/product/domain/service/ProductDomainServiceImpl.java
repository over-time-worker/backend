package com.owlexpress.product.domain.service;

import com.owlexpress.product.common.exceptions.ProductException;
import com.owlexpress.product.domain.entity.Product;
import com.owlexpress.product.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductDomainServiceImpl implements ProductDomainService {
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void delete(UUID productsId) {
        Product product = getProduct(productsId);

        product.softDeleteData(1L);
        product.getHubInfo().forEach(
                hubInfo -> hubInfo.softDeleteData(1L)
        );

    }


    private Product getProduct(UUID productsId) {
        return productRepository.findById(productsId).orElseThrow(
                () -> new ProductException.ProductNotFoundException("찾는 상품이 없습니다.")
        );
    }
}
