package com.dwj.generator.common.response;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.util.List;

/**
 * @ClassName PageResponse
 * @Description 分页响应
 * @Author dwjian
 * @Date 2020/1/13 0:26
 */
@Data
public class PageResponse<T> extends Response<List<T>> {
    private long pages;//总页数
    private long limit;//每页条数
    private long count;//总记录数

    private PageResponse(Response<List<T>> response, long pages, long limit, long count) {
        super(response);
        this.pages = pages;
        this.limit = limit;
        this.count = count;
    }

    public static <T> PageResponse<T> success(IPage<T> page){
        Response<List<T>> response = success(ResultEnum.SUCCESS.getCode(), ResultEnum.SUCCESS.getMsg(), page.getRecords());
        return new PageResponse<>(response, page.getPages(), page.getSize(), page.getTotal());
    }

    public static <T> PageResponse<T> fail(String msg , List<T> data) {
        Response<List<T>> fail = fail(ResultEnum.FAIL.getCode(), msg, data);
        return new PageResponse<>(fail, 0, 0, 0);
    }

}
