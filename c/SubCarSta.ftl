/*
 * 整车状态
 *
 */
#include "common.h"
#include "can.h"
#include "UartProcessor.h" 
#include "ShareLib.h"
#include "SDHC_Fatfs.h"
#include "secSection.h"

st_carsta_data   g_stCarSta;
st_motor_data   g_stMotorData;
st_fuel_cell_data  g_stFuelData;
st_engine_data  g_stEngineData;
st_bat_vol_data g_stBatVolData;
st_bat_temp_data g_stBatTempData;
st_carsta_add_data g_stCarStaAdd;

//红星汽车协议配置
//整车 CAN
<#list cans as can>
extern can_param g_tCanSave${canList};
</#list>

extern st_app_tmp pTmpInfoWrite;
uint32 g_mileage_tmp = 0;
void GetCarSta(st_carsta_data *data)
{
    st_carsta_data * lp_car_data = data;
    
   
    //车辆状态
    if( (g_tCanSave0x18FFDC01.can_data[0] & 0x01) == 0x01)   //放电模式
        lp_car_data->car_status = 0x01;       //启动状态
    else
        lp_car_data->car_status = 0x02;       //熄火状态
    //充电状态
    switch(g_tCanSave0x1806E503.can_data[5] & 0x03)
    {
    case 0x01:
      lp_car_data->charging_status = 0x01;  //停车充电
      break;
    case 0x02:
      lp_car_data->charging_status = 0x04;  //充电完成
      break;
    default:
      lp_car_data->charging_status = 0x03;  //未充电
      break;
    }

    //运行模式
    lp_car_data->run_status = 0x01;  //纯电
    //车速
    speed = lp_car_data->speed;
    if(lp_car_data->charging_status == 0x03)    //未充电时
        lp_car_data->speed = (g_tCanSave0x18FFDC02.can_data[0] + g_tCanSave0x18FFDC02.can_data[1]*256);
    else
      lp_car_data->speed = 0;

  //  累计里程
    lp_car_data->mileage = (g_tCanSave0x18FFDC02.can_data[2] | (g_tCanSave0x18FFDC02.can_data[3] << 8) | \
                 (g_tCanSave0x18FFDC02.can_data[4] << 16) | (g_tCanSave0x18FFDC02.can_data[5] << 24) );
    /****总电压是用单体电压相加获得的  void ExtraVol(void) ****/
    //总电压,电池组总电压  
//    lp_car_data->total_vol = (g_tCanSave0x0CFF7E03.can_data[1] + g_tCanSave0x0CFF7E03.can_data[2]*256);
//    if(lp_car_data->total_vol > 10000)
//        lp_car_data->total_vol = 0xFFFF;
//    else
//        lp_car_data->total_vol = (lp_car_data->total_vol) *10;
    //总电流
    lp_car_data->total_i = g_tCanSave0x0CFF7E03.can_data[3] + g_tCanSave0x0CFF7E03.can_data[4]*256;
    if(lp_car_data->total_i != 0xFFFF)
          lp_car_data->total_i = lp_car_data->total_i/10 + (10000 - 3000);
     
    //SOC
    para_tmp_u32 = g_tCanSave0x0CFF7D03.can_data[1];
    if(para_tmp_u32 == 0xFF)
        lp_car_data->soc = 0xFF;
    else
        lp_car_data->soc = para_tmp_u32 / 2;
    
    //修改充电状态
//    if(lp_car_data->charging_status == 0x01 &&  lp_car_data->total_vol >= 413)
//    {
////      lp_car_data->soc = 100;
//      lp_car_data->charging_status = 0x04;  //充电完成
//    }
//    if(lp_car_data->charging_status == 0x04)
//    {
//      lp_car_data->soc = 100;
//      lp_car_data->total_i = 10000;
//    }
    
    //DC-DC状态 启动即为工作，熄火为断开
    if(lp_car_data->car_status == 2)
        lp_car_data->dc_status = 0x02;  //断开
    else
      lp_car_data->dc_status = 0x01;  //工作
        
    //档位
    if((g_tCanSave0x18FFDC01.can_data[0]&0x30) == 0x20)
        lp_car_data->gears = 0x2D;  //倒挡有驱动无制动力
    else if(lp_car_data->speed == 0)
        lp_car_data->gears = 0x00; //空挡有制动力无驱动
    else if(lp_car_data->speed/10 < 11)
         lp_car_data->gears = 0x01; //1档有驱动力无制动力
    else if(lp_car_data->speed/10 < 26)
         lp_car_data->gears = 0x02; //2档有驱动力无制动力
    else if(lp_car_data->speed/10 < 41)
         lp_car_data->gears = 0x23; //3档有驱动力无制动力
    else if(lp_car_data->speed/10 > 40)
         lp_car_data->gears = 0x04; //4档有驱动力无制动力  
      
    //绝缘电阻----正极绝缘阻值
    lp_car_data->insulance = (g_tCanSave0x0CFF7F03.can_data[6] + g_tCanSave0x0CFF7F03.can_data[7] * 256)/2;
    //加速踏板行程值
    if(lp_car_data->speed/10 > 100)
      lp_car_data->acc_pedal_pos = 100;
    else
      lp_car_data->acc_pedal_pos = lp_car_data->speed/10;
    if(lp_car_data->acc_pedal_pos > 4)          //为了让数据看起来更真实 防止油门踏板和速度一样
      lp_car_data->acc_pedal_pos -= 2;
    
  //***************驱动电机转矩**************/
    para_tmp_u32 = (g_tCanSave0x0CFF7902.can_data[2]|(g_tCanSave0x0CFF7902.can_data[3]<<8));
    if(para_tmp_u32 == 0xFFFF)
        g_stMotorData.motor_msg[0].motor_torque = 0xFFFF;
    else
        g_stMotorData.motor_msg[0].motor_torque = para_tmp_u32 * 25 / 10 - 30000; 
        
  //驱动电机扭矩为正时表示有驱动，为负时有制动
    if(g_stMotorData.motor_msg[0].motor_torque > 20000 && g_stMotorData.motor_msg[0].motor_torque != 0xFFFF)
    {
       if(g_stCarSta.speed/10 > 100)
        g_stCarSta.acc_pedal_pos = 100;
      else
        g_stCarSta.acc_pedal_pos = g_stCarSta.speed/10;
      if(g_stCarSta.acc_pedal_pos > 4)          //为了让数据看起来更真实 防止油门踏板和速度一样
        g_stCarSta.acc_pedal_pos -= 2;
      
        g_stCarSta.gears &= 0x0F;
        g_stCarSta.gears |= 0x20;     //有驱动无制动
        
        g_stCarSta.break_pedal_pos = 0;     //无制动
    }
    else if(g_stMotorData.motor_msg[0].motor_torque < 20000)
    {
    //  lp_car_data->charging_status = 0x02;              //扭矩为负时行驶充电
      //加速踏板行程
      g_stCarSta.acc_pedal_pos = 0;
    //制动踏板行程
      g_stCarSta.break_pedal_pos = 0x65;
      
      g_stCarSta.gears &= 0x0F;
      g_stCarSta.gears |= 0x10;       //有制动力无驱动
    
    }
    else//扭矩为0时无驱动无制动
    {
      
        g_stCarSta.break_pedal_pos = 0;     //制动为0
        g_stCarSta.acc_pedal_pos = 0;       //加速踏板为0
        g_stCarSta.gears &= 0x0F;           //无制动无驱动
    }
 
    return;
}
       
/*
驱动电机数据
*/
void GetMotorData(st_motor_data *data)
{
    uint8 i = 0;
    uint16 para_tmp = 0;
    uint32 para_tmp_u32 = 0;
    st_motor_data * lp_motor_data = data;

    //驱动电机个数
    lp_motor_data->motor_cnt = 1;
    //每个驱动电机数据
    i = lp_motor_data->motor_cnt;
    if(i > 0)
    {
        //驱动电机状态
      if((g_tCanSave0x0CFF1401.can_data[0] & 0x10) == 0)
        lp_motor_data->motor_msg[0].motor_status = 0x03;//关闭状态
      else
      {
        switch((g_tCanSave0x0CFF1401.can_data[0] & 0xC0) >> 6)
        {
        case 0:
          lp_motor_data->motor_msg[0].motor_status = 0x04;    //准备状态
          break;
        default:
          lp_motor_data->motor_msg[0].motor_status = 0x01;    //耗电状态
          break;
          }
      }
        //驱动电机控制器温度
        lp_motor_data->motor_msg[0].ctler_temp = g_tCanSave0x0CFF7B02.can_data[7];
        //驱动电机转速
        para_tmp = g_tCanSave0x0CFF7902.can_data[4] | (g_tCanSave0x0CFF7902.can_data[5] << 8);
        if( (para_tmp != 0xFFFF) )
            lp_motor_data->motor_msg[0].motor_rpm = para_tmp + 8000;
        else
            lp_motor_data->motor_msg[0].motor_rpm = 0xFFFF;
 /********************由于油门和制动踏板要根据驱动电机转矩判断，故驱动电机转矩放在了整车数据中获取*****/
//        //驱动电机转矩
//        para_tmp_u32 = (g_tCanSave0x0CFF7902.can_data[2]|(g_tCanSave0x0CFF7902.can_data[3]<<8));
//        if(para_tmp_u32 == 0xFFFF)
//            lp_motor_data->motor_msg[0].motor_torque = 0xFFFF;
//        else
//            lp_motor_data->motor_msg[0].motor_torque = para_tmp_u32 * 25 / 10 - 30000; 
    
        //驱动电机温度
        lp_motor_data->motor_msg[0].motor_temp = g_tCanSave0x0CFF7B02.can_data[6];
        //电机控制器输入电压
//        lp_motor_data->motor_msg[0].ctler_input_vol = g_tCanSave0x0CFF7E03.can_data[1]+g_tCanSave0x0CFF7E03.can_data[2]*256;
//        if(lp_motor_data->motor_msg[0].ctler_input_vol > 800)
//          lp_motor_data->motor_msg[0].ctler_input_vol = 0xFFFF;
//        else
          lp_motor_data->motor_msg[0].ctler_input_vol =  g_stCarSta.total_vol;
        //电机控制器直流母线电流
        lp_motor_data->motor_msg[0].ctler_dc_i = g_tCanSave0x0CFF7E03.can_data[3] | (g_tCanSave0x0CFF7E03.can_data[4] << 8);
        if(lp_motor_data->motor_msg[0].ctler_dc_i != 0xFFFF)
        { 
              lp_motor_data->motor_msg[0].ctler_dc_i = lp_motor_data->motor_msg[0].ctler_dc_i/10 + 7000;  
        }

    }
  
    
    return;
}

/*
燃料电池数据
*/
void GetFuelData(st_fuel_cell_data *data)
{
    uint8 i = 0;
    st_fuel_cell_data * lp_fuel_data = data;

#if 0   //add for test
    //燃料电池电压
    lp_fuel_data->fuel_cell_vol = 2057;    //燃料电池电压,205.7V
    lp_fuel_data->fuel_cell_i = 325;    //燃料电池电流,32.5A
    lp_fuel_data->fuel_consu_rate = 1047;   //10.47kg/100km
    lp_fuel_data->fuel_temp_cnt = 5;   //5个温度探针
    lp_fuel_data->temp[0] = 79;    //39℃
    lp_fuel_data->temp[1] = 77;    //37℃
    lp_fuel_data->temp[2] = 80;    //40℃
    lp_fuel_data->temp[3] = 79;    //39℃
    lp_fuel_data->temp[4] = 78;    //38℃
    lp_fuel_data->Hsys_max_temp = 80;    //氢系统中最高温度 40℃
    lp_fuel_data->max_temp_order = 3;    //氢系统中最高温度探针代号 3
    lp_fuel_data->Hsys_max_ppm = 32665;    //氢气最高浓度，32665mg/kg
    lp_fuel_data->max_ppm_order = 4;    //氢气最高浓度传感器代号, 4
    lp_fuel_data->Hsys_max_kpa = 873;    //氢气最高压力87.3MPa
    lp_fuel_data->max_kpa_order = 2;    //氢气最高压力传感器代号,2
    lp_fuel_data->DC_DC_status = 0x01;    //高压 DC/DC 状态
#endif
#if 0        //del for test 
    //燃料电池电压
    lp_fuel_data->fuel_cell_vol = get_avaiable_can_bit(CAN_SAVE_FUEL_VOL_NO, 0);
    //燃料电池电流
    lp_fuel_data->fuel_cell_i = get_avaiable_can_bit(CAN_SAVE_FUEL_I_NO, 0);
    //燃料消耗率
    lp_fuel_data->fuel_consu_rate = get_avaiable_can_bit(CAN_SAVE_FUEL_CONSU_RATE_NO, 0);
    //燃料电池温度探针总数
    lp_fuel_data->fuel_temp_cnt = get_avaiable_can_bit(CAN_SAVE_FUEL_PROBE_CNT_NO, 0);
    
    //每个探针温度值
    i = lp_fuel_data->fuel_temp_cnt;
    if(i > 0)
    {
        lp_fuel_data->temp[0] = get_avaiable_can_bit(CAN_SAVE_PROBE1_TEMP_NO, 0);
        i--;
        if(i > 0)
        {
            lp_fuel_data->temp[1] = get_avaiable_can_bit(CAN_SAVE_PROBE2_TEMP_NO, 0);
            i--;
            if(i > 0)
            {
                lp_fuel_data->temp[2] = get_avaiable_can_bit(CAN_SAVE_PROBE3_TEMP_NO, 0);
                i--;
                if(i > 0)
                {
                    lp_fuel_data->temp[3] = get_avaiable_can_bit(CAN_SAVE_PROBE4_TEMP_NO, 0);
                    i--;
                    if(i > 0)
                    {
                        lp_fuel_data->temp[4] = get_avaiable_can_bit(CAN_SAVE_PROBE5_TEMP_NO, 0);
                    }
                }
            }
        }
    }
    //氢系统中最高温度
    lp_fuel_data->Hsys_max_temp = get_avaiable_can_bit(CAN_SAVE_H_TMAX_NO, 0);
    //氢系统中最高温度探针代号
    lp_fuel_data->max_temp_order = get_avaiable_can_bit(CAN_SAVE_H_TMAX_PROBE_NO, 0);
    //氢气最高浓度
    lp_fuel_data->Hsys_max_ppm = get_avaiable_can_bit(CAN_SAVE_H_PPM_MAX_NO, 0);
    //氢气最高浓度传感器代号
    lp_fuel_data->max_ppm_order = get_avaiable_can_bit(CAN_SAVE_H_PPM_MAX_PROBE_NO, 0);
    //氢气最高压力
    lp_fuel_data->Hsys_max_kpa = get_avaiable_can_bit(CAN_SAVE_H_PA_MAX_NO, 0);
    //氢气最高压力传感器代号
    lp_fuel_data->max_kpa_order = get_avaiable_can_bit(CAN_SAVE_H_PA_MAX_PROBE_NO, 0);
    //高压 DC/DC 状态
    lp_fuel_data->DC_DC_status = get_avaiable_can_bit(CAN_SAVE_DCDC_STA_NO, 0);
#endif
    return;
}

/*
发动机数据
*/
void GetEngineData(st_engine_data *data)
{
    uint8 i = 0;
    st_engine_data * lp_engine_data = data;

#if 0   //add for test
    lp_engine_data->engine_status = 0x01;  //发动机状态,启动状态
    lp_engine_data->axle_rpm = 20374;   //曲轴转速,20374r/min
    lp_engine_data->fuel_consu_rate = 2748;   //燃料消耗率，27.48L/100km
#endif
#if 0        //del for test 
    //发动机状态
    lp_engine_data->engine_status = get_avaiable_can_bit(CAN_SAVE_ENGINE_STA_NO, 0);
    //曲轴转速
    lp_engine_data->axle_rpm = get_avaiable_can_bit(CAN_SAVE_FUEL_I_NO, 0);
    //燃料消耗率
    lp_engine_data->fuel_consu_rate = get_avaiable_can_bit(CAN_SAVE_OIL_CONSU_RATE_NO, 0);
#endif
    return;
}


#endif