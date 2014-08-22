package sdkx.inmemory.client

class InMemoryGetRequiredPermissionsSpec extends IsPermissionRequiredSpec {
    InMemoryBinartaClient client = new InMemoryBinartaClient(namespace:namespace)

    def permissionRequirementWasRegistered(args) {
        client.requiresPermission(args)
    }
}
