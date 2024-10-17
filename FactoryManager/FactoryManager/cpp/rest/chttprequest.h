#ifndef CONTROLBOX_CHTTPREQUEST_H
#define CONTROLBOX_CHTTPREQUEST_H

#include <QNetworkReply>
#include <QJsonObject>
#include "chttpform.h"
#include "networktypes.h"

class CHttpRequest
{
public:
    /// \brief This constructor build an HTTP request based on an endpoint and an HTTP action verb
    /// \param endpoint HTTP endpoint
    /// \param verb HTTP action verb
    CHttpRequest(const std::string& endpoint, HttpVerb_et verb);

    /// \brief This method executes this request
    /// \return Returns the result obtained from the Network Manager
    [[nodiscard]] int execute();

    /// \brief Getter for request endpoint
    /// \return String representation of the request endpoint
    [[nodiscard]] std::string getEndpoint() const;

    /// \brief Getter for request ver
    /// \return Enum representing the HTTP action verb
    [[nodiscard]] HttpVerb_et getVerb() const;

    /// \brief This method returns the HTTP status of the request after execution
    /// \return Enum representing the HTTP return code
    [[nodiscard]] HttpStatusCode_et getStatus() const;

    /// \brief This method allows to retrieve the JSON data obtained by the request
    /// \param jsonObject JSON Object where data should be returned to
    /// \return -ENODATA if no JSON data is present on the request response, 0 on success
    [[nodiscard]] int getJsonData(QJsonObject& jsonObject) const;
    QNetworkReply* m_reply;
    CHttpForm m_formData;
private:
    bool m_ready;
    std::string m_endpoint;
    HttpVerb_et m_verb;
};

#endif
