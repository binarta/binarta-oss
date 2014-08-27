package sdkx.rest.client.operations.factory

import sdkx.rest.client.operations.ConfigureGatewayOperation
import sdkx.rest.client.operations.GetRequiredPermissionOperation
import sdkx.rest.client.operations.IsAuthenticationRequiredOperation
import sdkx.rest.client.operations.RequiresAuthenticationOperation
import sdkx.rest.client.operations.RequiresPermissionOperation

class OperationFactory {
    def restClient
    def defaultArgs

    def requiresAuthenticationOperation() {
        new RequiresAuthenticationOperation(restClient:restClient, defaultArgs:defaultArgs)
    }

    def isAuthenticationRequired() {
        new IsAuthenticationRequiredOperation(restClient: restClient, defaultArgs: defaultArgs)
    }

    def requiresPermissionOperation() {
        new RequiresPermissionOperation(restClient: restClient, defaultArgs: defaultArgs)
    }

    def getRequiredPermissionOperation() {
        new GetRequiredPermissionOperation(restClient: restClient, defaultArgs: defaultArgs)
    }

    def configureGatewayOperation() {
        new ConfigureGatewayOperation(restClient:restClient, defaultArgs: defaultArgs)
    }
}
