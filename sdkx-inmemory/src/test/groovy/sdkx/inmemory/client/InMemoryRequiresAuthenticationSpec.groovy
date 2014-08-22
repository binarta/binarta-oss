package sdkx.inmemory.client

import sdk.client.BinartaClient

class InMemoryRequiresAuthenticationSpec extends RequiresAuthenticationSpec {
    InMemoryBinartaClient client = new InMemoryBinartaClient(namespace:namespace)

    def authenticationRequirementWasRegistered(args) {
        [entity:args.entity, context:args.action] in client.authenticationPredicates[args.namespace]
    }
}
