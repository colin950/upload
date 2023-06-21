package com.example.calculate.component;

import com.example.calculate.base.service.CalculateService;
import com.example.calculate.base.service.RedisService;
import com.example.calculate.repository.CalculateInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class BatchScheduler {
    private final CalculateService calculateService;

    private final RedisService redisService;

    private final String unsettledFlagKey = "unsettledNotify:batch";

//    @Value("${applcable}")
    private Boolean unsettledApplicable = false;

    // 1분 마다 실행
    // 이전 작업이 진행중이라면 완료 후에 처리하기 위해 설정
    @Scheduled(fixedDelay = 1000 * 60)
    public void unsettledNotify() {
        /**
         * multi node로 구성되었을 경우 각자의 노드에서 스케줄이 돌게 됨으로
         * redis에 특정 키를 만들어 한개의 노드에서만 동작 하도록 분산락 처리시에 사용
         */
         redisService.setLock(unsettledFlagKey);
        // 스케줄러 중단을 위해 사용 하는 flag
        if (unsettledApplicable) {
            log.info("[unsettledNotify] start");
            calculateService.unsettledNotify();
        }
    }
}
