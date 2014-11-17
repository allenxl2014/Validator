package com.liang.util;

import org.apache.xerces.utils.StringHasher;
import org.fto.jthink.util.DateTimeHelper;
import org.fto.jthink.util.StringHelper;

/**
 * @author Administrator
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class TimeHelper {



  /**
    方法功能：
         补全日期功能，如将2004-09-05 补全为：2004-09-05 00:00:00或2004-09-05 23:59:59
  
    参数说明：
         pDate，需要被被全的时间串，格式如：2004-09-05
         flag,被全标识：
            1为补全为：2004-09-05 00:00:00 ；
              yyyy-mm-dd -> yyyy-mm-dd 00:00:00
              yyyy-mm-dd hh -> yyyy-mm-dd hh:00:00
              yyyy-mm-dd hh:mm ->yyyy-mm-dd hh:mm:00
              yyyy-mm-dd hh:mm:ss ->yyyy-mm-dd hh:mm:ss      
  
            2为补全为：2004-09-05 23:59:59格式；
              yyyy-mm-dd -> yyyy-mm-dd 23:59:59
              yyyy-mm-dd hh -> yyyy-mm-dd hh:59:59
              yyyy-mm-dd hh:mm ->yyyy-mm-dd hh:mm:59
              yyyy-mm-dd hh:mm:ss ->yyyy-mm-dd hh:mm:ss               
              
            其它的字串自动加补到pDate后面。
  
            如：fillDateTime('2004-09-03',"17:30:30")将返回2004-09-03 17:30:30
    返回值：
         返回补全后的时间串。
   */
  public static String fillDateTime(String pDate, String flag){
    if(pDate == null || pDate.trim().equals("")){
      return null;
    }
    String[] pDates = StringHelper.split(pDate.trim()," ");
    String[] dates = StringHelper.split(pDates[0], "-");
    
    String year = dates[0];
    String month = dates[1];
    String day = dates[2];

    String hour="00";
    String minute="00";
    String second="00";
    
    if(pDates.length==2){
      String[] times = StringHelper.split(pDates[1],":");
      if(times.length==1){
        hour = times[0];
      }
      if(times.length==2){
        hour = times[0];
        minute = times[1];
      }
      if(times.length==3){
        hour = times[0];
        minute = times[1];
        second = times[2];
      }
    }
    
    if(flag.equals("1")){
      return year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
      
    }else if(flag.equals("2")) {
      
      if(pDates.length==1){
        return year+"-"+month+"-"+day+" "+"23:59:59";
      }
      String[] times = StringHelper.split(pDates[1],":");
      if(times.length==1){
        return year+"-"+month+"-"+day+" "+hour+":59:59";
      }
      if(times.length==2){
        return year+"-"+month+"-"+day+" "+hour+":"+minute+":59";
      }
      if(times.length==3){
        return year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
      }
      return year+"-"+month+"-"+day;
    }else{
      return year+"-"+month+"-"+day + " "+flag;
    }
  }

  /**
  补全日期时间
       pDate，需要被被全的时间串，格式如：2004-09-05

       flag, 类型,值别为
          1: 如果pDate没有时分秒,将补全时间为:(00:00:00),只输入了小时,补全为:(小时:00:00),其它,不处理
          2: 如果pDate没有时分秒,将补全时间为:(23:59:59),只输入了小时,补全为:(小时:59:59),其它,不处理
          3: 如果pDate没有时分秒,将补全时间为:(23:59:59), 只输入了小时,补全为:(小时:00:00),其它,不处理
          4: 如果pDate没有时分秒,将补全时间为:(当前服务器时间), 只输入了小时,补全为:(小时:00:00),其它,不处理
          5: 如果pDate没有时分秒,将补全时间为:(当前服务器时间), 只输入了小时,补全为:(小时:59:59),其它,不处理
          其它值:直接加在日期后面
  */
  public static String fillTime(String pDate,String flag){
    if(pDate == null || pDate.trim().equals("")){
      return null;
    }
    String dt;
    if(flag.equals("1")){
        dt = fillDateTime(pDate, "1");
    }else if(flag.equals("2")){
        dt = fillDateTime(pDate, "2");
    }else if(flag.equals("3")){
        if(onlyHour(pDate)){
          dt = fillDateTime(pDate, "1");
        }else{
          dt = fillDateTime(pDate, "2");
        }
    }else if(flag.equals("4")){
        if(getTime(pDate)==""){
          dt = pDate+" "+getServerTime();
        }else if(onlyHour(pDate)){
          dt = fillDateTime(pDate, "1");
        }else{
          dt = pDate;
        }
    }else if(flag.equals("5")){
        if(getTime(pDate)==""){
          dt = pDate+" "+getServerTime();
        }else if(onlyHour(pDate)){
          dt = fillDateTime(pDate, "2");
        }else{
          dt = pDate;
        }
    }else{
        dt = fillDateTime(pDate, flag);
    }
    return dt; 
  }
  private static String getTime(String d){
    String[] ds = StringHelper.split(d, " ");
    if(ds.length>=2){
      return ds[1];
    }
    return "";
  }
  private static boolean onlyHour(String d){
    String time = getTime(d);
    if(time==""){
      return false;
    }
    String[] times = StringHelper.split(time, ":");
    if(times.length>1){
      return false;
    }
    return true;
  }

  /**
    返回服务器时间, 时:分
  */
  public static String getServerTime(){
    return DateTimeHelper.formatDateTimetoString(DateTimeHelper.getSystemDate(), "HH:mm:ss");
  }

  public static void main(String[] args) {
    System.out.println(fillDateTime("2006-3-2","1"));
    System.out.println(fillDateTime("2006-3-2 11","1"));
    System.out.println(fillDateTime("2006-3-2 11:10","1"));
    System.out.println(fillDateTime("2006-3-2 11:10:9","1"));
    System.out.println();
    System.out.println(fillDateTime("2006-3-2","2"));
    System.out.println(fillDateTime("2006-3-2 11","2"));
    System.out.println(fillDateTime("2006-3-2 11:10","2"));
    System.out.println(fillDateTime("2006-3-2 11:10:9","2"));    
    
    System.out.println();
    System.out.println(fillTime("2006-3-2","1"));
    System.out.println(fillTime("2006-3-2","2"));
    System.out.println(fillTime("2006-3-2","3"));
    System.out.println(fillTime("2006-3-2","4"));
    System.out.println(fillTime("2006-3-2","5"));
    
    
    System.out.println();
    System.out.println(fillTime("2006-3-2 11","1"));
    System.out.println(fillTime("2006-3-2 11","2"));
    System.out.println(fillTime("2006-3-2 15","3"));
    System.out.println(fillTime("2006-3-2 11","4"));
    System.out.println(fillTime("2006-3-2 11","5"));
  }
}
