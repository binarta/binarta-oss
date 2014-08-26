package sdkx.rest.client

import client.RestClient
import client.factory.RestClientFactory
import spock.lang.Specification

class RestBinartaClientSpec extends Specification {
    def restClient = GroovyMock(RestClient)
    RestBinartaClient client

    def capturedBaseUrl

    def setup() {
        RestBinartaClient.mergeLocalConfig = false
        RestClientFactory.metaClass.static.clientFor = { String baseUrl ->
            capturedBaseUrl = baseUrl
            return restClient
        }
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
}
