package sdkx.inmemory.client

import sdk.client.BinartaClient

class InMemoryBinartaClient implements BinartaClient {
    def namespace
    def authenticationPredicates = [:]
    def permissionPredicates = [:]

    def requiresAuthentication(args) {
        authenticationPredicates[namespace] << [entity:args.entity, context:args.action]
    }

    def isAuthenticationRequired(args) {
        authenticationPredicates[namespace].any { it.entity == args.entity && it.context == args.action }
    }

    def requiresPermission(args) {
        permissionPredicates[namespace] << [entity:args.entity, context:args.action, permission: args.permission]
    }

    def getRequiredPermissions(args) {
        permissionPredicates[namespace].findAll { it.entity == args.entity && it.context == args.action }.collect { it.permission }
    }

    void setNamespace(namespace) {
        this.namespace = namespace
        authenticationPredicates[namespace] = []
        permissionPredicates[namespace] = []
    }
}
