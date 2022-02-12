package com.xxh.blog.util;

import java.security.MessageDigest;

public class MD5Util extends Exception{
    public MD5Util() {
    }
    public static String code(String str){
        try{
            MessageDigest md=MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] byteDigest = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for(int offset =0;offset < byteDigest.length;offset++) {
                i = byteDigest[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
                //32位加密
                return buf.toString();
                //16位加密
                //return buf.toString().substring(8,24);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static void main(String[] args){
        System.out.println(code("0205"));
    }
    //b2c7cb394f65598f74e7e16a1c6dd95a
    //b199e05d5f2c938762856c0c2705d4de
}
