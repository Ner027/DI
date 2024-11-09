/*
 * tca9548.cpp
 *
 *  Created on: Oct 21, 2024
 *      Author: mariolima
 */
#include "tca9548.h"
#include "i2c.h"
#include <stdint.h>

#define TCA_ADDRESS	0x70 << 1

static uint8_t channel = 0;


int8_t openChannel(uint8_t ch)
{
    channel = --ch;

    uint8_t buffer = 0x00;
    buffer = 1 << channel;    //mask to prepare the comand to open the respective channel

    HAL_StatusTypeDef status;
    status = HAL_I2C_Master_Transmit(&hi2c2, TCA_ADDRESS, &buffer, 1, HAL_MAX_DELAY);
    if (status != HAL_OK)
    {
        return -1;
    }
    return 0;
}


uint8_t getCurrentMuxChannel()
{
	return channel;
}


int8_t closeChannel()
{
    channel = 0;

    uint8_t buffer = 0x00;

    HAL_StatusTypeDef status;
    status = HAL_I2C_Master_Transmit(&hi2c2, TCA_ADDRESS, &buffer, 1, HAL_MAX_DELAY);
    if (status != HAL_OK)
    {
        return -1;
    }
    return 0;
}
