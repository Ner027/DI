#ifndef _TCA9548_H_
#define _TCA9548_H_

#include <stdint.h>

int8_t openChannel(uint8_t ch);
uint8_t getCurrentMuxChannel();

int8_t closeChannel();


#endif /* SRC_TCA9548_CPP_ */
