#include "cnetworkmanager.h"
#include <sstream>
#include <QUrl>
#include <iostream>
#include <thread>
#include <QEventLoop>

#define CLOUD_URL "http://localhost:8080"

CNetworkManager& CNetworkManager::getInstance()
{
    static CNetworkManager selfInstance;
    return selfInstance;
}

CNetworkManager::~CNetworkManager()
{
    delete m_networkAccessManager;
    delete m_request;
}

CNetworkManager::CNetworkManager()
{
    m_request = new QNetworkRequest;
    m_networkAccessManager = nullptr;
}

int CNetworkManager::executeRequest(CHttpRequest& httpRequest)
{
    if (httpRequest.getVerb() >= HttpVerb_et::MAX)
        return -EINVAL;

    std::lock_guard lockGuard(m_requestMtx);

    std::stringstream urlBuilder;
    urlBuilder << CLOUD_URL << httpRequest.getEndpoint();

    QUrl fullUrl(QString::fromStdString(urlBuilder.str()));

    m_request->setUrl(fullUrl);

    QEventLoop loop;

    if (m_networkAccessManager)
    {
        delete m_networkAccessManager;
        m_networkAccessManager = nullptr;
    }

    m_networkAccessManager = new QNetworkAccessManager;

    httpRequest.m_reply = m_networkAccessManager->sendCustomRequest(*m_request,
                                                       g_verbLut[httpRequest.getVerb()].c_str(),
                                                       httpRequest.m_formData.getMultiPart());

    QObject::connect(httpRequest.m_reply, SIGNAL(finished()), &loop, SLOT(quit()));
    loop.exec();

    return 0;
}
