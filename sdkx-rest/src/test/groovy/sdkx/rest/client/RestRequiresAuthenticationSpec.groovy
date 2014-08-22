package sdkx.rest.client

import client.RestClient
import client.factory.RestClientFactory
import sdk.client.BinartaClient
import sdkx.inmemory.client.RequiresAuthenticationSpec

import static com.google.common.io.BaseEncoding.base64
import static groovyx.net.http.ContentType.JSON

class RestRequiresAuthenticationSpec extends RequiresAuthenticationSpec {
    def restClient = GroovyMock(RestClient)
    RestBinartaClient client

    def setup() {
        RestClientFactory.metaClass.static.clientFor = {String baseUrl->
            restClient
        }
        client = new RestBinartaClient(namespace:namespace, username:'U', password:'P')
    }

    def cleanup() {
        GroovySystem.metaClassRegistry.removeMetaClass RestClientFactory
    }

    def authenticationRequirementWasRegistered(args) {
        1 * restClient.post([
            path              : '/config',
            body              : [
                id   : "requires.authentication.$args.entity.$args.action",
                value: true
            ],
            headers           : [
                'X-Namespace': args.namespace,
                Authorization: "Binarta ${base64().encode("$args.namespace:U:P".bytes)}"
            ],
            requestContentType: JSON
        ]) >> new FakeResponse(status: 201)
    }

    def "registering auth requirement throws unauthorized"() {
        given:
        restClient.post(_) >> new FakeResponse(status: 401)

        when:
        client.requiresAuthentication(entity: 'E', action: 'C')

        then:
        thrown BinartaClient.Unauthorized
    }

    def "registering auth req throws forbidden"() {
        given:
        restClient.post(_) >> new FakeResponse(status: 403)

        when:
        client.requiresAuthentication(entity: 'E', action: 'C')

        then:
        thrown BinartaClient.Forbidden
    }

    def "registering auth req throws error"() {
        given:
        restClient.post(_) >> new FakeResponse(status: 500, data: 'data')

        when:
        client.requiresAuthentication([:])

        then:
        def e = thrown(BinartaClient.Error)
        e.message == 'data'
    }

}
