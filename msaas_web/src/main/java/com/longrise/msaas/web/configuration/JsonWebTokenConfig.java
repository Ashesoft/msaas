package com.longrise.msaas.web.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.longrise.msaas.global.domain.APIException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.UUID;

@Configuration
public class JsonWebTokenConfig {

    @Value("${jwt.secret}")
    private String secret; // 密钥

    @Value("${jwt.expire}")
    private Long expire; // 过期时间

    /**
     * 生成jwt
     * iss : jwt签发者
     * sub : 面向的用户(jwt所面向的用户)
     * aud : 接收jwt的一方
     * exp : 过期时间戳(jwt的过期时间, 这个过期时间必须要大于签发时间)
     * nbf : 定义在什么时间之前, 该jwt都是不可用的
     * iat : jwt签发时间
     * jti : jwt的唯一身份标识, 主要用来作为一次性token, 从而回避重放攻击
     * @param uaccount 用户唯一账号(标识符)
     * @return 返回token
     */
    public String generateToken(String uaccount) {
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + expire * 1000);
        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withSubject(uaccount)
                .withIssuedAt(now)
                .withExpiresAt(expireTime)
                .sign(Algorithm.HMAC256(secret));
    }

    /**
     * 获取jwt中所自定义的信息
     * @param key 字段名
     * @param token 生成后的jwt
     * @return 返回自定义信息
     */
    public Claim getClaimByToken(String key, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        try {
            String[] header = token.split("Bearer;");
            token = header[1];
            return JWT
                    .require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token)
                    .getClaim(key);
        }catch (Exception e){
            throw new APIException(5000,"令牌验证失败");
        }
    }

    /**
     * token是否过期
     * @return  true：过期
     */
    public boolean isExpired(String token) {
        return this.getClaimByToken("exp", token).asDate().before(new Date());
    }

    /*
    public static void main(String[] args) {
        EntityBean bean = new EntityBean(1);
        bean.put("uname", "ispring");
        bean.put("uphone", "13109290263");
        JsonWebTokenConfig jsonWebTokenConfig = new JsonWebTokenConfig();
        String token = jsonWebTokenConfig.generateToken(bean);
        System.out.println("token = " + token);
        Claim claim = jsonWebTokenConfig.getClaimByToken("info", token);
        System.out.println("info = " + claim.as(EntityBean.class));
        claim = jsonWebTokenConfig.getClaimByToken("exp", token);
        System.out.println("exp = " + claim.asDate());
    }
    */
}
