package com.sparta.MyPosts.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//아래 두부분은 다시 정리해서 til 작성하고 주석 지우기!! +@RequireArgsConstructor까지
@NoArgsConstructor
public class  PostRequestDto {


    private String title;

    private String contents;
}
