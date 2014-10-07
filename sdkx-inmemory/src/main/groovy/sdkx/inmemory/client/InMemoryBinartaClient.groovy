package sdkx.inmemory.client

import sdk.client.BinartaClient

class InMemoryBinartaClient implements BinartaClient {
    def namespace
    def authenticationPredicates = [:]
    def permissionPredicates = [:]
    def gatewayConfigurations = [:]
    def additions = [:]

    def requiresAuthentication(args) {
        authenticationPredicates[namespace] << [entity:args.entity, context:args.action]
    }

    def isAuthenticationRequired(args) {
        authenticationPredicates[namespace].any { it.entity == args.entity && it.context == args.action }
    }

    def requiresPermission(args) {
        permissionPredicates[namespace] << [entity:args.entity, context:args.action, permission: args.permission]
    }

    def getRequiredPermission(args) {
        permissionPredicates[namespace].find { it.entity == args.entity && it.context == args.action }.permission
    }

    def configureGateway(args) {
        gatewayConfigurations[namespace][args.entity] = args.args
    }

    def addEntity(args) {
        additions[namespace] << args
    }

    void setNamespace(namespace) {
        this.namespace = namespace
        authenticationPredicates[namespace] = []
        permissionPredicates[namespace] = []
        additions[namespace] = []
        gatewayConfigurations[namespace] = [:]
    }
}
