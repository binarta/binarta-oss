package sdkx.rest.client

import sdk.client.BinartaClient
import sdkx.rest.client.config.BinartaConfig
import sdkx.rest.client.operations.factory.OperationFactory

import static client.factory.RestClientFactory.clientFor
import static com.google.common.io.BaseEncoding.base64
import static groovyx.net.http.ContentType.JSON

class RestBinartaClient implements BinartaClient {
    private static mergeLocalConfig = true

    def factory = new OperationFactory()
    def config

    RestBinartaClient(args) {
        config = new ConfigSlurper(args.env).parse(BinartaConfig)
        config.merge(localConfig(args))
        factory.restClient = clientFor(config.baseUrl)
        factory.defaultArgs = [
            headers           : ['X-Namespace': args.namespace, Authorization: toAuthorization(args)],
            requestContentType: JSON
        ]
    }

    private static localConfig(args) {
        def file = new File("${System.getProperty('user.home')}/.binarta/config.groovy")
        mergeLocalConfig && file.exists() ? new ConfigSlurper(args.env).parse(file.toURI().toURL()) : new ConfigObject()
    }

    def toAuthorization(args) {
        "Binarta ${base64().encode("$args.namespace:$args.username:$args.password".bytes)}"
    }

    def requiresAuthentication(args) {
        factory.requiresAuthenticationOperation().execute(args)
    }

    def isAuthenticationRequired(args) {
        factory.isAuthenticationRequired().execute(args)
    }

    def requiresPermission(args) {
        factory.requiresPermissionOperation().execute(args)
    }

    def getRequiredPermission(args) {
        factory.getRequiredPermissionOperation().execute(args)
    }
}
