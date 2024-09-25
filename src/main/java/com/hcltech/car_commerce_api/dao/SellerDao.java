package com.hcltech.car_commerce_api.dao;

import com.hcltech.car_commerce_api.dto.CarDto;
import com.hcltech.car_commerce_api.entity.Car;
import com.hcltech.car_commerce_api.entity.Seller;
import com.hcltech.car_commerce_api.exception.NotFoundException;
import com.hcltech.car_commerce_api.repository.CarRepository;
import com.hcltech.car_commerce_api.repository.SellerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SellerDao {

    private final SellerRepository sellerRepository;

    public SellerDao(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    public void createSeller(Seller seller){
        sellerRepository.save(seller);
    }

    public Optional<Seller> getSellerByEmail(String email) {
        return sellerRepository.findByEmail(email);
    }


    public int deleteSeller(String email){
        return sellerRepository.deleteByEmail(email);
    }

}
