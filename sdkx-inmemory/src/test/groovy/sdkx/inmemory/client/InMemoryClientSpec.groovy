package sdkx.inmemory.client

class InMemoryClientSpec extends BaseClientSpec {
    InMemoryBinartaClient client = new InMemoryBinartaClient(namespace:namespace)

    void assertEntityAdded(payload) {
        assert payload in client.additions[client.namespace]
    }
}
