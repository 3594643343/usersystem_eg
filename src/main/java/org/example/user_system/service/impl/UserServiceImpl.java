package org.example.user_system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.example.user_system.context.BaseContext;
import org.example.user_system.dto.*;
import org.example.user_system.entity.User;
import org.example.user_system.mapper.UserMapper;
import org.example.user_system.properties.JwtProperties;
import org.example.user_system.service.UserService;
import org.example.user_system.utils.JwtUtils;
import org.example.user_system.utils.SecureUtils;
import org.example.user_system.vo.LoginVo;
import org.example.user_system.vo.UserInfoVo;
import org.example.user_system.vo.UserUpdateInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
* @author ASUS
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-07-16 14:12:34
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    @Resource
    private JwtProperties jwtProperties;

    /**
     * 管理员登录
     *
     * @param loginDto 用户登录信息
     * @return LoginVo
     */
    @Override
    public LoginVo adminLogin(LoginDto loginDto) {
        // 获取用户，由于设置了逻辑删除键，会在未删除的数据中查找
        User user = userMapper.selectOne(lambdaQuery()
                .eq(User::getAccount, loginDto.getAccount()));

        if (user == null) {
            throw new RuntimeException("账号不存在");
        }

        String password = SecureUtils.EncryptedPassword(loginDto.getPassword());
        if(!password.equals(loginDto.getPassword())){
            throw new RuntimeException("密码错误");
        }

        String token = JwtUtils.crateJwt(jwtProperties, user.getId());

        return LoginVo.builder()
                .token(token)
                .build();
    }

    /**
     * 新增用你那过户
     *
     * @param userAddDto 用户的个人信息
     */
    @Override
    public void add(UserAddDto userAddDto) {
        User oldUser = userMapper.selectOne(lambdaQuery()
                .eq(User::getAccount, userAddDto.getAccount()));

        if(oldUser != null){
            throw new RuntimeException("该账号已经存在");
        }

        User user = new User();
        BeanUtils.copyProperties(userAddDto, user);

        user.setPassword(SecureUtils.EncryptedPassword(user.getPassword()));

        userMapper.insert(user);
    }

    /**
     * 删除用户
     *
     * @param ids 用户id列表
     */
    @Override
    public void delete(List<Integer> ids) {
        userMapper.delete(lambdaQuery().in(User::getId, ids));
    }

    /**
     * 修改密码
     *
     * @param userUpdatePasswordDto 修改密码相关信息
     */
    @Override
    public void updatePassword(UserUpdatePasswordDto userUpdatePasswordDto) {
        User user = userMapper.selectById(userUpdatePasswordDto.getUserId());

        String oldPassword = SecureUtils.EncryptedPassword(userUpdatePasswordDto.getOldPassword());
        if(!oldPassword.equals(user.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }

        String newPassword = SecureUtils.EncryptedPassword(userUpdatePasswordDto.getNewPassword());
        userMapper.update(lambdaUpdate()
                .eq(User::getId, user.getId())
                .set(User::getPassword, newPassword));
    }

    /**
     * 更新用户角色
     *
     * @param userUpdateRoleDto 更新用户角色相关信息
     */
    @Override
    public void updateRole(UserUpdateRoleDto userUpdateRoleDto) {
        User self = userMapper.selectById(BaseContext.getCurrentId());

        if (self.getRole() <= userUpdateRoleDto.getRoleId()) {
            throw new RuntimeException("权限不够，无法修改该用户角色");
        }

        userMapper.update(lambdaUpdate()
                .eq(User::getId, userUpdateRoleDto.getUserId())
                .set(User::getRole, userUpdateRoleDto.getRoleId()));
    }

    /**
     * 更新用户个人信息
     *
     * @param userUpdateInfoDto 新的用户个人信息
     * @return UserUpdateInfoVo
     */
    @Override
    public UserUpdateInfoVo updateInfo(UserUpdateInfoDto userUpdateInfoDto) {
        User user = new User();
        BeanUtils.copyProperties(userUpdateInfoDto, user);

        userMapper.updateById(user);

        UserUpdateInfoVo userUpdateInfoVo = new UserUpdateInfoVo();
        BeanUtils.copyProperties(userMapper.selectById(user.getId()), userUpdateInfoVo);

        return userUpdateInfoVo;
    }

    /**
     * 获取用户详细信息
     *
     * @param id 用户id
     * @return UserInfoVo
     */
    @Override
    public UserInfoVo getInfo(Integer id) {
        User user = userMapper.selectById(id);

        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(user, userInfoVo);

        return userInfoVo;
    }

    /**
     * 根据条件查询用户列表
     *
     * @param userQueryDto 查询条件
     * @return PageResult<UserInfoVo>
     */
    @Override
    public List<UserInfoVo> query(UserQueryDto userQueryDto) {
        IPage<User> iPage = new Page<>(userQueryDto.getPage(), userQueryDto.getSize());

        IPage<User> userIPage = super.page(iPage, buildQueryWrapper(userQueryDto));

        return userIPage.getRecords().stream()
                .map(user -> {
                    UserInfoVo userInfoVo = new UserInfoVo();
                    BeanUtils.copyProperties(user, userInfoVo);
                    return userInfoVo;
                })
                .collect(Collectors.toList());
    }

    private LambdaQueryChainWrapper<User> buildQueryWrapper(UserQueryDto userQueryDto) {

        LambdaQueryChainWrapper<User> lambdaQueryChainWrapper = lambdaQuery();

        Map<Object, Consumer<LambdaQueryChainWrapper<User>>> queryConditions = new HashMap<>();
        queryConditions.put(userQueryDto.getAccount(), wrapper -> wrapper.like(User::getAccount, userQueryDto.getAccount()));
        queryConditions.put(userQueryDto.getNickname(), wrapper -> wrapper.like(User::getNickname, userQueryDto.getNickname()));
        queryConditions.put(userQueryDto.getGender(), wrapper -> wrapper.eq(User::getGender, userQueryDto.getGender()));
        queryConditions.put(userQueryDto.getRole(), wrapper -> wrapper.eq(User::getRole, userQueryDto.getRole()));

        queryConditions.entrySet().stream()
                .filter(entry -> ObjectUtil.isNotEmpty(entry.getKey()))
                .forEach(entry -> entry.getValue().accept(lambdaQueryChainWrapper));

        return lambdaQueryChainWrapper;
    }
}




