package sdkx.rest.client

import client.RestClient
import client.factory.RestClientFactory
import sdk.client.BinartaClient
import sdkx.inmemory.client.RequiresPermissionSpec

import static com.google.common.io.BaseEncoding.base64
import static groovyx.net.http.ContentType.JSON

class RestRequiresPermissionSpec extends RequiresPermissionSpec {
    def restClient = GroovyMock(RestClient)
    RestBinartaClient client = new RestBinartaClient(namespace:namespace, username:'U', password:'P')

    def setup() {
        RestClientFactory.metaClass.static.clientFor = {String baseUrl->
            restClient
        }
        client = new RestBinartaClient(namespace:namespace, username:'U', password:'P')
    }

    def cleanup() {
        GroovySystem.metaClassRegistry.removeMetaClass RestClientFactory
    }

    def permissionRequirementWasRegistered(args) {
        1 * restClient.post([
            path              : '/config',
            body              : [
                id   : "requires.permission.$args.entity.$args.action",
                value: args.permission
            ],
            headers           : [
                'X-Namespace': namespace,
                Authorization: "Binarta ${base64().encode("$namespace:U:P".bytes)}"
            ],
            requestContentType: JSON
        ]) >> new FakeResponse(status: 201)
    }

    def "handle 401"() {
        given:
        restClient.post(_) >> new FakeResponse(status: 401)

        when:
        client.requiresPermission(entity: 'E', action: 'C')

        then:
        thrown BinartaClient.Unauthorized
    }

    def "handle 403"() {
        given:
        restClient.post(_) >> new FakeResponse(status: 403)

        when:
        client.requiresPermission(entity: 'E', action: 'C')

        then:
        thrown BinartaClient.Forbidden
    }

    def "handle 404"() {
        given:
        restClient._ >> new FakeResponse(status:404)

        when:
        client.requiresPermission([:])

        then:
        thrown BinartaClient.NotFound
    }

    def "handle 500"() {
        given:
        restClient.post(_) >> new FakeResponse(status: 500, data: 'data')

        when:
        client.requiresPermission([:])

        then:
        def e = thrown(BinartaClient.Error)
        e.message == 'data'
    }
}
