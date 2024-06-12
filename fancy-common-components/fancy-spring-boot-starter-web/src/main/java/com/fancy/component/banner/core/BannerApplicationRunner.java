package com.fancy.component.banner.core;

import cn.hutool.core.thread.ThreadUtil;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.util.ClassUtils;

/**
 * 项目启动Banner
 *
 * @author paven
 */
@Slf4j
public class BannerApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        ThreadUtil.execute(() -> {
            // 延迟 1 秒，保证输出到结尾
            ThreadUtil.sleep(1, TimeUnit.SECONDS);
        });
    }

    private static boolean isNotPresent(String className) {
        return !ClassUtils.isPresent(className, ClassUtils.getDefaultClassLoader());
    }
}