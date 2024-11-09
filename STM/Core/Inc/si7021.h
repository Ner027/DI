/*
 * si7021.h
 *
 *  Created on: Oct 21, 2024
 *      Author: mariolima
 */

#ifndef INC_SI7021_H_
#define INC_SI7021_H_

#include "stdint.h"


typedef struct{
	uint8_t channel;
	float temperature;
	float humidity;
	
}si7021_st;


float readHumidity(si7021_st* sensor);
float readTemperature(si7021_st* sensor);

float getTemperature(si7021_st* sensor);
float getHumidity(si7021_st* sensor);

void setChannel(si7021_st* sensor, uint8_t ch);
uint8_t getChannel(si7021_st* sensor);

#endif /* INC_SI7021_H_ */
