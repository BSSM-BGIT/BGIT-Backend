package bssm.db.bssmgit.domain.boj.service;

import bssm.db.bssmgit.domain.boj.domain.repository.CustomBojRepository;
import bssm.db.bssmgit.domain.boj.web.dto.response.BojResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BojInformationService {

    private final CustomBojRepository customBojRepository;

    public List<BojResponseDto> findAllUserBojDesc() {
        return customBojRepository.findAllUserBojDesc();
    }
}
