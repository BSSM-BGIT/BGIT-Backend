package bssm.db.bssmgit.domain.user.service;

import bssm.db.bssmgit.domain.user.domain.ImaginaryNumber;
import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.facade.ImaginaryNumberFacade;
import bssm.db.bssmgit.domain.user.facade.UserFacade;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ImaginaryNumberService {

    private final UserFacade userFacade;
    private final ImaginaryNumberFacade imaginaryNumberFacade;

    //=============================/ 완료
    // 허수 판독 기능 추가
    // 매주 수요일마다 허수 판독 기능 실행
    // 랭킹에 올라와있는 사람들에게 투표 가능
    //=============================/
    // 한사람당 3명씩 투표 가능
    // 중복 투표 불가능
    // 허수 투표 랭킹 1, 2, 3등 허수 칭호

    @Scheduled(cron = "0 * * * 3 ?") // 매주 수요일 허수 관련 정보 초기화
    public void init() {
        imaginaryNumberFacade.removeAll();
    }

    // TODO : top 3가 중복된 값이면 어떻게 처리할것인가
//    @Scheduled(cron = "0 * * * 4 ?") // 매주 목요일 투표 탑 3 허수 지정
//    public void designationImaginaryNumber() {
//        List<User> users = userFacade.findAllUserByImaginaryNumberDesc();
//        users.forEach(User::updateImaginary);
//        userFacade.saveAll(users);
//    }

    @Transactional // 만약에 수요일이면 허수 투표 가능
    public void votingImaginaryNumber(Long userId) { // WEDNESDAY
        if (LocalDate.now().getDayOfWeek().name().equals("SATURDAY")) {
            Optional<ImaginaryNumber> optionalImaginaryNumber = imaginaryNumberFacade.findAll()
                    .stream()
                    .filter(imaginary -> Objects.equals(imaginary.getUserId(), userId))
                    .findFirst();

            if (optionalImaginaryNumber.isPresent()) {
                optionalImaginaryNumber.get().increaseVoting();
            } else {
                ImaginaryNumber imaginaryNumber = imaginaryNumberFacade.save(new ImaginaryNumber(userId));
                imaginaryNumber.initVotingNumber();
                imaginaryNumber.increaseVoting();
            }
        } else {
            throw new CustomException(ErrorCode.NOT_SUPPORTED_DAY);
        }
    }
}
