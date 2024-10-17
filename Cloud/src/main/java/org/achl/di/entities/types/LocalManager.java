package org.achl.di.entities.types;

import org.achl.di.entities.JsonField;
import org.achl.di.entities.SerializableField;
import org.achl.di.entities.SerializableObject;
import org.achl.di.enums.SerializableFieldType;

public class LocalManager extends SerializableObject
{
    @JsonField(name = "lmId")
    @SerializableField(name = "lm_id", type = SerializableFieldType.ENCODE)
    private final long m_lmId;

    @JsonField(name = "factoryId")
    @SerializableField(name = "parent_factory_id", type = SerializableFieldType.ENC_DEC)
    private long m_parentId;

    @JsonField(name = "lmName")
    @SerializableField(name = "lm_name", type = SerializableFieldType.ENC_DEC, encode = true)
    private String m_lmName;

    @JsonField(name = "hwId")
    @SerializableField(name = "hw_id", type = SerializableFieldType.ENC_DEC, encode = true)
    private String m_hwId;

    public LocalManager()
    {
        m_lmId = -1;
        m_parentId = -1;
    }

    public long getId()
    {
        return m_lmId;
    }

    public long getParentId()
    {
        return m_parentId;
    }

    public String getName()
    {
        return m_lmName;
    }

    public String getHwId()
    {
        return m_hwId;
    }

    public void setParentId(long parentId)
    {
        m_parentId = parentId;
    }

    public boolean setHwId(String newHwId)
    {
        if (newHwId.length() != 17)
            return false;

        m_hwId = newHwId;
        return true;
    }

    public boolean setName(String newName)
    {
        if (newName.length() < 4)
            return false;

        m_lmName = newName;
        return true;
    }

    @Override
    public String getInsertionQueryName()
    {
        return "InsertLocalManager";
    }

    @Override
    public String getUpdateQueryName()
    {
        return "UpdateLocalManager";
    }

    @Override
    public String getLoadQueryName(String fieldName)
    {
        return "GetLocalManagerBy_" + fieldName;
    }

    @Override
    public String getDeleteQueryName(String fieldName)
    {
        return "DeleteLocalManagerBy_" + fieldName;
    }

    @Override
    public String getInsertionReadBackQueryName(String readBackField)
    {
        return "";
    }
}
