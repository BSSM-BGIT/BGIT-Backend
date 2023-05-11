package bssm.db.bssmgit.domain.boj.domain.repository;

import bssm.db.bssmgit.domain.boj.web.dto.response.BojResponseDto;

import java.util.List;

public interface CustomBojRepository {

    List<BojResponseDto> findAllUserBojDesc();
}
