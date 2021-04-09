package com.dwj.generator.model.admin;

import lombok.Data;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-04-08 14:25
 **/
@Data
public class UpdateForm {

    private String oldPassword;

    private String newPassword;

    private String againPassword;
}
