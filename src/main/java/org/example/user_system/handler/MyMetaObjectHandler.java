package org.example.user_system.handler;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.example.user_system.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * @param metaObject meta对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("insert 开始自动填充");

        if(metaObject.getOriginalObject() instanceof User) {
            if(ObjectUtil.isNotEmpty(metaObject.getValue("gender"))) {
                this.strictInsertFill(metaObject,"gender", Integer.class,-1);
            }
            if(ObjectUtil.isNotEmpty(metaObject.getValue("role"))) {
                this.strictInsertFill(metaObject,"role", Integer.class,2);
            }
        }

        this.strictInsertFill(metaObject,"createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject,"updateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject,"deleted", Integer.class, 1);
    }

    /**
     * @param metaObject meta对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("update 开始自动填充");
        this.strictUpdateFill(metaObject,"updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
