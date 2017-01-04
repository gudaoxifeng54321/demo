package com.ly.sys.common.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class StringJoinUtils {
    
    public static <M> String join(String join,Set<M> set){
      M[] array = (M[]) set.toArray();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<array.length;i++){
             if(i==(array.length-1)){
                 sb.append(array[i]);
             }else{
                 sb.append(array[i]).append(join);
             }
        }
        
        return new String(sb);
    }
 
    
    public static<M> void SetAddArray(Set<M> set,M[] array){
      for(M m :array ){
        set.add(m);
      }
    }
    
    
    
    
    public static void main(String[] args){
 
        Set<Long>  sa= new HashSet<Long>();
        sa.add(1L);
        sa.add(2L);
        sa.add(3L);
        String s1=StringJoinUtils.join(",",sa);
        System.out.println(s1);
 
    }
}
