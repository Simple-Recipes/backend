package com.recipes.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User Report view object")
public class UserReportVO implements Serializable {


    private String dateList;


    private String totalUserList;


    private String newUserList;

}
