#ifndef CONTROLBOX_CNETWORKMANAGER_H
#define CONTROLBOX_CNETWORKMANAGER_H

/***********************************************************************************************************************
 * Includes
 ******************************************************************************************************************++**/
#include <QObject>
#include <QNetworkAccessManager>
#include <QNetworkRequest>
#include <QNetworkReply>
#include <mutex>
#include <condition_variable>
#include "chttpform.h"
#include "chttprequest.h"
#include "networktypes.h"

/***********************************************************************************************************************
 * Constants
 ******************************************************************************************************************++**/
static std::string g_verbLut[] =
                        {"GET",
                         "HEAD",
                         "POST",
                         "PUT",
                         "DELETE",
                         "CONNECT",
                         "OPTIONS",
                         "TRACE",
                         "PATCH"};

class CNetworkManager
{
public:
    ~CNetworkManager();

    /// \brief This method allows to return a singleton instance of the Network Manager
    /// \return Pointer to Network Manager singleton instance
    static CNetworkManager& getInstance();

    /// \brief This method executes an HTTP Request
    /// \param httpRequest HTTP Request to be executed
    /// \return -EINVAL in passed HTTP Request is invalid, 0 on success
    int executeRequest(CHttpRequest& httpRequest);
private:
    /// \brief Default constructor
    /// \note Made private to only allow instantiation from singleton
    CNetworkManager();
    static CNetworkManager m_instance;
    QNetworkAccessManager* m_networkAccessManager;
    QNetworkRequest* m_request;
    std::mutex m_requestMtx;
};

#endif
