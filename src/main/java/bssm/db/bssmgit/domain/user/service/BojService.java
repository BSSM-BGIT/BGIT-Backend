package bssm.db.bssmgit.domain.user.service;

import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.repository.UserRepository;
import bssm.db.bssmgit.domain.user.web.dto.response.*;
import bssm.db.bssmgit.global.config.security.SecurityUtil;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
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
            if (word.contains("bio")) {
                list.add(word);
                System.out.println("word = " + word);
            }
        }

        ArrayList<String> message = new ArrayList<>();
        for (String bio : list) {
            StringTokenizer stt = new StringTokenizer(bio, ":");
            stt.nextToken();
            message.add(stt.nextToken());
        }

        System.out.println("message = " + message);

        String finalResult = null;
        for (String result : message) {
            StringTokenizer st2 = new StringTokenizer(result, "\"");
            finalResult = st2.nextToken();
        }

        System.out.println("finalResult = " + finalResult);

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
    public void updateUserBojInfo() {
        ArrayList<User> users = new ArrayList<>();

        userRepository.findAll().stream().filter(u -> u.getBojAuthId() != null).forEach(u -> {
            URL url;
            try {
                String bojAuthId = u.getBojAuthId();
                String bojUrl = "https://solved.ac/api/v3/user/show?handle=";
                url = new URL(bojUrl + bojAuthId);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            HttpURLConnection urlConnection;

            try {
                url = new URL("https://solved.ac/api/v3/user/show?handle=" + u.getBojAuthId());
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

            HashMap<String, String> properties = new HashMap<>();
            String imgUrl = null;

            String bojImg = null;
            long solvedCount = 0;
            long tier = 0;
            long rating = 0;
            long maxStreak = 0;
            int solvedCountCnt = 0;
            int ratingCnt = 0;
            String solvedCountStorage = null;
            String ratingStorage = null;

            for (String property : list) {
                StringTokenizer stt = new StringTokenizer(property, ":");
                String firstToken = stt.nextToken();
                String secondToken = stt.nextToken();
                String thirdToken = "";

                switch (firstToken) {
                    case "\"tier\"":
                        properties.put("tier", secondToken);
                        break;
                    case "\"maxStreak\"":
                        properties.put("maxStreak", secondToken);
                        break;
                    case "\"solvedCount\"":
                        solvedCountCnt++;
                        if (solvedCountCnt == 2) properties.put("solvedCount", secondToken);
                        else solvedCountStorage = secondToken;
                        break;
                    case "\"rating\"":
                        ratingCnt++;
                        if (ratingCnt == 2) properties.put("rating", secondToken);
                        else ratingStorage = secondToken;
                        break;
                }

                if (firstToken.equals("\"profileImageUrl\"")) {
                    thirdToken = stt.nextToken();
                    properties.put("profileImageUrl", secondToken + ":" + thirdToken);
                }
            }

            if (solvedCountCnt == 1) {
                properties.put("solvedCount", solvedCountStorage);
            }

            if (ratingCnt == 1) {
                properties.put("rating", ratingStorage);
            }

            System.out.println("properties = " + properties);

            bojImg = properties.get("profileImageUrl");
            solvedCount = Long.parseLong(properties.get("solvedCount"));
            tier = Long.parseLong(properties.get("tier"));
            rating = Long.parseLong(properties.get("rating"));
            maxStreak = Long.parseLong(properties.get("maxStreak"));

            u.updateUserBojInfo(solvedCount, tier, rating, maxStreak, bojImg);
            users.add(u);
        });

        userRepository.saveAll(users);
    }

}