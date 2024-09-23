package com.hcltech.car_commerce_api.service;

import com.hcltech.car_commerce_api.configuration.ModelMapperConfig;
import com.hcltech.car_commerce_api.dao.service.SellerDaoService;
import com.hcltech.car_commerce_api.dto.SellerDto;
import com.hcltech.car_commerce_api.entity.Seller;
import com.hcltech.car_commerce_api.exception.SellerNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SellerService {
    @Autowired
    private SellerDaoService sellerDaoService;

    @Autowired
    private ModelMapper modelMapper;

//    public List<SellerDto> getAll() {
//        final List<Seller> seller= sellerDaoService.getAll();
//      //  final List<SellerDto> response=toDto(seller);
//        return response;
//
//       // return Collections.singletonList(toDto(sellerDaoService.getAll()));
//    }

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

//        final Seller seller=toEntity(sellerDto);
//        final Optional<Seller> result= sellerDaoService.create(seller);
//        if(result.isEmpty())
//        {
//            return Optional.empty();
//        }
//        return Optional.of(toDto(result.get()));
    }


//    private List<SellerDto> toDtoSeller(List<Seller> seller) {
//        return seller.stream()
//                .map(this::toDto)
//                .collect(Collectors.toList());
//    }
//    private SellerDto toDto(Seller seller) {
//
//        return SellerDto.builder()
//                .email(seller.getEmail())
//                .firstName(seller.getFirstName())
//                .lastName(seller.getLastName())
//                .phoneNumber(seller.getPhoneNumber())
//                .address(seller.getAddress())
//                .city(seller.getCity())
//                .postalCode(seller.getPostalCode())
//                .build();
//    }

    // Dto -> Dao
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
