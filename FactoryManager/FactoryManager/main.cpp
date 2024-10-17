#include <QCoreApplication>
#include <thread>
#include "cpp/rest/cnetworkmanager.h"

static int randomInRange(int lBound, int hBound)
{
    return (rand() % (hBound - lBound)) + lBound;
}

int main(int argc, char *argv[])
{
    QCoreApplication app(argc, argv);
    CNetworkManager& networkManager = CNetworkManager::getInstance();
    srand(0);

    CHttpRequest httpReq("/sens/data", HttpVerb_et::POST);

    int sensorId = 1;
    while (true)
    {
        int rng = randomInRange(100, 500);
        httpReq.m_formData.addField("logVal", QString::number(rng).toStdString().c_str())
                .addField("sensId", QString::number(sensorId).toStdString().c_str());
        networkManager.executeRequest(httpReq);

        std::this_thread::sleep_for(std::chrono::milliseconds(1000));
    }

    return app.exec();
}
