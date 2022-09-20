package bssm.db.bssmgit.domain.user.service;

import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.repository.UserRepository;
import bssm.db.bssmgit.domain.user.web.dto.response.BojAuthenticationResultResDto;
import bssm.db.bssmgit.domain.user.web.dto.response.BojUserResponseDto;
import bssm.db.bssmgit.domain.user.web.dto.response.RandomCodeResponseDto;
import bssm.db.bssmgit.global.config.security.SecurityUtil;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BojService {

    private final UserRepository userRepository;

    public List<BojUserResponseDto> findAllUserBojDesc(Pageable pageable) {
        return userRepository.findBojAll(pageable).stream()
                .map(BojUserResponseDto::new)
                .collect(Collectors.toList());
    }

    public BojAuthenticationResultResDto matchedCode() throws IOException {
        User user = userRepository.findByEmail(SecurityUtil.getLoginUserEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_LOGIN));

        String bojId = user.getBojId();
        String randomCode = user.getRandomCode();

        URL url = new URL("https://solved.ac/api/v3/user/show?handle=" + bojId);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        BufferedInputStream bufferedInputStream = new BufferedInputStream(urlConnection.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(bufferedInputStream, StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();
        String s;

        ArrayList<String> list = new ArrayList<>();
        while ((s = br.readLine()) != null) {
            sb.append(s);
        }

        StringTokenizer st = new StringTokenizer(sb.toString(), ",");
        int tokens = st.countTokens();
        for (int i = 0; i < tokens; i++) {
            String word = st.nextToken();
            if (word.contains("bio")) list.add(word);
        }

        ArrayList<String> message = new ArrayList<>();
        for (String bio : list) {
            StringTokenizer stt = new StringTokenizer(bio, ":");
            stt.nextToken();
            message.add(stt.nextToken());
        }

        String finalResult = null;
        for (String result : message) {
            StringTokenizer st2 = new StringTokenizer(result, "\"");
            finalResult = st2.nextToken();
        }

        if (finalResult == null) return new BojAuthenticationResultResDto(false);
        if (Objects.equals(finalResult, randomCode)) {
            return new BojAuthenticationResultResDto(true);
        } else {
            return new BojAuthenticationResultResDto(false);
        }

    }

    @Transactional
    public RandomCodeResponseDto getRandomCode(String id) {
        User user = userRepository.findByEmail(SecurityUtil.getLoginUserEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_LOGIN));

        String key = createKey();
        user.updateBojAuthId(id);
        user.updateRandomCode(key);

        return new RandomCodeResponseDto(key);
    }

    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) {
            int index = rnd.nextInt(3);

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    break;
            }
        }

        return key.toString();
    }

    @Transactional
    @Scheduled(cron = "0 4 * * * *") // 매일 새벽 4시
    public void updateUserBojInfo() throws IOException {
        final String bojId;

        userRepository.findAll()
                .stream().filter(u -> u.getGithubId() != null)
                .forEach(u -> {
                    URL url = null;
                    try {
                        url = new URL("https://solved.ac/api/v3/user/show" + "?handle=" + u.getBojId());
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }

                    HttpURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpURLConnection) url.openConnection();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        urlConnection.setRequestMethod("GET");
                    } catch (ProtocolException e) {
                        throw new RuntimeException(e);
                    }

                    try {
                        urlConnection.connect();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    BufferedInputStream bufferedInputStream = null;

                    try {
                        bufferedInputStream = new BufferedInputStream(urlConnection.getInputStream());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    BufferedReader br = new BufferedReader(new InputStreamReader(bufferedInputStream, StandardCharsets.UTF_8));

                    StringBuilder sb = new StringBuilder();
                    String s;

                    ArrayList<String> list = new ArrayList<>();
                    while (true) {
                        try {
                            if ((s = br.readLine()) == null) break;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        sb.append(s);
                    }

                    StringTokenizer st = new StringTokenizer(sb.toString(), ",");
                    int tokens = st.countTokens();

                    for (int i = 0; i < tokens; i++) {
                        String word = st.nextToken();
                        if (word.contains("solvedCount") ||
                                word.contains("exp") ||
                                word.contains("tier") ||
                                word.contains("maxStreak")) list.add(word);
                    }

                    ArrayList<String> properties = new ArrayList<>();
                    for (String property : list) {
                        StringTokenizer stt = new StringTokenizer(property, ":");
                        stt.nextToken();
                        properties.add(stt.nextToken());
                    }

                    ArrayList<String> result = new ArrayList<>();
                    for (String bojInfo : properties) {
                        StringTokenizer st2 = new StringTokenizer(bojInfo, "\"");
                        bojInfo = st2.nextToken();
                        result.add(bojInfo);
                    }

                    long solvedCount = Long.parseLong(result.get(0));
                    long tier = Long.parseLong(result.get(1));
                    long exp = Long.parseLong(result.get(2));
                    long maxStreak = Long.parseLong(result.get(3));

                    u.updateUserBojInfo(solvedCount, tier, exp, maxStreak);
                });
    }

}