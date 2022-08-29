package bssm.db.bssmgit.domain.user.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class BsmOauthResourceDto {

    private int grade;
    private int classNo;
    private int studentNo;
    private String name;
    private String email;

    @JsonProperty("user")
    private void unpackNested(Map<String, Object> user) {
        this.grade = (int) user.get("grade");
        this.classNo = (int) user.get("classNo");
        this.studentNo = (int) user.get("studentNo");
        this.name = (String) user.get("name");
        this.email = (String) user.get("email");
    }
}
