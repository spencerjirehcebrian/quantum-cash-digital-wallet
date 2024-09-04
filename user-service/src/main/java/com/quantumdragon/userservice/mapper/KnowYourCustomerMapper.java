package com.quantumdragon.userservice.mapper;

import com.quantumdragon.userservice.dto.KnowYourCustomerDto;
import com.quantumdragon.userservice.entity.KnowYourCustomer;

import java.util.Base64;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface KnowYourCustomerMapper {
    KnowYourCustomerMapper INSTANCE = Mappers.getMapper(KnowYourCustomerMapper.class);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "addressProofDocument", expression = "java(decodeBase64(dto.getAddressProofDocument()))")
    @Mapping(target = "idProofDocument", expression = "java(decodeBase64(dto.getIdProofDocument()))")
    KnowYourCustomer toEntity(KnowYourCustomerDto dto);

    @Mapping(target = "addressProofDocument", expression = "java(encodeBase64(entity.getAddressProofDocument()))")
    @Mapping(target = "idProofDocument", expression = "java(encodeBase64(entity.getIdProofDocument()))")
    KnowYourCustomerDto toDto(KnowYourCustomer entity);

    default byte[] decodeBase64(String base64String) {
        if (base64String == null) {
            return null;
        }
        String[] parts = base64String.split(",");
        return Base64.getDecoder().decode(parts.length > 1 ? parts[1] : parts[0]);
    }

    default String encodeBase64(byte[] data) {
        if (data == null) {
            return null;
        }
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(data);
    }
}