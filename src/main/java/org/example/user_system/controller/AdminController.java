package org.example.user_system.controller;

import cn.hutool.core.util.ObjectUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.user_system.context.BaseContext;
import org.example.user_system.dto.*;
import org.example.user_system.result.PageResult;
import org.example.user_system.service.UserService;
import org.example.user_system.vo.LoginVo;
import org.example.user_system.vo.UserGetCountVo;
import org.example.user_system.vo.UserInfoVo;
import org.example.user_system.vo.UserUpdateInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "管理员接口")
@Slf4j
public class AdminController {

    @Resource
    private UserService userService;

    /**
     * 管理员登录
     * @param loginDto 用户登录信息
     * @return LoginVo
     */
    @PostMapping("/login")
    @Operation(summary = "管理员登录")
    public LoginVo login(@RequestBody @Validated LoginDto loginDto) {
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
        if(ObjectUtil.isEmpty(ids)) {
            throw new RuntimeException("请选择需要删除的用户");
        }

        log.info("删除用户 ids = {}", ids);

        userService.delete(ids);
    }

    @PutMapping("/update/password")
    @Operation(summary = "修改密码")
    public void updatePassword(@RequestBody UserUpdatePasswordDto userUpdatePasswordDto) {
        if(ObjectUtil.isEmpty(userUpdatePasswordDto.getUserId())) {
            userUpdatePasswordDto.setUserId(BaseContext.getCurrentUser().getUserId());
        }

        log.info("修改密码 userUpdatePasswordDto = {}", userUpdatePasswordDto);

        userService.updatePassword(userUpdatePasswordDto);
    }

    @PutMapping("/update/role")
    @Operation(summary = "更新用户角色")
    public void updateRole(@RequestBody UserUpdateRoleDto userUpdateRoleDto) {
        if(ObjectUtil.isEmpty(userUpdateRoleDto.getUserId())) {
            throw new RuntimeException("请选择你需要修改的用户");
        }

        log.info("更新用户角色 userUpdateRoleDto = {}", userUpdateRoleDto);

        userService.updateRole(userUpdateRoleDto);
    }

    @PutMapping("/update/info")
    @Operation(summary = "修改用户个人信息")
    public UserUpdateInfoVo updateInfo(@RequestBody UserUpdateInfoDto userUpdateInfoDto) {
        if(ObjectUtil.isEmpty(userUpdateInfoDto.getId())) {
            userUpdateInfoDto.setId(BaseContext.getCurrentUser().getUserId());
        }

        log.info("修改用户个人信息 userUpdateInfoDto = {}", userUpdateInfoDto);

        return userService.updateInfo(userUpdateInfoDto);
    }

    @GetMapping("/get/info")
    @Operation(summary = "获取用户详细信息")
    public UserInfoVo getInfo(Integer id) {
        if(ObjectUtil.isEmpty(id)) {
            id = BaseContext.getCurrentUser().getUserId();
        }

        log.info("获取用户详细信息 id = {}", id);

        return userService.getInfo(id);
    }

    @GetMapping("/get/count")
    @Operation(summary =  "获取用户总数")
    public UserGetCountVo getCount() {
        log.info("获取用户总数");

        return UserGetCountVo.builder().num(userService.count()).build();
    }

    @GetMapping("/query")
    @Operation(summary = "根据条件查询用户列表")
    public PageResult<UserInfoVo> query(UserQueryDto userQueryDto) {
        if (ObjectUtil.isEmpty(userQueryDto.getPage())) {
            userQueryDto.setPage(1);
        }
        if (ObjectUtil.isEmpty(userQueryDto.getSize())) {
            userQueryDto.setSize(10);
        }

        log.info("根据条件查询用户列表 userQueryDto = {}", userQueryDto);

        return userService.query(userQueryDto);
    }

    @DeleteMapping("/logout")
    @Operation(summary = "登出")
    public void logout() {
        userService.logout();
    }
}
