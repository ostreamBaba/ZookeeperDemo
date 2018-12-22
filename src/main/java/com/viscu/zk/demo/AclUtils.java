package com.viscu.zk.demo;

import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.security.NoSuchAlgorithmException;

/**
 * @ Create by ostreamBaba on 18-12-22
 * @ 描述
 */
public class AclUtils {

    //调用助手类进行加密
    public static String getDigestUserPwd(String id) throws NoSuchAlgorithmException {
        return DigestAuthenticationProvider.generateDigest(id);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String id = "viscu:viscu";  //id:password
        String idDigest = getDigestUserPwd(id);
        System.out.println(idDigest);
    }

}
