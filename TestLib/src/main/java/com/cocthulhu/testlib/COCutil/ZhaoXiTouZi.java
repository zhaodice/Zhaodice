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

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

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
        /*
        final Pattern test = Pattern.compile("([ca])?([bp])?(\\d+)?(#\\d+)?([^0-9+\\-]+)?([+\\-])?(\\d+)?");
        Matcher m=test.matcher("a斗殴+10");
        m.find();
        System.out.println(m.group(5));
        System.out.println(m.group(6));
        */
        /*
        String rep="null";
        Jedis jedis = new Jedis("r-2ze8k7x0bluc5makrypd.redis.rds.aliyuncs.com", 6379);
        //jedis.log
        jedis.auth("bFhcFyCRrfHB2KLPxThWBdbx");
        jedis.select(15);
        for (String group : jedis.keys("*")) {
            System.out.println(group);
        }
        System.out.println("DEBUG测试:"+jedis.ping());
*/
        //AwLog.Log("Starting server");
       // try {
            new socketTest();
        //} catch (IOException ioe) { //AwLog.Log("Couldn't start server:\n" + ioe);
        //}
        //scan.close();


        //System.out.println("-sc1d5/2d10:\n"+o.msg);
    }
}
