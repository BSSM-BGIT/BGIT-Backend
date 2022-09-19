package bssm.db.bssmgit.domain.user.service;

import bssm.db.bssmgit.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
import java.util.ArrayList;
import java.util.StringTokenizer;

@RequiredArgsConstructor
@Service
public class BojService {

    private final UserRepository userRepository;

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
