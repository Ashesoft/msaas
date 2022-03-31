package com.longrise.msaas.web.config;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * 对jasypt进行加解密操作的用例测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CodeSheepConfigTest{
    @Autowired
    private Environment environment;

    @Autowired
    private StringEncryptor codeSheepEncryptorBean;

    @Test
    public void encodeTest(){
        String dbpwd = "iosplay";
        System.err.println(codeSheepEncryptorBean.encrypt(dbpwd));
    }

    @Test
    public void decodeTest(){
        String dbpwd = environment.getProperty("spring.datasource.password");
        System.out.println("dbpwd = " + dbpwd);
        System.err.println(codeSheepEncryptorBean.decrypt("JZKecUE8aqUuOLJnbI4zKyNEARWx3MxPNOdLXz/5IzYBatH/mU3JOdLfds/NCwTr"));
    }
}
