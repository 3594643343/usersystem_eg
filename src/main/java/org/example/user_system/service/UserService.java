package org.example.user_system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.user_system.dto.*;
import org.example.user_system.entity.User;
import org.example.user_system.vo.LoginVo;
import org.example.user_system.vo.UserInfoVo;
import org.example.user_system.vo.UserUpdateInfoVo;

import java.util.List;

/**
* @author ASUS
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-07-16 14:12:34
*/
public interface UserService extends IService<User> {
    /**
     * 管理员登录
     * @param loginDto 用户登录信息
     * @return LoginVo
     */
    LoginVo adminLogin(LoginDto loginDto);

    /**
     * 新增用你那过户
     * @param userAddDto 用户的个人信息
     */
    void add(UserAddDto userAddDto);

    /**
     * 删除用户
     * @param ids 用户id列表
     */
    void delete(List<Integer> ids);

    /**
     * 修改密码
     * @param userUpdatePasswordDto 修改密码相关信息
     */
    void updatePassword(UserUpdatePasswordDto userUpdatePasswordDto);

    /**
     * 更新用户角色
     * @param userUpdateRoleDto 更新用户角色相关信息
     */
    void updateRole(UserUpdateRoleDto userUpdateRoleDto);

    /**
     * 更新用户个人信息
     * @param userUpdateInfoDto 新的用户个人信息
     * @return UserUpdateInfoVo
     */
    UserUpdateInfoVo updateInfo(UserUpdateInfoDto userUpdateInfoDto);

    /**
     * 获取用户详细信息
     * @param id 用户id
     * @return UserInfoVo
     */
    UserInfoVo getInfo(Integer id);

    /**
     * 根据条件查询用户列表
     * @param userQueryDto 查询条件
     * @return PageResult<UserInfoVo>
     */
    List<UserInfoVo> query(UserQueryDto userQueryDto);
}
