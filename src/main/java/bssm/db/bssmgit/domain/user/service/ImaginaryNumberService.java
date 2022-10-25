package bssm.db.bssmgit.domain.user.service;

import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.facade.ImaginaryNumberFacade;
import bssm.db.bssmgit.domain.user.facade.UserFacade;
import bssm.db.bssmgit.domain.user.web.dto.request.ImaginaryNumberRequestDto;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ImaginaryNumberService {

    private final UserFacade userFacade;
    private final ImaginaryNumberFacade imaginaryNumberFacade;

    // 1. 한 사람당 한번씩 신고할 수 있음(신고 횟수는 무제한) -> done
    // 2. 2주 이내에 받은 신고가 3개 이상이면 허수 등록 -> done
    // 3. 등록된 신고는 2주마다 사라짐
    // 4. 2주가 지나면 다시 같은사람 신고 가능
    public void reportUser(Long userId) {
        User user = userFacade.getCurrentUser();
        boolean isAlreadyReportedUserExists = imaginaryNumberFacade.findAll()
                .stream()
                .anyMatch(imaginaryNumber -> imaginaryNumber.getUser().equals(user));

        if (isAlreadyReportedUserExists) {
            throw new CustomException(ErrorCode.DONT_REPORT_USER);
        }

        ImaginaryNumberRequestDto imaginaryNumberRequestDto = new ImaginaryNumberRequestDto(userId, user);
        imaginaryNumberFacade.save(imaginaryNumberRequestDto.toImaginaryNumber());
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void updateImaginaryNumberUser() {
        ArrayList<Long> userIds = new ArrayList<>();
        imaginaryNumberFacade.findAll()
                .forEach(imaginaryNumber -> userIds.add(imaginaryNumber.getReportedUserId()));

        ArrayList<User> users = new ArrayList<>();
        int cnt;
        for (Long userId : userIds) {
            cnt = 0;
            for (Long id : userIds) {
                if (Objects.equals(userId, id)) cnt++;
                if (cnt >= 3) {
                    User user = userFacade.findById(id);
                    user.updateImaginary();
                    users.add(user);
                }
            }
        }

        userFacade.saveAll(users);
    }
}
