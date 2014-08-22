package client

class FakeRestClient implements RestClient {
    def onGetRequest = {}
    def onPutRequest = {}
    def onPostRequest = {}
    def onDeleteRequest = {}

    String requestMethod
    String requestResource
    String requestContentType
    Object requestBody
    Map requestHeaders
    Map query

    int responseStatus
    def responseData
    private def response = [getStatus: {responseStatus}, getData: {responseData}] as RestClient.Response
    String username
    String password

    @Override
    RestClient.Response get(Map request) {
        captureRequestInfo('GET', request)
        swallowExceptions {
            onGetRequest(request)
        }
        return response
    }

    private void swallowExceptions(cl) {
        try {
            cl()
        } catch (Throwable t) {
            assert false: "unhandled exception! [$t]"
        }
    }

    private void captureRequestInfo(String method, Map map) {
        requestBody = map?.body
        requestMethod = method
        requestResource = map?.path
        requestContentType = map?.requestContentType
        requestHeaders = map?.headers ?: [:]
        query = map?.query ?: [:]
    }

    @Override
    RestClient.Response post(Map request) {
        captureRequestInfo('POST', request)
        swallowExceptions {
            onPostRequest(request)
        }
        return response
    }

    @Override
    RestClient.Response put(Map request) {
        captureRequestInfo('PUT', request)
        swallowExceptions {
            onPutRequest(request)
        }
        return response
    }

    @Override
    RestClient.Response delete(Map request) {
        captureRequestInfo('DELETE', request)
        swallowExceptions {
            onDeleteRequest(request)
        }
        return response
    }

    @Override
    void setBasicCredentials(String username, String password) {
        this.username = username
        this.password = password
    }
}
