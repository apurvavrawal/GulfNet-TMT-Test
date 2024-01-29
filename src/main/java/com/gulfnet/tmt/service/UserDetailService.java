package com.gulfnet.tmt.service;

import com.gulfnet.tmt.entity.nosql.UserDetail;
import com.gulfnet.tmt.model.response.ChatUserResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.repository.nosql.UserDetailRepository;
import com.gulfnet.tmt.util.enums.ChatStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailService {

    private final UserDetailRepository userDetailRepository;
    public void saveUser(UserDetail userDetail){
        userDetail.setChatStatus(ChatStatus.ONLINE);
        userDetailRepository.save(userDetail);
    }

    public void disconnect(UserDetail userDetail){
        var storedUserDetail = userDetailRepository.findById(userDetail.getFirstName()).orElse(null);
        if(storedUserDetail != null){
            storedUserDetail.setChatStatus(ChatStatus.OFFLINE);
            userDetailRepository.save(storedUserDetail);
        }
    }

    public ResponseDto<ChatUserResponse> findConnectedUsers(Pageable pageable){
        ChatUserResponse chatUserResponse = (ChatUserResponse) userDetailRepository.findAllByChatStatus(ChatStatus.ONLINE);
        return ResponseDto.<ChatUserResponse>builder()
                .data(List.of(chatUserResponse))
                .build();
    }

}
