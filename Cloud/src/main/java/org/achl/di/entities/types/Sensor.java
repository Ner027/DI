package org.achl.di.entities.types;

import org.achl.di.entities.JsonField;
import org.achl.di.entities.SerializableField;
import org.achl.di.entities.SerializableObject;
import org.achl.di.enums.SerializableFieldType;

public class Sensor extends SerializableObject
{
    @JsonField(name = "sensId")
    @SerializableField(name = "sens_id", type = SerializableFieldType.ENCODE)
    private final long m_sensId;

    @JsonField(name = "lmId")
    @SerializableField(name = "parent_lm_id", type = SerializableFieldType.ENC_DEC)
    private long m_parentLmId;

    @JsonField(name = "sensName")
    @SerializableField(name = "sens_name", type = SerializableFieldType.ENC_DEC, encode = true)
    private String m_sensName;

    @JsonField(name = "sensType")
    @SerializableField(name = "sens_type", type = SerializableFieldType.ENC_DEC)
    private int m_sensType;

    @JsonField(name = "sensStatus")
    @SerializableField(name = "sens_status", type = SerializableFieldType.ENC_DEC)
    private int m_sensStatus;

    public Sensor()
    {
        m_sensId = -1;
        m_parentLmId = -1;
    }

    public boolean setName(String newName)
    {
        if (newName.length() < 4)
            return false;

        m_sensName = newName;

        return true;
    }

    public long getId()
    {
        return m_sensId;
    }

    public void setSensorType(int newType)
    {
        m_sensType = newType;
    }

    public void setSensorStatus(int newStatus)
    {
        m_sensStatus = newStatus;;
    }

    public void setParentId(long parentId)
    {
        m_parentLmId = parentId;
    }

    @Override
    public String getInsertionQueryName()
    {
        return "InsertSensor";
    }

    @Override
    public String getInsertionReadBackQueryName(String readBackField)
    {
        return "InsertSensorReadBack_" + readBackField;
    }

    @Override
    public String getUpdateQueryName()
    {
        return "UpdateSensor";
    }

    @Override
    public String getLoadQueryName(String fieldName)
    {
        return "GetSensorBy_" + fieldName;
    }

    @Override
    public String getDeleteQueryName(String fieldName)
    {
        return "DeleteSensorBy_" + fieldName;
    }
}
