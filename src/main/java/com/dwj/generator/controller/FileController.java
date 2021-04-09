package com.dwj.generator.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.dwj.generator.common.response.Response;
import com.dwj.generator.model.file.PathNode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: dangweijian
 * @description:
 * @create: 2021-03-23 15:14
 **/
@RestController
@RequestMapping("/api/file")
public class FileController {

    @GetMapping("/path/select")
    public Response<List<PathNode>> pathSelect(@RequestParam(required = false) String basePath) {
        List<PathNode> nodes = new ArrayList<>();
        if (StrUtil.isEmpty(basePath)) {
            File[] rootPaths = File.listRoots();
            if (rootPaths != null && rootPaths.length > 0) {
                Arrays.stream(rootPaths).forEach(v -> {
                    String showName = getRootPathName(v.getAbsolutePath());
                    nodes.add(new PathNode(v.getAbsolutePath(), showName, isLeaf(v.getAbsolutePath())));
                });
            }
        }else {
            File baseFile = new File(basePath);
            if (baseFile.exists() && !baseFile.isFile()) {
                File[] files = baseFile.listFiles();
                if(files != null && files.length > 0){
                    Arrays.stream(files).filter(v->!v.isHidden()&&v.isDirectory()).forEach(v-> nodes.add(new PathNode(v.getAbsolutePath(), v.getName(), isLeaf(v.getAbsolutePath()))));
                }
            }
        }
        return Response.success(nodes);
    }

    private String getRootPathName(String absolutePath) {
        Pattern pattern = Pattern.compile("[a-zA-Z]:\\\\");
        Matcher matcher = pattern.matcher(absolutePath);
        if(matcher.matches()){
            absolutePath = "本地磁盘 (" +absolutePath.replace("\\",")") ;
        }
        return absolutePath;
    }

    private boolean isLeaf(String absolutePath) {
        File baseFile = new File(absolutePath);
        if (baseFile.isFile() || !baseFile.exists()) {
            return true;
        }
        File[] files = baseFile.listFiles();
        if(files != null && files.length > 0){
            for (File file : files) {
                if(file.isDirectory() && !file.isHidden()){
                    return false;
                }
            }
        }
        return true;
    }

    private PathNode recursionParent(PathNode baseNode, String path){
        if(StrUtil.isBlank(path)){
            return baseNode;
        }
        ArrayList<PathNode> pathNodes = new ArrayList<>();
        File parentFile = new File(path).getParentFile();
        if(parentFile != null && parentFile.exists() && !parentFile.isFile()){
            if(parentFile.listFiles() != null && Objects.requireNonNull(parentFile.listFiles()).length > 0){
                for (File children : Objects.requireNonNull(parentFile.listFiles())) {
                    if(children.exists() && !children.isHidden() && children.isDirectory()){
                        if(!children.getAbsolutePath().equals(path)){
                            pathNodes.add(new PathNode(children.getAbsolutePath(), children.getName(), isLeaf(children.getAbsolutePath())));
                        }else {
                            pathNodes.add(baseNode);
                        }
                    }
                }
                if(parentFile.getParentFile() != null && parentFile.getParentFile().exists()){
                    PathNode parentNode = new PathNode(parentFile.getAbsolutePath(), parentFile.getName(), isLeaf(parentFile.getAbsolutePath()));
                    parentNode.setIsOpen(true);
                    parentNode.setChildren(pathNodes);
                    return recursionParent(parentNode, parentFile.getAbsolutePath());
                }else {
                    PathNode parentNode = new PathNode(parentFile.getAbsolutePath(), getRootPathName(parentFile.getAbsolutePath()), isLeaf(parentFile.getAbsolutePath()));
                    parentNode.setIsOpen(true);
                    if(CollectionUtil.isNotEmpty(pathNodes)){
                        parentNode.setChildren(pathNodes);
                    }
                    return parentNode;
                }
            }
        }else {
            File file = new File(path);
            return new PathNode(file.getAbsolutePath(), getRootPathName(file.getAbsolutePath()), isLeaf(file.getAbsolutePath()));
        }
        return baseNode;
    }
}
