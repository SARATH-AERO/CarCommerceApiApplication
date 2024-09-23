package com.hcltech.car_commerce_api.dao;

import com.hcltech.car_commerce_api.dto.SellerDto;
import com.hcltech.car_commerce_api.entity.Cars;
import com.hcltech.car_commerce_api.entity.Seller;
import com.hcltech.car_commerce_api.repository.SellerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SellerDao {

    private final SellerRepository sellerRepository;
    private final com.hcltech.car_commerce_api.repository.CarsRepository carsRepository;
    private final ModelMapper modelMapper;

    public SellerDao(SellerRepository sellerRepository, ModelMapper modelMapper,
                     com.hcltech.car_commerce_api.repository.CarsRepository carsRepository) {
        this.sellerRepository = sellerRepository;
        this.modelMapper = modelMapper;
        this.carsRepository =carsRepository;
    }

    public void createSeller(Seller seller){
        sellerRepository.save(seller);
    }

    public Optional<Seller> getSellerByEmail(String email) {
        return sellerRepository.findByEmail(email);
    }

    public String updateSeller(String email, SellerDto updateSellerDto) throws Exception {
        Optional<Seller> existingSeller = sellerRepository.findByEmail(email);

        if(existingSeller.isEmpty())
            throw new Exception(email + " seller not present");
        Seller modifiedSeller = existingSeller.get();
        modelMapper.map(updateSellerDto, modifiedSeller);
        sellerRepository.save(modifiedSeller);
        return modifiedSeller.getEmail()+" buyer details added successfully";
    }

    public int deleteSeller(String email){
        return sellerRepository.deleteByEmail(email);
    }

    public List<Cars> getAllCars() {
        return carsRepository.findAll();
    }
}
