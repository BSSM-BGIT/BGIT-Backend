package bssm.db.bssmgit.domain.user.service;

import bssm.db.bssmgit.domain.user.domain.ImaginaryNumber;
import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.domain.type.Imaginary;
import bssm.db.bssmgit.domain.user.facade.ImaginaryNumberFacade;
import bssm.db.bssmgit.domain.user.facade.UserFacade;
import bssm.db.bssmgit.domain.user.web.dto.request.ImaginaryNumberRequestDto;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ImaginaryNumberService {

    private final UserFacade userFacade;
    private final ImaginaryNumberFacade imaginaryNumberFacade;

    public void reportUser(Long userId) {
        User user = userFacade.getCurrentUser();
        List<ImaginaryNumber> imaginaryNumbers = imaginaryNumberFacade.findByReportedUserId(userId);
        boolean isAlreadyReportedUserExists = imaginaryNumbers.stream()
                .anyMatch(imaginaryNumber -> Objects.equals(imaginaryNumber.getUser().getId(), user.getId()));

        if (isAlreadyReportedUserExists) {
            throw new CustomException(ErrorCode.DONT_REPORT_USER);
        }

        ImaginaryNumberRequestDto imaginaryNumberRequestDto = new ImaginaryNumberRequestDto(userId, user);
        imaginaryNumberFacade.save(imaginaryNumberRequestDto.toImaginaryNumber());
    }

    @Scheduled(cron = "0 0 0/1 * * *") // TODO: REFACTOR
    public void updateImaginaryNumberUser() {
        ArrayList<Long> userIds = new ArrayList<>();
        imaginaryNumberFacade.findAll()
                .forEach(imaginaryNumber -> userIds.add(imaginaryNumber.getReportedUserId()));

        ArrayList<User> users = new ArrayList<>();
        for (Long userId : userIds) {
            long count = userIds.stream()
                    .filter(id -> Objects.equals(id, userId))
                    .count();
            if (count > 2) {
                User user = userFacade.findById(userId);
                user.updateImaginary();
            }
        }

        userFacade.saveAll(users);
    }

    @Scheduled(cron = "0 0 0/1 * * *") // TODO: REFACTOR
    public void removeOldReport() {
        imaginaryNumberFacade.findAll()
                .forEach(imaginaryNumber -> {
                    long between = ChronoUnit.WEEKS.between(imaginaryNumber.getCreatedAt(), LocalDateTime.now());
                    if (between > 1) {
                        imaginaryNumberFacade.remove(imaginaryNumber);
                    }
                });
    }

    @Scheduled(cron = "0 0 0/1 * * *") // TODO: REFACTOR
    public void rollbackRealNumber() {
        ArrayList<Long> userIds = new ArrayList<>();
        imaginaryNumberFacade.findAll()
                .forEach(imaginaryNumber -> userIds.add(imaginaryNumber.getReportedUserId()));

        ArrayList<User> users = new ArrayList<>();
        for (Long userId : userIds) {
            long total = userIds.stream()
                    .filter(id -> Objects.equals(id, userId))
                    .count();
            if (total < 3) {
                User user = userFacade.findById(userId);
                if (user.getImaginary() == Imaginary.IMAGINARY_NUMBER) {
                    user.initImaginary();
                }
            }
        }

        userFacade.saveAll(users);
    }
}
