package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.entity.nosql.UserDetail;
import com.gulfnet.tmt.model.response.ChatUserResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserDetailController {
    private final UserDetailService userDetailService;


    // This is used to subscribe user to front-end.
    @MessageMapping("/user.addUser")
    @SendTo("/user/topic")
    public UserDetail addUserDetail(@Payload UserDetail userDetail){
        userDetailService.saveUser(userDetail);
        return userDetail;
    }

    // This is used to provide disconnected user's details
    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/topic")
    public UserDetail disconnect(@Payload UserDetail userDetail){
        userDetailService.disconnect(userDetail);
        return userDetail;
    }

    @GetMapping("/users")
    public ResponseDto<ChatUserResponse> findConnectedUsers(Pageable pageable){
        return userDetailService.findConnectedUsers(pageable);
    }

}
