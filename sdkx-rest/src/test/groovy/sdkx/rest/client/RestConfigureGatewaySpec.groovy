package sdkx.rest.client

import client.FakeRestClient
import client.factory.RestClientFactory
import sdkx.inmemory.client.ConfigureGatewaySpec

import static com.google.common.io.BaseEncoding.base64
import static groovyx.net.http.ContentType.JSON

class RestConfigureGatewaySpec extends ConfigureGatewaySpec {
    def restClient = new FakeRestClient()
    RestBinartaClient client

    def setup() {
        RestBinartaClient.mergeLocalConfig = false
        RestClientFactory.metaClass.static.clientFor = { String baseUrl ->
            restClient
        }
        client = new RestBinartaClient(namespace: namespace, username: 'U', password: 'P')
    }
    def cleanup() {
        GroovySystem.metaClassRegistry.removeMetaClass RestClientFactory
    }

    void gatewayWasConfigured(args) {
        assert restClient.requestMethod == 'POST'
        assert restClient.requestResource == 'config'
        assert restClient.requestBody == [id:"entity.gateway.$input.entity", value: input.args]
        assert restClient.requestHeaders == ['X-Namespace': namespace, Authorization: "Binarta ${base64().encode("$namespace:U:P".bytes)}"]
        assert restClient.requestContentType == JSON as String
    }
}
