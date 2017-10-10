package com.example.healthhelper.function;

import java.sql.Date;
import com.example.healthhelper.utils.PreUtils;

/**
 * Created by Dinghow on 2017/10/7.
 * 计算水量
 */

public class WaterCounter {
    public static int water_amount = 0;
    //需要喝的水量
    public static int saveAmount(){
        int weight = Integer.parseInt(PreUtils.getString("user_data","weight"));
        int gender = PreUtils.getInt("user_data","gender");
        boolean sport_model = PreUtils.getBoolean("user_data","sport_model");
        switch (gender){
            case 0:
                water_amount = weight*36;
                break;
            case 1:
                water_amount = weight*33;
                break;
            default:
                break;
        }
        if(sport_model){
            switch (gender){
                case 0:
                    water_amount = weight*40;
                    break;
                case 1:
                    water_amount = weight*37;
                    break;
                default:
                    break;
            }
        }
        PreUtils.putInt("user_data","water_amount",water_amount);
        return water_amount;
    }

    //喝掉的水量
    public static void drinkedWater(int amount){
        int water_amount  = PreUtils.getInt("user_data","water_amount");
        int drink_amount = PreUtils.getInt("user_data","drink_amount");
        drink_amount += amount;
        if(water_amount<drink_amount){
            drink_amount = water_amount;
            PreUtils.putInt("user_data","drink_amount",drink_amount);
            return;
        }
        else{
            PreUtils.putInt("user_data","drink_amount",drink_amount);
            return;
        }
    }

    public static int remainWater(){
        return PreUtils.getInt("user_data","water_amount")-PreUtils.getInt("user_data","drink_amount");
    }

    public static int drinkedPercent(){
        if(PreUtils.getInt("user_data","water_amount")!=0)
            return (100*PreUtils.getInt("user_data","drink_amount"))/PreUtils.getInt("user_data","water_amount");
        else
            return 0;
    }
}
