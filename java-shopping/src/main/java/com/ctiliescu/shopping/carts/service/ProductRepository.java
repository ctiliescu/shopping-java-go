package com.ctiliescu.shopping.carts.service;

import com.ctiliescu.shopping.carts.model.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.stock = :stock WHERE p.id = :id")
    void setProductStock(@Param("stock") Integer stock, @Param("id") Integer id);
}
