package sdkx.rest.client

import client.RestClient

class FakeResponse implements RestClient.Response {
    int status
    String contentType
    def data
}
