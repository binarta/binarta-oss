package sdkx.inmemory.client

class InMemoryRequiresPermissionSpec extends RequiresPermissionSpec {
    InMemoryBinartaClient client = new InMemoryBinartaClient(namespace:namespace)

    def permissionRequirementWasRegistered(Object args) {
        [entity:args.entity, context:args.action, permission:args.permission] in client.permissionPredicates[namespace]
    }
}
