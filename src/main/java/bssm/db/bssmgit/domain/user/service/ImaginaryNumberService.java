package bssm.db.bssmgit.domain.user.service;

import bssm.db.bssmgit.domain.user.facade.ImaginaryNumberFacade;
import bssm.db.bssmgit.domain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ImaginaryNumberService {

    private final UserFacade userFacade;
    private final ImaginaryNumberFacade imaginaryNumberFacade;

    // 1. 한 사람당 한번씩 신고할 수 있음(신고 횟수는 무제한)
    // 1-1. ImaginaryNumber에 신고 당한 유저 아이디와 신고한 유저의 아이디가 있어야함
    // 1-1-1. voting_nunber 컬럼 삭제 & 신고한 유저 아이디 컬럼 추가
    // 2. 2주 이내에 받은 신고가 3개 이상이면 허수 등록
    // 3. 등록된 신고는 2주마다 사라짐
    // 4. 2주가 지나면 다시 같은사람 신고 가능


}
