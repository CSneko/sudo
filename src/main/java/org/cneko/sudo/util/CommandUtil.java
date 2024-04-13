package org.cneko.sudo.util;

public class CommandUtil {
    public static boolean isOptions(String opt){
        return true;
        //return opt.startsWith("_");
    }
    public static boolean hasOption(String opt,String a){
        return opt.contains(a);
        //return isOptions(opt) && opt.contains(a);
    }

}
