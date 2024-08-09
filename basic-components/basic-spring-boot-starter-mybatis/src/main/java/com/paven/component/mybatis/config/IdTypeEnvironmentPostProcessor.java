package com.paven.component.mybatis.config;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.paven.common.util.collection.SetUtils;
import com.paven.component.mybatis.core.enums.SqlConstants;
import com.paven.component.mybatis.core.util.JdbcUtils;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 当 IdType 为 {@link IdType#NONE} 时，根据 PRIMARY 数据源所使用的数据库，自动设置
 *
 * @author paven
 */
@Slf4j
public class IdTypeEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String ID_TYPE_KEY = "mybatis-plus.global-config.db-config.id-type";

    private static final String DATASOURCE_DYNAMIC_KEY = "spring.datasource.dynamic";
    private static final String DATASOURCE_KEY = "spring.datasource";

    private static final String QUARTZ_JOB_STORE_DRIVER_KEY = "spring.quartz.properties.org.quartz.jobStore.driverDelegateClass";

    private static final Set<DbType> INPUT_ID_TYPES = SetUtils.asSet(
            DbType.ORACLE, DbType.ORACLE_12C,
            DbType.POSTGRE_SQL, DbType.KINGBASE_ES, DbType.DB2, DbType.H2);

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        // 如果获取不到 DbType，则不进行处理
        DbType dbType = getDbType(environment);
        if (dbType == null) {
            return;
        }
        // 设置 Quartz JobStore 对应的 Driver
        setJobStoreDriverIfPresent(environment, dbType);

        // 初始化 SQL 静态变量
        SqlConstants.init(dbType);

        // 如果非 NONE，则不进行处理
        IdType idType = getIdType(environment);
        if (idType != IdType.NONE) {
            return;
        }
        if (INPUT_ID_TYPES.contains(dbType)) {
            setIdType(environment, IdType.INPUT);
            return;
        }
        setIdType(environment, IdType.AUTO);
    }

    public IdType getIdType(ConfigurableEnvironment environment) {
        return environment.getProperty(ID_TYPE_KEY, IdType.class);
    }

    public void setIdType(ConfigurableEnvironment environment, IdType idType) {
        environment.getSystemProperties().put(ID_TYPE_KEY, idType);
        log.info("[setIdType][修改 MyBatis Plus 的 idType 为({})]", idType);
    }

    public void setJobStoreDriverIfPresent(ConfigurableEnvironment environment, DbType dbType) {
        String driverClass = environment.getProperty(QUARTZ_JOB_STORE_DRIVER_KEY);
        if (StrUtil.isNotEmpty(driverClass)) {
            return;
        }
        // 根据 dbType 类型，获取对应的 driverClass
        driverClass = switch (dbType) {
            case POSTGRE_SQL -> "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate";
            case ORACLE, ORACLE_12C -> "org.quartz.impl.jdbcjobstore.oracle.OracleDelegate";
            case SQL_SERVER, SQL_SERVER2005 -> "org.quartz.impl.jdbcjobstore.MSSQLDelegate";
            default -> throw new IllegalArgumentException(String.format("未知的 dbType[%s]", dbType));
        };
        // 设置 driverClass 变量
        if (StrUtil.isNotEmpty(driverClass)) {
            environment.getSystemProperties().put(QUARTZ_JOB_STORE_DRIVER_KEY, driverClass);
        }
    }

    public static DbType getDbType(ConfigurableEnvironment environment) {
        String datasource = environment.getProperty(DATASOURCE_KEY);
        if (StrUtil.isEmpty(datasource)) {
            return null;
        }
        String url = environment.getProperty(DATASOURCE_KEY + ".datasource.url");
        if (StrUtil.isEmpty(url)) {
            return null;
        }
        return JdbcUtils.getDbType(url);
    }

}