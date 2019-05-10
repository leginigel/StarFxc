package com.stars.tv.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @Author: Dicks.yang
 * @Date: 2019.04.29
 */

public class MD5 {

    private static final String[] strDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};


    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (bByte < 0) {
            iRet = bByte + 256;
        }

        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    private String byteToNum(byte bByte) {
        int iRet = bByte;
        if (bByte < 0) {
            iRet = bByte + 256;
        }

        return String.valueOf(iRet);
    }

    // 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        int i = 0;

        for(int lh = bByte.length; i < lh; ++i) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    public static String getMD5CodeStr( String strObj) {
        String resultString = "";

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return resultString;
    }

    public static byte[] getMD5Code( byte[] strObj) {
        byte[] resultString = null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = md.digest(strObj);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return resultString;
    }

}
