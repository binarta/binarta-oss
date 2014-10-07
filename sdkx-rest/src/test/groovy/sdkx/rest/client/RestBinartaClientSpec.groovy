package sdkx.rest.client

import client.FakeRestClient
import client.factory.RestClientFactory
import sdkx.inmemory.client.BaseClientSpec

import static com.google.common.io.BaseEncoding.base64
import static groovyx.net.http.ContentType.JSON

class RestBinartaClientSpec extends BaseClientSpec {
    def restClient = new FakeRestClient()
    RestBinartaClient client

    def capturedBaseUrl

    def setup() {
        RestBinartaClient.mergeLocalConfig = false
        RestClientFactory.metaClass.static.clientFor = { String baseUrl ->
            capturedBaseUrl = baseUrl
            return restClient
        }
        client = new RestBinartaClient(namespace: 'N', username: 'U', password: 'P', env: 'dev')
    }

    def cleanup() {
        GroovySystem.metaClassRegistry.removeMetaClass RestClientFactory
    }

    def "base url is resolved by environment"() {
        when:
        client = new RestBinartaClient(namespace: 'N', username: 'U', password: 'P', env: env)

        then:
        url == capturedBaseUrl

        where:
        env    | url
        'dev'  | 'http://api.binarta.dev.thinkerit.be/'
        null   | 'http://api.binarta.dev.thinkerit.be/'
        'demo' | 'http://api.binarta.demo.thinkerit.be/'
        'prod' | 'https://api.binarta.com/'
    }

    void assertRequest() {
        assert restClient.requestHeaders == ['X-Namespace': namespace, Authorization: "Binarta ${base64().encode("$namespace:U:P".bytes)}"]
        assert restClient.requestContentType == JSON as String
    }

    void assertEntityAdded(args) {
        assertRequest()
        assert restClient.requestMethod == 'PUT'
        assert restClient.requestResource == "api/entity/$args.entity"
        assert restClient.requestBody == args.payload
    }
}
