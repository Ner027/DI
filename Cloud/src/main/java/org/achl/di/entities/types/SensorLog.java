package org.achl.di.entities.types;

import org.achl.di.entities.JsonField;
import org.achl.di.entities.SerializableField;
import org.achl.di.entities.SerializableObject;
import org.achl.di.enums.SerializableFieldType;

public class SensorLog extends SerializableObject
{
    @SerializableField(name = "log_id", type = SerializableFieldType.ENCODE)
    private final long m_logId;

    @SerializableField(name = "parent_sens_id", type = SerializableFieldType.ENC_DEC)
    private long m_parentSensorId;

    @JsonField(name = "logTimestamp")
    @SerializableField(name = "log_timestamp", type = SerializableFieldType.ENC_DEC)
    private long m_logTimestamp;

    @JsonField(name = "logValue")
    @SerializableField(name = "log_value", type = SerializableFieldType.ENC_DEC)
    private double m_logValue;

    public SensorLog()
    {
        m_logId = -1;
        m_parentSensorId = -1;
    }

    @Override
    public String getInsertionQueryName()
    {
        return "InsertLog";
    }

    public void setParentId(long newId)
    {
        m_parentSensorId = newId;
    }

    public void setValue(float newVal)
    {
        m_logValue = newVal;
    }

    public void refreshTimestamp()
    {
        m_logTimestamp = System.currentTimeMillis() / 1000;
    }

    @Override
    public String getInsertionReadBackQueryName(String readBackField)
    {
        return "";
    }

    @Override
    public String getUpdateQueryName()
    {
        return "";
    }

    @Override
    public String getLoadQueryName(String fieldName)
    {
        return "GetLogBy_" + fieldName;
    }

    @Override
    public String getDeleteQueryName(String fieldName)
    {
        return "DeleteLogBy_" + fieldName;
    }
}
