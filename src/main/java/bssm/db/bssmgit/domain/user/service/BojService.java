package bssm.db.bssmgit.domain.user.service;

import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.repository.UserRepository;
import bssm.db.bssmgit.domain.user.web.dto.response.*;
import bssm.db.bssmgit.global.config.security.SecurityUtil;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class BojService {

    private final UserRepository userRepository;

    @Transactional
    public BojAuthenticationResultResDto matchedCode() throws IOException {
        User user = userRepository.findByEmail(SecurityUtil.getLoginUserEmail()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_LOGIN));

        String bojId = user.getBojAuthId();
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
            user.updateBojId(user.getBojAuthId());
            // TODO Refactor
            updateUserBojInfo();
            return new BojAuthenticationResultResDto(true);
        } else {
            return new BojAuthenticationResultResDto(false);
        }

    }

    @Transactional
    public RandomCodeResponseDto getRandomCode(String id) {
        User user = userRepository.findByEmail(SecurityUtil.getLoginUserEmail()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_LOGIN));

        String key = createKey();
        user.updateBojAuthId(id);
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

//    @Scheduled(cron = "0 4 * * * ?") // 매일 새벽 4시
    public void updateUserBojInfo() {
        ArrayList<User> users = new ArrayList<>();

        userRepository.findAll().stream().filter(u -> u.getGithubId() != null).forEach(u -> {
            URL url;
            try {
                url = new URL("https://solved.ac/api/v3/user/show?handle=" + u.getBojId());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            HttpURLConnection urlConnection;

            try {
                url = new URL("https://solved.ac/api/v3/user/show?handle=" + u.getBojId());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
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
                if (word.contains("solvedCount") || word.contains("rating") ||
                        word.contains("tier") || word.contains("maxStreak") ||
                        word.contains("profileImageUrl"))
                    list.add(word);
            }

            System.out.println("list = " + list);

            ArrayList<String> properties = new ArrayList<>();
            String imgUrl = null;
            for (String property : list) {
                StringTokenizer stt = new StringTokenizer(property, ":");
                stt.nextToken();
                String stt2 = stt.nextToken();
                if (Objects.equals(stt2, "\"https")) {
                    imgUrl = stt.nextToken();
                    properties.add(stt2 + ":" + imgUrl);
                    System.out.println("properties = " + properties);
                } else {
                    properties.add(stt2);
                }
            }

            System.out.println("properties = " + properties);

            ArrayList<String> result = new ArrayList<>();
            for (String bojInfo : properties) {
                StringTokenizer st2 = new StringTokenizer(bojInfo, "\"");
                bojInfo = st2.nextToken();
                result.add(bojInfo);
            }

            System.out.println("result = " + result);

            StringBuilder sbb = new StringBuilder();
            String bojImg = sbb.append(result.get(0)).toString();
            long solvedCount = Long.parseLong(result.get(1));
            long tier = Long.parseLong(result.get(2));
            long rating = Long.parseLong(result.get(3));
            long maxStreak = Long.parseLong(result.get(4));

            System.out.println("rating = " + rating);

            u.updateUserBojInfo(solvedCount, tier, rating, maxStreak, bojImg);
            users.add(u);
        });

        userRepository.saveAll(users);
    }

}