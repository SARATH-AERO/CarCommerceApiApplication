package com.hcltech.car_commerce_api.service;

import com.hcltech.car_commerce_api.dto.SellerDto;
import com.hcltech.car_commerce_api.entity.Seller;
import com.hcltech.car_commerce_api.exception.SellerNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SellerService {
    @Autowired
    private com.hcltech.car_commerce_api.dao.service.SellerDao sellerDaoService;

    @Autowired
    private ModelMapper modelMapper;

    public Optional<SellerDto> getById(Integer id) {
        final Optional<Seller> optionalSeller=sellerDaoService.getById(id);
        if(optionalSeller.isEmpty())
        {
            throw new SellerNotFoundException(id);
        }
        final SellerDto result= toDto(optionalSeller.get());
        return Optional.of(result);
    }


    public SellerDto create(SellerDto sellerDto) {

        return  toDto(sellerDaoService.create(toEntity(sellerDto)));

    }

    private Seller toEntity(SellerDto sellerDto) {
        Seller s = modelMapper.map(sellerDto,Seller.class);
        return  s;

    }
    private  SellerDto  toDto(Seller seller) {
        SellerDto sellerDto =new SellerDto();
        modelMapper.map(sellerDto,seller);
        return sellerDto;

    }


}
