package org.achl.di.enums;

public enum SensorType
{
    Temperature (0),
    Humidity (1),
    IO(2),
    Voltage(3),
    Current(4),
    Power(5),
    Energy(6),
    PowerFactor(7),
    Max (8);
    private final int m_value;
    SensorType(int value)
    {
        m_value = value;
    }

    public int getValue()
    {
        return m_value;
    }
}
