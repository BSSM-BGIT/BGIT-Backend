package bssm.db.bssmgit.domain.user.service;

import bssm.db.bssmgit.domain.user.domain.ImaginaryNumber;
import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.facade.ImaginaryNumberFacade;
import bssm.db.bssmgit.domain.user.facade.UserFacade;
import bssm.db.bssmgit.domain.user.service.validate.ImaginaryNumberVerifier;
import bssm.db.bssmgit.domain.user.web.dto.request.ImaginaryNumberRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static bssm.db.bssmgit.domain.user.domain.type.Imaginary.IMAGINARY_NUMBER;
import static bssm.db.bssmgit.global.util.Constants.EVERY_50MINUTES;

@RequiredArgsConstructor
@Service
public class ImaginaryNumberService {

    private final UserFacade userFacade;
    private final ImaginaryNumberFacade imaginaryNumberFacade;

    public void reportUser(Long userId) {
        User user = userFacade.getCurrentUser();
        List<ImaginaryNumber> imaginaryNumbers = imaginaryNumberFacade.findByReportedUserId(userId);
        ImaginaryNumberVerifier.isAlreadyExistsReportedUser(user, imaginaryNumbers);

        ImaginaryNumberRequestDto imaginaryNumberRequestDto = new ImaginaryNumberRequestDto(userId, user);
        imaginaryNumberFacade.save(imaginaryNumberRequestDto.toImaginaryNumber());
    }

    @Scheduled(cron = EVERY_50MINUTES) // TODO: REFACTOR
    public void updateImaginaryNumberUser() {
        ArrayList<Long> userIds = new ArrayList<>();
        imaginaryNumberFacade.findAll()
                .forEach(imaginaryNumber -> userIds.add(imaginaryNumber.getReportedUserId()));

        ArrayList<User> users = new ArrayList<>();
        for (Long userId : userIds) {
            long count = userIds.stream()
                    .filter(id -> Objects.equals(id, userId))
                    .count();
            if (count > 4) {
                User user = userFacade.findById(userId);
                user.updateImaginary();
            }
        }

        userFacade.saveAll(users);
    }

    @Scheduled(cron = EVERY_50MINUTES) // TODO: REFACTOR
    private void removeOldReport() {
        imaginaryNumberFacade.findAll()
                .forEach(imaginaryNumber -> {
                    if (isAfter3Days(imaginaryNumber)) {
                        imaginaryNumberFacade.remove(imaginaryNumber);
                    }
                });
    }

    // 3일이 지난 신고 기록인가
    private boolean isAfter3Days(ImaginaryNumber imaginaryNumber) {
        long between = ChronoUnit.DAYS.between(imaginaryNumber.getCreatedAt(), LocalDateTime.now());
        return between > 3;
    }

    @Scheduled(cron = EVERY_50MINUTES) // TODO: REFACTOR
    public void rollbackImaginaryNumberToRealNumber() {
        initImaginaryReportsLessThan5();
        dontHaveImaginaryNumber();
    }

    private void initImaginaryReportsLessThan5() {
        // 신고 당한 유저들의 id 저장
        List<Long> userIds = new ArrayList<>();
        imaginaryNumberFacade.findAll()
                .forEach(imaginaryNumber -> userIds.add(imaginaryNumber.getReportedUserId()));

        // 신고 횟수가 5 미만인 유저들 초기화 후 저장
        List<User> users = new ArrayList<>();
        for (Long userId : userIds) {
            if (reportsLessThan5(userIds, userId)) {
                User user = userFacade.findById(userId);
                if (user.getImaginary() == IMAGINARY_NUMBER) {
                    user.initImaginary();
                }
            }
        }
        userFacade.saveAll(users);
    }

    private boolean reportsLessThan5(List<Long> userIds, Long userId) {
        long total = userIds.stream()
                .filter(id -> Objects.equals(id, userId))
                .count();
        return total < 5;
    }

    private void dontHaveImaginaryNumber() {
        List<User> users = userFacade.findUserByImaginaryUser();
        List<User> userList = new ArrayList<>();
        for (User user : users) {
            if (!notExistsRecordReportButAdaptImaginaryNumberUser(user)) {
                user.initImaginary();
                userList.add(user);
            }
        }
        userFacade.saveAll(userList);
    }

    // 허수 테이블에 신고받은 기록은 없지만(신고 적용 기간이 지나서 다 사라진 경우) 유저 테이블에서는 허수로 적용되어있는가
    private boolean notExistsRecordReportButAdaptImaginaryNumberUser(User user) {
        return imaginaryNumberFacade.findAll()
                .stream()
                .anyMatch(imaginaryNumber -> Objects.equals(imaginaryNumber.getReportedUserId(), user.getId()));
    }
}
