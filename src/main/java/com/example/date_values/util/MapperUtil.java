package com.example.date_values.util;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

import static org.modelmapper.convention.MatchingStrategies.STRICT;

public class MapperUtil {
    private static final ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(STRICT);
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    }

    public static  <D, T> Page<D> mapEntityPageIntoDtoPage(Page<T> entities, Class<D> dtoClass) {
        return entities.map(objectEntity -> modelMapper.map(objectEntity, dtoClass));
    }

    public static  <D, T> List<D> mapEntityListIntoDtoPage(List<T> entities, Class<D> dtoClass) {
        return entities.stream().map(objectEntity -> modelMapper.map(objectEntity, dtoClass)).collect(Collectors.toList());
    }

    public static <D, T> D map(final T source, Class<D> destination) {
        return modelMapper.map(source, destination);
    }

    public static <D, T> D mapValue(final T source, D destination) {
        modelMapper.map(source, destination);
        return destination;
    }
}
