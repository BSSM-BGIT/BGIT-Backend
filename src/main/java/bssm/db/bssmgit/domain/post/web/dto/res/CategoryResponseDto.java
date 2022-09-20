package bssm.db.bssmgit.domain.post.web.dto.res;

import bssm.db.bssmgit.domain.post.entity.Category;
import lombok.Getter;

@Getter
public class CategoryResponseDto {
    private final String name;

    public CategoryResponseDto(Category category) {
        this.name = category.getName();
    }
}

