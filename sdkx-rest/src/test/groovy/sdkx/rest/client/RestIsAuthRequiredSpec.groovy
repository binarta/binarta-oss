package sdkx.rest.client

import client.RestClient
import client.factory.RestClientFactory
import sdk.client.BinartaClient
import sdkx.inmemory.client.IsAuthRequiredSpec

import static com.google.common.io.BaseEncoding.base64
import static groovyx.net.http.ContentType.HTML

class RestIsAuthRequiredSpec extends IsAuthRequiredSpec {
    def restClient = GroovyMock(RestClient)
    RestBinartaClient client

    def setup() {
        RestClientFactory.metaClass.static.clientFor = { String baseUrl ->
            return restClient
        }
        client = new RestBinartaClient(namespace: 'N', username: 'U', password: 'P')
    }

    def cleanup() {
        GroovySystem.metaClassRegistry.removeMetaClass RestClientFactory
    }

    def authenticationWasRegistered(args) {
        restClient.get([
            path   : "/config/requires.authentication.$args.entity.$args.action",
            query: [type:'boolean'],
            headers: [
                'X-Namespace': 'N',
                Authorization: "Binarta ${base64().encode("$namespace:U:P".bytes)}"
            ],
            requestContentType: HTML
        ]) >> new FakeResponse(status: 200, data: [value:'true'])

    }

    def "registering auth requirement throws unauthorized"() {
        given:
        restClient.get(_) >> new FakeResponse(status: 401)

        when:
        client.isAuthenticationRequired(entity: 'E', action: 'C')

        then:
        thrown BinartaClient.Unauthorized
    }

    def "registering auth req throws forbidden"() {
        given:
        restClient.get(_) >> new FakeResponse(status: 403)

        when:
        client.isAuthenticationRequired(entity: 'E', action: 'C')

        then:
        thrown BinartaClient.Forbidden
    }

    def "handle 404"() {
        given:
        restClient._ >> new FakeResponse(status:404)

        when:
        client.isAuthenticationRequired([:])

        then:
        thrown BinartaClient.NotFound
    }

    def "registering auth req throws error"() {
        given:
        restClient.get(_) >> new FakeResponse(status: 500, data: 'data')

        when:
        client.isAuthenticationRequired([:])

        then:
        def e = thrown(BinartaClient.Error)
        e.message == 'data'
    }
}
