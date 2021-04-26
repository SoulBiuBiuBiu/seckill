package com.example.miaosha.util;


import org.springframework.util.DigestUtils;

public class MD5Util {
    public static String md5(String src){
        return DigestUtils.md5DigestAsHex(src.getBytes());
    }
    public static final String salt="1a2b3c4d";

    public static String inputPassToFormPass(String inputPass){
        String str=""+salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }
    public static String formPassToDbPass(String formPass,String salt){
        String str=""+salt.charAt(0)+salt.charAt(2)+formPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }

    public static String InputPassToDbPass(String input,String saltDB){
        String formPass = inputPassToFormPass(input);
        return formPassToDbPass(formPass,saltDB);
    }

    public static void main(String[] args) {
        System.out.println(InputPassToDbPass("123456","1a2b3c4d"));
    }
}
