/* USER CODE BEGIN Header */
/**
  ******************************************************************************
  * File Name          : freertos.c
  * Description        : Code for freertos applications
  ******************************************************************************
  * @attention
  *
  * Copyright (c) 2024 STMicroelectronics.
  * All rights reserved.
  *
  * This software is licensed under terms that can be found in the LICENSE file
  * in the root directory of this software component.
  * If no LICENSE file comes with this software, it is provided AS-IS.
  *
  ******************************************************************************
  */
/* USER CODE END Header */

/* Includes ------------------------------------------------------------------*/
#include "FreeRTOS.h"
#include "task.h"
#include "main.h"
#include "cmsis_os.h"
#include "open62541.h"

/* Private includes ----------------------------------------------------------*/
/* USER CODE BEGIN Includes */
#include "si7021.h"
#include "tca9548.h"

#define SAMPLING_TIME 	1000
#define SI7021_NUMBER	2
#define SENSORS_NUMBER	4

typedef struct
{
    uint32_t sensorId;
    float sensorData;
    float (*readFunc)(si7021_st* sensor);
    si7021_st* sensorPtr;
}sensor_data_st;


void initSensorData(sensor_data_st* sensorData, uint32_t id, float (*readFunc)(si7021_st*), si7021_st* sensorPtr) 
{
    sensorData->sensorId = id;
    sensorData->readFunc = readFunc;
    sensorData->sensorPtr = sensorInstance;
}


float readSensorData(sensor_data_st* sensorData) 
{
    return sensorData->readFunc(sensorData->sensorPtr);
}

/* USER CODE END Includes */

/* Private typedef -----------------------------------------------------------*/
/* USER CODE BEGIN PTD */

/* USER CODE END PTD */

/* Private define ------------------------------------------------------------*/
/* USER CODE BEGIN PD */

/* USER CODE END PD */

/* Private macro -------------------------------------------------------------*/
/* USER CODE BEGIN PM */

/* USER CODE END PM */

/* Private variables ---------------------------------------------------------*/
/* USER CODE BEGIN Variables */

/* USER CODE END Variables */
/* Definitions for defaultTask */
osThreadId_t defaultTaskHandle;
const osThreadAttr_t defaultTask_attributes = {
        .name = "defaultTask",
        .stack_size = 8196 * 4,
        .priority = (osPriority_t) osPriorityNormal,
};

/* Private function prototypes -----------------------------------------------*/
/* USER CODE BEGIN FunctionPrototypes */

/* USER CODE END FunctionPrototypes */

void StartDefaultTask(void *argument);

extern void MX_LWIP_Init(void);

void MX_FREERTOS_Init(void); /* (MISRA C 2004 rule 8.1) */

/**
  * @brief  FreeRTOS initialization
  * @param  None
  * @retval None
  */
void MX_FREERTOS_Init(void)
{
    /* USER CODE BEGIN Init */

    /* USER CODE END Init */

    /* USER CODE BEGIN RTOS_MUTEX */
    /* add mutexes, ... */
    /* USER CODE END RTOS_MUTEX */

    /* USER CODE BEGIN RTOS_SEMAPHORES */
    /* add semaphores, ... */
    /* USER CODE END RTOS_SEMAPHORES */

    /* USER CODE BEGIN RTOS_TIMERS */
    /* start timers, add new ones, ... */
    /* USER CODE END RTOS_TIMERS */

    /* USER CODE BEGIN RTOS_QUEUES */
    /* add queues, ... */
    /* USER CODE END RTOS_QUEUES */

    /* Create the thread(s) */
    /* creation of defaultTask */
    defaultTaskHandle = osThreadNew(StartDefaultTask, NULL, &defaultTask_attributes);

    /* USER CODE BEGIN RTOS_THREADS */
    /* add threads, ... */
    /* USER CODE END RTOS_THREADS */

    /* USER CODE BEGIN RTOS_EVENTS */
    /* add events, ... */
    /* USER CODE END RTOS_EVENTS */

}

/* USER CODE BEGIN Header_StartDefaultTask */
/**
  * @brief  Function implementing the defaultTask thread.
  * @param  argument: Not used
  * @retval None
  */
/* USER CODE END Header_StartDefaultTask */
void StartDefaultTask(void *argument)
{
    /* init code for LWIP */
    MX_LWIP_Init();

    osDelay(3000);

    UA_Client *pClient = UA_Client_new();
    UA_ClientConfig_setDefault(UA_Client_getConfig(pClient));

    UA_StatusCode retCode = UA_Client_connect(pClient, "opc.tcp://rasp.mshome.net:4840");
    if (retCode != UA_STATUSCODE_GOOD)
    {
        UA_Client_delete(pClient);
        return;
    }

    UA_Variant value;
    UA_Variant_init(&value);

    UA_NodeId nodeId = UA_NODEID_STRING(1, "sensor.data");
    retCode = UA_Client_readValueAttribute(pClient, nodeId, &value);
    UA_Int32 retInt;
    retInt = *(UA_Int32 *) value.data;

    si7021_st si7021Sensors[SI7021_NUMBER]; 
    setChannel(&si7021Sensors[0], 1);
    setChannel(&si7021Sensors[1], 2);
    
    sensor_data_st systemSensors[SENSORS_NUMBER];
    
    //bit 16-12   - manufactoring line
    //bit 11-8	  - type (00 tempertaure / 01 humidity)
    //last 8 bits - index
    initSensorData(&systemSensors[0], 0x00000000, readTemperature, &si7021Sensors[0]);
    initSensorData(&systemSensors[1], 0x00000100, readHumidity, &si7021Sensors[0]);
    initSensorData(&systemSensors[2], 0x00000001, readTemperature, &si7021Sensors[1]);
    initSensorData(&systemSensors[3], 0x00000101, readHumidity, &si7021Sensors[1]);  
    

    /* USER CODE BEGIN StartDefaultTask */
    /* Infinite loop */
    for (;;)
    {
    	for(uint8_t i = 0; i < SENSORS_NUMBER; i++)
    	{
    		openChannel(getChannel(systemSensors[i].sensorPtr));
    		systemSensors[i].sensorData = readSensorData(&systemSensors[i]);

        	printf("Writing: %f\n", systemSensors[i].sensorData);
        
        	UA_Variant_setScalar(&value, &sensorData, &UA_TYPES[UA_TYPES_UINT64]);
        	UA_Client_writeValueAttribute(pClient, nodeId, &value);
    	}

        osDelay(SAMPLING_TIME);
    }
    /* USER CODE END StartDefaultTask */
}

/* Private application code --------------------------------------------------*/
/* USER CODE BEGIN Application */

/* USER CODE END Application */

