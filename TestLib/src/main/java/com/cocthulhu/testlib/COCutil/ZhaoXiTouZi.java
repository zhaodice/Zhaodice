package com.cocthulhu.testlib.COCutil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZhaoXiTouZi {

    public static void main(String[] a) throws IOException {
        /*
        COCHelper.helper_interface_out o;
        Scanner scan = new Scanner(System.in);
        // 从键盘接收数据
        // next方式接收字符串
        System.out.println("next接收：");
        // 判断是否还有输入
        while (scan.hasNext()) {
            String str1 = scan.next();
            o=COCHelper.cmd(new COCHelper.helper_interface_in(str1,"2410992617",System.currentTimeMillis()));
            if(o!=null)
                System.out.println(o.msg);
        }
        */
        final Pattern test = Pattern.compile("[{]%(.*?)[}]");
        Matcher m=test.matcher("\\n你的超能力是【\\{%随机能力}】，发动能力的代价是【\\{%随机代价}】。");
        m.find();
        System.out.println(m.group(1));
        m.find();
        System.out.println(m.group(1));
        //scan.close();


        //System.out.println("-sc1d5/2d10:\n"+o.msg);
    }
}
