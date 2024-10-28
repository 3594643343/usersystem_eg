package org.example.user_system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.system.UserInfo;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.example.user_system.properties.RedisProp;
import org.example.user_system.result.PageResult;
import org.example.user_system.service.UserService;
import org.example.user_system.utils.JwtUtils;
import org.example.user_system.utils.SecureUtils;
import org.example.user_system.vo.LoginVo;
import org.example.user_system.vo.UserInfoVo;
import org.example.user_system.vo.UserUpdateInfoVo;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
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

    @Resource
    private RedisProp redisProp;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

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
                .eq(User::getAccount, loginDto.getAccount())
                .getWrapper());
//        lambdaQuery()
//                .eq(User::getAccount, loginDto.getAccount())
//                .one();

        String password = SecureUtils.EncryptedPassword(loginDto.getPassword());
        //String password = loginDto.getPassword();

        if (ObjectUtil.isEmpty(user)) {
            throw new RuntimeException("账号不存在");
        }
        if(!password.equals(user.getPassword())){
            throw new RuntimeException("密码错误 "+password+" "+user.getPassword());
        }
        if (user.getRole() > 1 ) {
            throw new RuntimeException("您没有权限");
        }

        String token = JwtUtils.crateJwt(jwtProperties,
                user.getId(), user.getRole());

        if (redisProp.getEnabled()) {
            redisTemplate.opsForValue().set(String.valueOf(user.getId()), token);
            redisTemplate.expire(String.valueOf(user.getId()), jwtProperties.getTtl(), TimeUnit.SECONDS);
        }

        return LoginVo.builder()
                .token(token)
                .build();
    }

    /**
     * 新增新用户
     *
     * @param userAddDto 用户的个人信息
     */
    @Override
    public void add(UserAddDto userAddDto) {
        User oldUser = userMapper.selectOne(lambdaQuery()
                .eq(User::getAccount, userAddDto.getAccount())
                .getWrapper());

        // User oldUser = lambdaQuery().eq(User::getAccount, userAddDto.getAccount()).one();

        if(ObjectUtil.isNotEmpty(oldUser)){
            throw new RuntimeException("该账号已经存在");
        }

        if (userAddDto.getRole() <= BaseContext.getCurrentUser().getUserRole()) {
            throw new RuntimeException("新用户不能与当前用户等级相同");
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
        if (ObjectUtil.contains(ids, BaseContext.getCurrentUser().getUserId())) {
            throw new RuntimeException("您不能删除您自己");
        }

        if (redisProp.getEnabled()) {
            ids.forEach(id -> redisTemplate.delete(String.valueOf(id)));
        }

        userMapper.delete(lambdaQuery().in(User::getId, ids).getWrapper());
    }

    /**
     * 修改密码
     *
     * @param userUpdatePasswordDto 修改密码相关信息
     */
    @Override
    public void updatePassword(UserUpdatePasswordDto userUpdatePasswordDto) {
        if (ObjectUtil.isEmpty(userUpdatePasswordDto.getNewPassword())) {
            throw new RuntimeException("新密码不能为空");
        }

        User user = userMapper.selectById(userUpdatePasswordDto.getUserId());

        String oldPassword = SecureUtils.EncryptedPassword(userUpdatePasswordDto.getOldPassword());
        if(!oldPassword.equals(user.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }

        String newPassword = SecureUtils.EncryptedPassword(userUpdatePasswordDto.getNewPassword());
        userMapper.update(lambdaUpdate()
                .eq(User::getId, user.getId())
                .set(User::getPassword, newPassword)
                .getWrapper());
    }

    /**
     * 更新用户角色
     *
     * @param userUpdateRoleDto 更新用户角色相关信息
     */
    @Override
    public void updateRole(UserUpdateRoleDto userUpdateRoleDto) {
        User self = userMapper.selectById(BaseContext.getCurrentUser().getUserId());

        if (Objects.equals(userUpdateRoleDto.getUserId(), BaseContext.getCurrentUser().getUserId())) {
            throw new RuntimeException("您不能修改您自己的角色");
        }

        if (self.getRole() >= userUpdateRoleDto.getRoleId()) {
            throw new RuntimeException("权限不够，无法修改成为该用户角色");
        }

        userMapper.update(lambdaUpdate()
                .eq(User::getId, userUpdateRoleDto.getUserId())
                .set(User::getRole, userUpdateRoleDto.getRoleId())
                .getWrapper());
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
    public PageResult<UserInfoVo> query(UserQueryDto userQueryDto) {
        IPage<User> iPage = new Page<>(userQueryDto.getPage(), userQueryDto.getSize());

        IPage<User> userIPage = userMapper.selectPage(iPage, buildQueryWrapper(userQueryDto));

        PageResult<UserInfoVo> pageResult = new PageResult<>();
        pageResult.setTotal((int) userIPage.getTotal());
        pageResult.setRecords(userIPage.getRecords().stream()
                .map(user -> {
                    UserInfoVo userInfoVo = new UserInfoVo();
                    BeanUtils.copyProperties(user, userInfoVo);
                    return userInfoVo;
                })
                .collect(Collectors.toList()));

        return pageResult;
    }

    /**
     * 用户登出
     */
    @Override
    public void logout() {
        if (redisProp.getEnabled()) {
            redisTemplate.delete(String.valueOf(BaseContext.getCurrentUser().getUserId()));
        }
    }

    private Wrapper<User> buildQueryWrapper(UserQueryDto userQueryDto) {

        LambdaQueryChainWrapper<User> lambdaQueryChainWrapper = lambdaQuery();

        Map<Object, Consumer<LambdaQueryChainWrapper<User>>> queryConditions = new HashMap<>();
        queryConditions.put(userQueryDto.getAccount(), wrapper -> wrapper.like(User::getAccount, userQueryDto.getAccount()));
        queryConditions.put(userQueryDto.getNickname(), wrapper -> wrapper.like(User::getNickname, userQueryDto.getNickname()));
        queryConditions.put(userQueryDto.getGender(), wrapper -> wrapper.eq(User::getGender, userQueryDto.getGender()));
        queryConditions.put(userQueryDto.getRole(), wrapper -> wrapper.eq(User::getRole, userQueryDto.getRole()));

        queryConditions.entrySet().stream()
                .filter(entry -> ObjectUtil.isNotEmpty(entry.getKey()))
                .forEach(entry -> entry.getValue().accept(lambdaQueryChainWrapper));

        return lambdaQueryChainWrapper.getWrapper();
    }
}




