package org.achl.di.enums;

public enum RequestErrorCause
{
    INVALID_FACTORY_NAME            (-1),
    INVALID_FACTORY_EXISTS          (-2),
    INVALID_FACTORY_ID              (-3),
    INVALID_LM_NAME                 (-4),
    INVALID_LM_HWID                 (-5),
    INVALID_LM_EXISTS               (-6),
    INVALID_LM_ID                   (-7),
    INVALID_SENS_NAME               (-8),
    INVALID_SENS_TYPE               (-9),
    INVALID_SENS_ID                 (-10),
    INVALID_LOG_VAL                 (-11),
    INVALID_START_TIME              (-12),
    INVALID_END_TIME                (-13);

    private final int m_value;
    RequestErrorCause(int value)
    {
        m_value = value;
    }

    public int getValue()
    {
        return m_value;
    }
}
