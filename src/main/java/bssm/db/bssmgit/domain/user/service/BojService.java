package bssm.db.bssmgit.domain.user.service;

import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.repository.UserRepository;
import bssm.db.bssmgit.domain.user.web.dto.response.*;
import bssm.db.bssmgit.global.config.security.SecurityUtil;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class BojService {

    private final UserRepository userRepository;
    private final static String URL = "https://solved.ac/api/v3/user/show?handle=";
    private final OkHttpClient okHttpClient;

    @Transactional
    public BojAuthenticationResultResDto matchedCode() throws IOException {
        User user = userRepository.findByEmail(SecurityUtil.getLoginUserEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_LOGIN));

        Request tokenRequest = new Request.Builder()
                .url(URL + user.getBojAuthId())
                .build();
        Response bojResponse = okHttpClient.newCall(tokenRequest).execute();
        if (bojResponse.code() == 404) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        assert bojResponse.body() != null;
        String result = bojResponse.body().string();

        Gson gson = new Gson();
        BojJsonResponseDto info = gson.fromJson(result, BojJsonResponseDto.class);

        if (user.getRandomCode().equals(info.getBio())) {
            user.updateUserBojInfo(
                    user.getBojAuthId(),
                    info.getSolvedCount(),
                    info.getTier(),
                    info.getRating(),
                    info.getMaxStreak(),
                    info.getProfileImageUrl(),
                    info.getBio()
            );
        }

        return new BojAuthenticationResultResDto(user);
    }

    @Transactional
    public RandomCodeResponseDto getRandomCode(String bojId) {
        User user = userRepository.findByEmail(SecurityUtil.getLoginUserEmail()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_LOGIN));

        String key = createKey();
        user.updateBojAuthId(bojId);
        user.updateRandomCode(key);

        return new RandomCodeResponseDto(key);
    }

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

    @Scheduled(cron = "0 4 * * * ?") // 매일 새벽 4시
    public void updateUserBojInfo() throws IOException {
        ArrayList<User> users = new ArrayList<>();

        userRepository.findAll().stream().filter(u -> u.getBojId() != null)
                .forEach(u -> {
                            // Request
                            Request tokenRequest = new Request.Builder()
                                    .url(URL + u.getBojAuthId())
                                    .build();
                            Response bojResponse = null;
                            try {
                                bojResponse = okHttpClient.newCall(tokenRequest).execute();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            if (bojResponse.code() == 404) {
                                throw new CustomException(ErrorCode.USER_NOT_FOUND);
                            }

                            assert bojResponse.body() != null;
                            String result = null;
                            try {
                                result = bojResponse.body().string();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            Gson gson = new Gson();
                            BojJsonResponseDto info = gson.fromJson(result, BojJsonResponseDto.class);

                            u.updateUserBojInfo(
                                    u.getBojAuthId(),
                                    info.getSolvedCount(),
                                    info.getTier(),
                                    info.getRating(),
                                    info.getMaxStreak(),
                                    info.getProfileImageUrl(),
                                    info.getBio()
                            );

                            users.add(u);
                        }
                );

        userRepository.saveAll(users);

    }

}