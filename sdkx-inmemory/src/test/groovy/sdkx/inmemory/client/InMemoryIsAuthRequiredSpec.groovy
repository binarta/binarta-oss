package sdkx.inmemory.client

class InMemoryIsAuthRequiredSpec extends IsAuthRequiredSpec {
    InMemoryBinartaClient client = new InMemoryBinartaClient(namespace:namespace)

    def authenticationWasRegistered(Object args) {
        client.requiresAuthentication(args)
    }
}
