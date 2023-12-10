package io.github.toquery.example.spring.security.acl.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.support.NoOpCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionCacheOptimizer;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionCacheOptimizer;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.BasicLookupStrategy;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
public class SpringSecurityAclConfig {

    /**
     * Acl会被频繁访问，所以设置缓存相当有必要
     *
     */
    @Bean
    public AclCache aclCache(
                                             PermissionGrantingStrategy permissionGrantingStrategy,
                                             AclAuthorizationStrategy aclAuthorizationStrategy) {
        return new SpringCacheBasedAclCache(new NoOpCache("aclCache"), permissionGrantingStrategy, aclAuthorizationStrategy);
    }

    @Bean
    public AuditLogger auditLogger() {
        return new ConsoleAuditLogger();
    }

    /**
     * 判断某用户/角色是否有对某个资源有某项访问权限的策略
     * 这里使用默认策略，表示：
     * 根据acl_entry表中的记录做判断
     * 该表中有个ace_order字段，在进行权限判断时，会按该字段进行排序。
     * 然后进行遍历。
     * 如果找到了granting为1的记录，则不再遍历而返回true(有权限）
     * 否则会继续遍历下一条，直接遍历到granting为1记录或是遍历完毕为止。
     * 如果没有遍历到granting为1的记录，则将返回首条granting为0的记录中的原因（audit_failure)做为无访问权限的原因返回
     *
     * 在构造函数中传入的new ConsoleAuditLogger()作用是：在控制台上直接打印权限判断的结果。
     * 此时将校验通过或未通过时，将在控制台看到相应的校验结果。
     *
     * 想了解更多详细信息，可参考：https://docs.spring.io/spring-security/site/docs/4.2.15.RELEASE/apidocs/org/springframework/security/acls/domain/DefaultPermissionGrantingStrategy.html
     *
     * @return
     */
    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy(AuditLogger auditLogger) {
        return new DefaultPermissionGrantingStrategy(auditLogger);
    }

    /**
     * 设置谁可以管理ACL控制策略，即设置ACL的管理员。
     * AclAuthorizationStrategyImpl将管理权限细分为3种。
     * 当传入1个参数时，3种管理权限将统一赋值为该参数。
     * 除此此外，还可以传入3个参数分别对3种管理权限进行配置。
     *
     * 更多详情可参考：https://docs.spring.io/spring-security/site/docs/4.2.15.RELEASE/apidocs/org/springframework/security/acls/domain/AclAuthorizationStrategyImpl.html
     *
     * @return
     */
    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Bean
    public MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler(PermissionEvaluator permissionEvaluator,
                                                                                  PermissionCacheOptimizer permissionCacheOptimizer) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        expressionHandler.setPermissionCacheOptimizer(permissionCacheOptimizer);
        return expressionHandler;
    }


    @Bean
    public PermissionCacheOptimizer permissionCacheOptimizer(AclService aclService) {
        return new AclPermissionCacheOptimizer(aclService);
    }

    @Bean
    public PermissionEvaluator permissionEvaluator(AclService aclService) {
        return new AclPermissionEvaluator(aclService);
    }

    /**
     * LookupStrategy主要提供两个功能：
     * 1. lookupPrimaryKeys 查找资源的主健
     * 2. lookupObjectIdentities 根据资源主键、资源对应的Class，近而查找资源对应的acl_object_identity中的主键
     * 该acl_object_identity主键将被PermissionGrantingStrategy调用，用于在acl_entry查找对应权限策略
     *
     */
    @Bean
    public LookupStrategy lookupStrategy(DataSource dataSource,
                                         AclCache aclCache,
                                         AclAuthorizationStrategy aclAuthorizationStrategy,
                                         AuditLogger auditLogger) {
        return new BasicLookupStrategy(dataSource, aclCache, aclAuthorizationStrategy, auditLogger);
    }

    /**
     * 基于数据源的ACL权限控制服务
     * dataSource 数据源
     * lookupStrategy 查找实体ID，实体对应的CLASS，应该此两项在acl_object_identity中的记录
     * aclCache 根据acl_object_identity记录、当前登录用户/角色，查找acl_entry表，最终获取相应的权限
     * @return
     */
    @Bean
    public JdbcMutableAclService aclService(DataSource dataSource,
                                            LookupStrategy lookupStrategy,
                                            AclCache aclCache) {
        return new JdbcMutableAclService(dataSource, lookupStrategy, aclCache);
    }

}
