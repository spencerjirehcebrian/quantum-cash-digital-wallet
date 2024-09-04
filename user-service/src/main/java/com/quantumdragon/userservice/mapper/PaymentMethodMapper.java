package com.quantumdragon.userservice.mapper;

import org.mapstruct.Mapper;

import com.quantumdragon.userservice.dto.PaymentMethodDto;
import com.quantumdragon.userservice.entity.PaymentMethod;

@Mapper(componentModel = "spring")
public interface PaymentMethodMapper {

    PaymentMethod toEntity(PaymentMethodDto paymentMethodDTO);

    PaymentMethodDto toDto(PaymentMethod paymentMethod);
}
