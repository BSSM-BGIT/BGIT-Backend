package bssm.db.bssmgit.domain.boj.service;

import bssm.db.bssmgit.domain.boj.domain.Boj;
import bssm.db.bssmgit.domain.boj.domain.repository.BojRepository;
import bssm.db.bssmgit.domain.user.facade.UserFacade;
import bssm.db.bssmgit.domain.user.web.dto.response.BojAuthenticationResultResDto;
import bssm.db.bssmgit.domain.user.web.dto.response.BojJsonResponseDto;
import bssm.db.bssmgit.domain.user.web.dto.response.RandomCodeResponseDto;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.util.Constants;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Random;

import static bssm.db.bssmgit.global.exception.ErrorCode.USER_NOT_FOUND;
import static bssm.db.bssmgit.global.util.Constants.BOJ_URL;
import static bssm.db.bssmgit.global.util.Constants.EVERY_5MINUTES;

@Slf4j
@RequiredArgsConstructor
@Service
public class BojService {

    private final BojRepository bojRepository;
    private final UserFacade userFacade;
    private final OkHttpClient okHttpClient;

    public static String createKey() {
        StringBuilder key = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) {
            int index = rnd.nextInt(3);

            switch (index) {
                case 0:
                    key.append((char) (rnd.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    break;
            }
        }

        return key.toString();
    }

    @Transactional
    public BojAuthenticationResultResDto matchedCode() throws IOException {
        Boj boj = userFacade.getCurrentUser().getBoj();
        Request tokenRequest = new Request.Builder()
                .url(Constants.BOJ_URL + boj.getBojAuthId())
                .build();
        Response bojResponse = okHttpClient.newCall(tokenRequest).execute();
        if (bojResponse.code() == 404) {
            throw new CustomException(USER_NOT_FOUND);
        }

        assert bojResponse.body() != null;
        String result = bojResponse.body().string();

        Gson gson = new Gson();
        BojJsonResponseDto info = gson.fromJson(result, BojJsonResponseDto.class);

        if (boj.getRandomCode().equals(info.getBio())) {
            boj.updateUserBojInfo(
                    boj.getBojAuthId(),
                    info.getSolvedCount(),
                    info.getTier(),
                    info.getRating(),
                    info.getMaxStreak(),
                    info.getProfileImageUrl(),
                    info.getBio()
            );
            bojRepository.save(boj);
        }

        return new BojAuthenticationResultResDto(boj);
    }

    @Transactional
    public RandomCodeResponseDto getRandomCode(String bojId) {
        Boj boj = userFacade.getCurrentUser().getBoj();
        String key = createKey();
        boj.updateBojAuthId(bojId);
        boj.updateRandomCode(key);
        bojRepository.save(boj);

        return new RandomCodeResponseDto(key);
    }

    @Scheduled(cron = EVERY_5MINUTES) // 매일 새벽 4시
    public void updateUserBojInfo() {

        bojRepository.findAll().stream().filter(b -> b.getBojId() != null)
                .forEach(b -> {
                            // Request
                            Request tokenRequest = new Request.Builder()
                                    .url(BOJ_URL + b.getBojAuthId())
                                    .build();

                            Response bojResponse;

                            try {
                                bojResponse = okHttpClient.newCall(tokenRequest).execute();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            if (bojResponse.code() == 404) {
                                throw new CustomException(USER_NOT_FOUND);
                            }

                            assert bojResponse.body() != null;
                            String result;

                            try {
                                result = bojResponse.body().string();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            Gson gson = new Gson();
                            BojJsonResponseDto info = gson.fromJson(result, BojJsonResponseDto.class);

                            b.updateUserBojInfo(
                                    b.getBojAuthId(),
                                    info.getSolvedCount(),
                                    info.getTier(),
                                    info.getRating(),
                                    info.getMaxStreak(),
                                    info.getProfileImageUrl(),
                                    info.getBio()
                            );
                        }
                );
    }

}
