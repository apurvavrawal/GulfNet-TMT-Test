package com.gulfnet.tmt.mapper;

import com.gulfnet.tmt.entity.sql.UserRole;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserRoleMapper extends AbstractConverter<List<UserRole>, List<String>> {

    @Override
    protected List<String> convert(List<UserRole> source) {
        return source.stream().map(userRole -> userRole.getRole().getName()).collect(Collectors.toList());
    }

}



