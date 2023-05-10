package bssm.db.bssmgit.domain.github.util;

import bssm.db.bssmgit.global.util.Constants;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class GithubUtil {

    public static URLConnection getGithubUrlConnection(String githubId) throws IOException {
        URL url = new URL(Constants.GITHUB_URL + githubId);
        return url.openConnection();
    }
}
