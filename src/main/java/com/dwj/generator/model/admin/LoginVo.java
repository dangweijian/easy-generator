package com.dwj.generator.model.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-04-08 9:53
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVo implements Serializable {

    private String username;

    private String token;
}
