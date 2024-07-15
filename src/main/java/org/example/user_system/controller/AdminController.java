package org.example.user_system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.user_system.dto.*;
import org.example.user_system.service.UserService;
import org.example.user_system.vo.LoginVo;
import org.example.user_system.vo.UserInfoVo;
import org.example.user_system.vo.UserUpdateInfoVo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "通用接口")
@Slf4j
public class AdminController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    @Operation(summary = "管理员登录")
    public LoginVo login(@RequestBody LoginDto loginDto) {
        log.info("管理员登录 loginDto = {}", loginDto);

        return userService.adminLogin(loginDto);
    }

    @PostMapping("/add")
    @Operation(summary = "新增用户")
    public void add(@RequestBody UserAddDto userAddDto) {
        log.info("新增用户 userAddDto = {}", userAddDto);

        userService.add(userAddDto);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除用户")
    public void delete(@RequestParam List<Integer> ids) {
        log.info("删除用户 ids = {}", ids);

        userService.delete(ids);
    }

    @PutMapping("/update/password")
    @Operation(summary = "修改密码")
    public void updatePassword(UserUpdatePasswordDto userUpdatePasswordDto) {
        log.info("修改密码 userUpdatePasswordDto = {}", userUpdatePasswordDto);

        userService.updatePassword(userUpdatePasswordDto);
    }

    @PutMapping("/update/role")
    @Operation(summary = "更新用户角色")
    public void updateRole(UserUpdateRoleDto userUpdateRoleDto) {
        log.info("更新用户角色 userUpdateRoleDto = {}", userUpdateRoleDto);

        userService.updateRole(userUpdateRoleDto);
    }

    @PutMapping("/update/info")
    @Operation(summary = "修改用户个人信息")
    public UserUpdateInfoVo updateInfo(UserUpdateInfoDto userUpdateInfoDto) {
        log.info("修改用户个人信息 userUpdateInfoDto = {}", userUpdateInfoDto);

        return userService.updateInfo(userUpdateInfoDto);
    }

    @GetMapping("/get/info")
    @Operation(summary = "获取用户详细信息")
    public UserInfoVo getInfo(Integer id) {
        log.info("获取用户详细信息 id = {}", id);

        return userService.getInfo(id);
    }

    @GetMapping("/get/count")
    @Operation(summary =  "获取用户总数")
    public Long getCount() {
        log.info("获取用户总数");

        return userService.count();
    }

    @GetMapping("/query")
    @Operation(summary = "根据条件查询用户列表")
    public List<UserInfoVo> query(UserQueryDto userQueryDto) {
        log.info("根据条件查询用户列表 userQueryDto = {}", userQueryDto);

        return userService.query(userQueryDto);
    }
}
