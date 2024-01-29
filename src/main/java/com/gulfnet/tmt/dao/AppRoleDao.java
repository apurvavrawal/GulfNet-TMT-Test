package com.gulfnet.tmt.dao;

import com.gulfnet.tmt.entity.sql.AppRole;
import com.gulfnet.tmt.repository.sql.AppRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppRoleDao {

    private final AppRoleRepository appRoleRepository;

    public List<AppRole> getAppRolesByCode(List<String> codes){
        return appRoleRepository.findAllByCodeIn(codes);
    }

}
