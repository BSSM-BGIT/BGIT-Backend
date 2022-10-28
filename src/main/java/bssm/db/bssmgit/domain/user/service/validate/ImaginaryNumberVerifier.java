package bssm.db.bssmgit.domain.user.service.validate;

import bssm.db.bssmgit.domain.user.domain.ImaginaryNumber;
import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;

import java.util.List;
import java.util.Objects;

public class ImaginaryNumberVerifier {

    public static void isAlreadyExistsReportedUser(User user, List<ImaginaryNumber> imaginaryNumbers) {
        boolean isAlreadyExistsReportedUser = imaginaryNumbers.stream()
                .anyMatch(imaginaryNumber -> Objects.equals(imaginaryNumber.getUser().getId(), user.getId()));
        if (isAlreadyExistsReportedUser) {
            throw new CustomException(ErrorCode.DONT_REPORT_USER);
        }
    }
}
