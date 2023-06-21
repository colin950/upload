package com.example.calculate;


import com.example.calculate.base.dto.request.CalculateDetailDto;
import com.example.calculate.base.dto.request.CalculateRequestDto;
import com.example.calculate.base.service.CalculateService;
import com.example.calculate.security.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class AcceptanceTest {
    @Autowired
    public CalculateService calculateService;

    public UserInfo setUpUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(1L);
        userInfo.setRoomId("asdfasd123f");
        return userInfo;
    }
    public CalculateRequestDto setUpRequestDto() {
        CalculateRequestDto request = new CalculateRequestDto();
        request.setTitle("title1");
        request.setMessage("message1");
        request.setAmount("1000");
        request.setNotifiedAt(LocalDateTime.now().minusMinutes(2));

        List<CalculateDetailDto> detailList = new ArrayList<>();

        CalculateDetailDto detail = new CalculateDetailDto();
        detail.setUserId(2L);
        detail.setAmount("500");
        detailList.add(detail);

        CalculateDetailDto detail2 = new CalculateDetailDto();
        detail2.setUserId(3L);
        detail2.setAmount("500");
        detailList.add(detail2);

        request.setData(detailList);

        return request;
    }

}
