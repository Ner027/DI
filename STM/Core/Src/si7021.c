#include "si7021.h"
#include "i2c.h"

#define SI7021_ADDRESS    0x40 << 1
#define TEMPERATURE_REG   0xE3
#define HUMIDITY_REG      0xE5


/// @brief function to read the humidity
/// @return humidity value (float)
float readHumidity(si7021_st* sensor)
{
    uint8_t humReg = HUMIDITY_REG;
    HAL_StatusTypeDef status;

    status = HAL_I2C_Master_Transmit(&hi2c2, SI7021_ADDRESS, &humReg, 1, HAL_MAX_DELAY);
    if (status != HAL_OK)
    {
        return -1.0;
    }

    HAL_Delay(100);

    uint8_t humidityData[2];
    status = HAL_I2C_Master_Receive(&hi2c2, SI7021_ADDRESS,  humidityData, 2, HAL_MAX_DELAY);
    if (status != HAL_OK)
    {
        return -2.0;
    }

    uint16_t humidityRaw = (humidityData[0] << 8) | humidityData[1];
    sensor->humidity = (float)(((125.0 * (float)humidityRaw) / 65536.0) - 6.0);

    return sensor->humidity;
}

/// @brief function to read the temperature
/// @return temperature value (float)
float readTemperature(si7021_st* sensor)
{
    uint8_t temReg = TEMPERATURE_REG;

    HAL_StatusTypeDef status = HAL_I2C_Master_Transmit(&hi2c2, SI7021_ADDRESS, &temReg, 1, HAL_MAX_DELAY);
    if (status != HAL_OK)
    {
        return -1.0;
    }

    HAL_Delay(100);

    uint8_t temperatureData[2];
    status = HAL_I2C_Master_Receive(&hi2c2, SI7021_ADDRESS, temperatureData, 2, HAL_MAX_DELAY);
    if (status != HAL_OK)
    {
        return -2.0;
    }

    uint16_t temperatureRaw = (temperatureData[0] << 8) | temperatureData[1];
    sensor->temperature = (float)(((175.72 * temperatureRaw) / 65536.0) - 46.85);

    return sensor->temperature;
}



float getTemperature(si7021_st* sensor)
{
	return sensor->temperature;
}



float getHumidity(si7021_st* sensor)
{
	return sensor->humidity;
}



void setChannel(si7021_st* sensor, uint8_t ch)
{
	sensor->channel = ch;
}


uint8_t getChannel(si7021_st* sensor)
{
	return sensor->channel;
}
