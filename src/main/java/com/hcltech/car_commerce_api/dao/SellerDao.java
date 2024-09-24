package com.hcltech.car_commerce_api.dao;

import com.hcltech.car_commerce_api.dto.CarDto;
import com.hcltech.car_commerce_api.entity.Car;
import com.hcltech.car_commerce_api.entity.Seller;
import com.hcltech.car_commerce_api.repository.CarRepository;
import com.hcltech.car_commerce_api.repository.SellerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SellerDao {

    private final SellerRepository sellerRepository;
    private final CarRepository carRepository;
    private final ModelMapper modelMapper;

    public SellerDao(SellerRepository sellerRepository, ModelMapper modelMapper,
                     CarRepository carRepository) {
        this.sellerRepository = sellerRepository;
        this.modelMapper = modelMapper;
        this.carRepository = carRepository;
    }

    public void createSeller(Seller seller){
        sellerRepository.save(seller);
    }

    public Optional<Seller> getSellerByEmail(String email) {
        return sellerRepository.findByEmail(email);
    }

    public String updateSeller(String email, CarDto carDto) throws Exception {
        Optional<Seller> existingSeller = sellerRepository.findByEmail(email);
        if(existingSeller.isEmpty())
            throw new Exception(email + " seller not present");
        modelMapper.map(carDto, existingSeller.get());
        sellerRepository.save(existingSeller.get());
        return email+" buyer details added successfully";
    }

    public int deleteSeller(String email){
        return sellerRepository.deleteByEmail(email);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }
}
