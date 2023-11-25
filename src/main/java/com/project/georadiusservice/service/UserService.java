package com.project.georadiusservice.service;

import com.project.georadiusservice.bo.UserClientRequestBO;
import com.project.georadiusservice.dto.AdvisorDetailDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    List<AdvisorDetailDTO> getNearbyUserList(UserClientRequestBO userClientRequestBO, Integer offset);

}