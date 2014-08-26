package sdkx.rest.client

import client.RestClient
import client.factory.RestClientFactory
import sdk.client.BinartaClient
import sdkx.inmemory.client.IsPermissionRequiredSpec

import static com.google.common.io.BaseEncoding.base64
import static groovyx.net.http.ContentType.HTML

class RestGetRequiredPermissionSpec2 extends IsPermissionRequiredSpec {
    def restClient = GroovyMock(RestClient)
    RestBinartaClient client

    def setup() {
        RestBinartaClient.mergeLocalConfig = false
        RestClientFactory.metaClass.static.clientFor = { String baseUrl ->
            return restClient
        }
        client = new RestBinartaClient(namespace: namespace, username: 'U', password: 'P')
    }

    def cleanup() {
        GroovySystem.metaClassRegistry.removeMetaClass RestClientFactory
    }

    def permissionRequirementWasRegistered(args) {
        restClient.get([
            path   : "config/requires.permission.$args.entity.$args.action",
            query: [type:'boolean'],
            headers: [
                'X-Namespace': namespace,
                Authorization: "Binarta ${base64().encode("$namespace:U:P".bytes)}"
            ],
            requestContentType: HTML
        ]) >> new FakeResponse(status: 200, data: [value:args.permission])
    }

    def "handle 401"() {
        given:
        restClient.get(_) >> new FakeResponse(status: 401)

        when:
        client.getRequiredPermission(entity: 'E', action: 'C')

        then:
        thrown BinartaClient.Unauthorized
    }

    def "handle 403"() {
        given:
        restClient.get(_) >> new FakeResponse(status: 403)

        when:
        client.getRequiredPermission(entity: 'E', action: 'C')

        then:
        thrown BinartaClient.Forbidden
    }

    def "handle 404"() {
        given:
        restClient._ >> new FakeResponse(status:404)

        when:
        client.getRequiredPermission([:])

        then:
        thrown BinartaClient.NotFound
    }

    def "handle 500"() {
        given:
        restClient.get(_) >> new FakeResponse(status: 500, data: 'data')

        when:
        client.getRequiredPermission([:])

        then:
        def e = thrown(BinartaClient.Error)
        e.message == 'data'
    }
}
