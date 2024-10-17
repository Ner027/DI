#ifndef CONTROLBOX_CHTTPFORM_H
#define CONTROLBOX_CHTTPFORM_H

/***********************************************************************************************************************
 * Includes
 ******************************************************************************************************************++**/
#include <QHttpPart>
#include <QHttpMultiPart>
#include <vector>

class CHttpForm
{
public:
    /// \brief Default Constructor
    CHttpForm();
    ~CHttpForm();

    /// \brief This method allows to add a new field to a multipart request form
    /// \param fieldName Name of the form field
    /// \param fieldValue Value of the form field
    /// \return Reference to this object to allows its use in a builder pattern style
    CHttpForm& addField(std::string fieldName, QByteArray fieldValue);

    /// \brief This method allows to retrieve the assembled multipart form
    /// \return Pointer to built multipart form
    [[nodiscard]] QHttpMultiPart* getMultiPart() const;
private:
    std::vector<QHttpPart*> m_allocatedParts;
    QHttpMultiPart* m_multiPart;
};


#endif
