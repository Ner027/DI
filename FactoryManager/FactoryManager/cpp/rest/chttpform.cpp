#include "chttpform.h"
#include <sstream>
#include <QVariant>
#include <QDebug>

#define FORM_DATA_HEADER "form-data; name="

CHttpForm::CHttpForm()
{
    m_multiPart = new QHttpMultiPart(QHttpMultiPart::FormDataType);
}

CHttpForm::~CHttpForm()
{
    for(auto ptr : m_allocatedParts)
    {
        delete ptr;
        ptr = nullptr;
    }

    delete m_multiPart;
}

CHttpForm &CHttpForm::addField(std::string fieldName, QByteArray fieldValue)
{
    QHttpPart* newPart = new QHttpPart;
    m_allocatedParts.push_back(newPart);

    std::stringstream ss;

    ss << FORM_DATA_HEADER << "\"" << fieldName << "\"";

    newPart->setHeader(QNetworkRequest::ContentDispositionHeader, QVariant(QString::fromStdString(ss.str())));
    newPart->setBody(fieldValue);

    m_multiPart->append(*newPart);

    return *this;
}

QHttpMultiPart* CHttpForm::getMultiPart() const
{
    return m_multiPart;
}
