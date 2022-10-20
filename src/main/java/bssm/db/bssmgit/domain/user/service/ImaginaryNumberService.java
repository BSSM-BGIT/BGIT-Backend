package bssm.db.bssmgit.domain.user.service;

import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.facade.UserFacade;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static java.util.Calendar.WEDNESDAY;

@RequiredArgsConstructor
@Service
public class ImaginaryNumberService {

    private final UserFacade userFacade;

    // 허수 판독 기능 추가
    // 매주 수요일마다 허수 판독 기능 실행
    // 랭킹에 올라와있는 사람들에게 투표 가능
    // 허수 투표 랭킹 1, 2, 3등 허수 칭호

    @Scheduled(cron = "0 * * * 3 ?") // 매주 수요일 허수 관련 정보 초기화
    public void init() {
        userFacade.findAll()
                .forEach(User::initImaginary);
    }

    @Scheduled(cron = "0 * * * 4 ?") // 매주 목요일 투표 탑 3 허수 지정
    public void votingImaginaryNumber() {
        List<User> users = userFacade.findAllUserByImaginaryNumberDesc();
        for (User user : users) {
            user.updateImaginary();
        }
    }

    @Transactional // 만약에 수요일이면 허수 투표 가능
    public void upImaginaryNumber(Long userId) {
        if (LocalDate.now().getDayOfWeek().getValue() == WEDNESDAY) {
            User user = userFacade.findById(userId);
            user.upImaginaryNumber();
        } else {
            throw new CustomException(ErrorCode.NOT_SUPPORTED_DAY);
        }
    }
}
